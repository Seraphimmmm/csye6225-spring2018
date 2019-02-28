package com.csye6225.spring2018.domain;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
@SuppressWarnings("deprecation")
public class AmazonS3Util {
    private final static Logger logger = LoggerFactory.getLogger(AmazonS3Util.class);

    private String bucketName;


    private AmazonS3 s3;

    private void init() {
        if(s3 == null){

            s3 = new AmazonS3Client();
            bucketName = "web-app.csye6225-spring2018-huangre.me";

        }
    }

    public void upload(String key, MultipartFile file) {
        init();
        //logger.info("init done");
        if(s3.doesObjectExist(bucketName,key)) delete(key);


        try {
            InputStream is = file.getInputStream();
            //logger.info("data done: ");
            ObjectMetadata omd = new ObjectMetadata();
            omd.setContentType(file.getContentType());
            omd.setContentLength(file.getSize());
            omd.setHeader("filename", file.getName());

            s3.putObject(new PutObjectRequest(bucketName, key, is, omd).withCannedAcl(CannedAccessControlList.PublicRead));
            //logger.info("s3 put object done");
        }catch (Exception e) {e.getStackTrace();}

    }

    public void delete(String key) {
        if(s3 == null) init();
        if(s3.doesObjectExist(bucketName,key)) {
            logger.info("starting deleting");
            s3.deleteObject(bucketName, key);
        }

        else {logger.info("not delete");return;}
    }

    public S3Object getS3Object(String key) {
        if(s3 == null) init();
        if(!s3.doesObjectExist(bucketName,key)) return  null ;
        return s3.getObject(bucketName,key);
    }
    public void uploadString(String key,String content) {
        s3.putObject(bucketName,key,content);
        s3.setObjectAcl(bucketName,key,CannedAccessControlList.PublicRead);
    }
    public AmazonS3 getS3() {
        if(s3 == null) init();
        return this.s3;
    }


}
