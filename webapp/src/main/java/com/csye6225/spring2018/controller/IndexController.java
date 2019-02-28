package com.csye6225.spring2018.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.csye6225.spring2018.dao.UserDAO;
import com.csye6225.spring2018.domain.AmazonS3Util;

import com.csye6225.spring2018.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;


@Controller

public class IndexController {

  private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
  @Autowired
  private UserDAO userDAO;

    @Autowired
    private AmazonS3Util amazonS3Util;

  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  
  @RequestMapping({"/","/index"})
  public String index(RedirectAttributes redirectAttributes,User user) {

    return "index";
  }
  @PostMapping(value="/search")
  public String search(@ModelAttribute("user") User user,Model model,HttpServletRequest request) {
      String searchFailure = "The user does not exist";
      User temperUser = userDAO.findUserByEmailAddress(user.getEmailAddress());
      if(temperUser == null) {
          model.addAttribute("searchFail",searchFailure);
          return "index";
      }
      model.addAttribute("visitName",user.getEmailAddress());
      String imageUrl;
      S3Object o = amazonS3Util.getS3Object(temperUser.getEmailAddress()+"-image");
      if(o == null) imageUrl = "";
      else imageUrl = /"+temperUser.getEmailAddress()+"-image";
      model.addAttribute("imageUrl",imageUrl);
      logger.info("index: "+imageUrl);



      if(temperUser.getAboutMe() == null) {temperUser.setAboutMe("");logger.info("about me does not exit");}
      else {
          model.addAttribute("AboutMe",temperUser.getAboutMe());

      }


      return "info";
  }
  @GetMapping(value = "/register")
    public String register(User user) {

      return "register";
  }

  @RequestMapping(value = "/addRegister",method = RequestMethod.POST)
    public String save(@ModelAttribute("user") User user,Model model) {
          String failure = "The email has been registered";
          String failure2 = "The password's length is too long or too short";
          logger.info("user email :"+user.getEmailAddress());
          User tempUser = userDAO.findUserByEmailAddress(user.getEmailAddress());
          if(tempUser!= null) {
              model.addAttribute("rresult", failure);
              return "register";
          }
          else if(user.getPassword().length()<5 || user.getPassword().length()>15) {
              model.addAttribute("rresult", failure2);
              return "register";
          }
          else {
              user.setPassword(passwordEncoder.encode(user.getPassword()));
              userDAO.save(user);
              return "success";
          }

  }

  @RequestMapping("/success")
    public String success() {
      return "success";
  }

  @GetMapping(value = "/logIn")
    public String logIn(Model model,User user) {
      return "logIn";
  }

  @PostMapping(value = "/logIn")
    public String verify(Model model,@ModelAttribute("user") User user,RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {
      User tempUser = userDAO.findUserByEmailAddress(user.getEmailAddress());
      request.getSession().setAttribute("name",user.getEmailAddress());
      model.addAttribute("name",user.getEmailAddress());
      logger.info(user.getEmailAddress());
      if(tempUser == null) {
          model.addAttribute("lresult","The user does not exist");
          return "logIn";
      }
      else if(!passwordEncoder.matches(user.getPassword(),tempUser.getPassword()))
      {
          logger.info("wrong password");
          model.addAttribute("lresult","wrong password");
          return "logIn";
      }
      else {
          /*String image = user.getEmailAddress()+"-image";
          String imageUrl;
          S3Object o = amazonS3Util.getS3Object(image);

          else imageUrl = o.getObjectContent().getHttpRequest().getURI().toString();
          logger.info("login imageURl: "+imageUrl);
          redirectAttributes.addAttribute("imageUrl",imageUrl);
*/
          return "redirect:/management";
      }
  }

  @GetMapping("/forget")
  public String forget(@ModelAttribute("user") User user) {return "forget";}

@Deprecated
  @PostMapping("/forget")
  public String link(Model model,@ModelAttribute("user") User user) {
      String email = user.getEmailAddress();
      AmazonSNSClient snsClient = new AmazonSNSClient();

      
      return "link";
  }
  @GetMapping("/home")
    public String home(Model model) {
      return "home";
  }

    @GetMapping(value = "/management")
    public String manage(Model model,HttpServletRequest request) throws MalformedURLException,IOException{
        String username = request.getSession().getAttribute("name").toString();
        String image = username+"-image";
        String imageUrl;
        String aboutMe = username+"-Aboutme";
        String aMUrl;
        S3Object o = amazonS3Util.getS3Object(image);
        if(o == null) imageUrl = "https://s3.amazonaws.com/csye6225-huangre/placehold.png";

        else imageUrl = "https://s3.amazonaws.com/web-app.csye6225-spring2018-huangre.me/"+image;
        logger.info("get imageUrl: "+imageUrl );
        model.addAttribute("imageUrl",imageUrl);
        logger.info("get management: "+username);
        User temp = userDAO.findUserByEmailAddress(username);
        if(temp.getAboutMe()==null) temp.setAboutMe("");
        else
            model.addAttribute("aboutMe",temp.getAboutMe());
        return "management";
    }

    @PostMapping(value="/upload")
    public String Manage(@RequestParam("file") MultipartFile file,
                         HttpServletRequest request,
                         Model model) {
        String name = request.getSession().getAttribute("name").toString();
        logger.info("post management: "+name);

        String[] a = file.getOriginalFilename().split("\\.");

        String regex = a[a.length-1];
        model.addAttribute("name",name);
        if(!regex.equals("jpg") && !regex.equals("png") && !regex.equals("jpeg")) {
            String hint = "The image format must be jpg or jpeg or png";
            model.addAttribute("error",hint);
            return "management";
        }
        String imageName = name+"-image";
        amazonS3Util.upload(imageName,file);
        return "redirect:/management";
    }

    @PostMapping("/updateString")
    public String updateAboutMe(HttpServletRequest request) {
      String about = request.getParameter("Aboutme");
      logger.info("aboutme: "+about);
      String username = request.getSession().getAttribute("name").toString();
      User temp = userDAO.findUserByEmailAddress(username);
      temp.setAboutMe(about);
      return "redirect:/management";
    }
    @PostMapping(value="/delete")
    public String DeleteImg(HttpServletRequest request) {
      String username = request.getSession().getAttribute("name").toString();
      String image = username + "-image";
      amazonS3Util.delete(image);
      return "redirect:/management";
    }
}
