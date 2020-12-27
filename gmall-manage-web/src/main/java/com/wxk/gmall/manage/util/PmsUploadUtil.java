package com.wxk.gmall.manage.util;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author wxk
 * @creat 2020-10-29 14:47
 */

public class PmsUploadUtil {


    public static String uploadImage(MultipartFile multipartFile) {

        //上传图片到服务器

        String imgUrl = "http://192.168.254.131";

        String file = PmsUploadUtil.class.getResource("/tracker.conf").getFile();
        try {
            ClientGlobal.init(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer= null;
        try {
            trackerServer = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StorageClient storageClient=new StorageClient(trackerServer,null);



        try {

            byte[] bytes = multipartFile.getBytes(); //上传文件的二进制
            String originalFilename = multipartFile.getOriginalFilename();  //a.b.c.d.jpg

            int lastIndex = originalFilename.lastIndexOf(".");
            String extName = originalFilename.substring(lastIndex+1);   //jpg


            String[]  upload_file = storageClient.upload_file(bytes, extName, null);
            //String url = "http://192.168.254.131";
            for (int i = 0; i < upload_file.length; i++) {
                String s = upload_file[i];
                imgUrl+="/"+s;
                System.out.println("s = " + s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return imgUrl;
    }
}
