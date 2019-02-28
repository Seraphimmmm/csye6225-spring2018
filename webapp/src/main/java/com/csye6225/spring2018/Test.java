package com.csye6225.spring2018;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("deprecation")
public class Test {
    @Value("{$accessKey}")
    private String key;

    @Value(("{$secretKey}"))
    private String value;

    private static AWSCredentials credentials;

    static AmazonS3 amazon;

    private static void init() {

        amazon = new AmazonS3Client(new ProfileCredentialsProvider());
    }
    public  static void main(String args[]) throws MalformedURLException,IOException{
        init();
        String aboutMe = "6788";
       /* S3Object o = amazon.getObject("csye6225-huangre","pa.jpg");
        String Url = o.getObjectContent().getHttpRequest().getURI().toString();
        System.out.println("Url "+Url);
        amazon.deleteObject("csye6225-huangre","dsa-image.jpg");

        System.out.println("delete done");
        */
       amazon.putObject("csye6225-huangre","Aboutme",aboutMe);
       amazon.setObjectAcl("csye6225-huangre","Aboutme", CannedAccessControlList.PublicRead);
       S3Object o = amazon.getObject("csye6225-huangre","Aboutme");
       URL url = o.getObjectContent().getHttpRequest().getURI().toURL();
        URLConnection uc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String inputLine;
        String who ="";
        while ( (inputLine = in.readLine()) != null) {
            who+=inputLine.toString();
        }
       /* StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(uri);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
        */
        System.out.println(who);
    }
}
