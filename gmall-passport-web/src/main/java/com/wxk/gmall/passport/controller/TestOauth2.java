package com.wxk.gmall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.wxk.gmall.util.HttpclientUtil;

import java.util.HashMap;
import java.util.Map;

public class TestOauth2 {

    public static String getCode(){

        // 1 获得授权码
        // 187638711
        // http://passport.gmall.com:8085/vlogin

        String s1 = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=887348261&response_type=code&redirect_uri=http://passport.gmall.com:8085/vlogin");
        System.out.println(s1);


        String s2 = "http://passport.gmall.com:8085/vlogin?code=92a9803a9095eebe89dbd6526b9b5a6d";
        // 在第一步和第二部返回回调地址之间,有一个用户操作授权的过程
        // 2 返回授权码到回调地址
        return null;
    }

    public static String getAccess_token(){
        // 换取access_token
        // client_secret=44db05a21af06c583fc5b9d36c95b44c
        // client_id=887348261
        String s3 = "https://api.weibo.com/oauth2/access_token?client_id=887348261&client_secret=44db05a21af06c583fc5b9d36c95b44c&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8085/vlogin&code=CODE";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id","887348261");
        paramMap.put("client_secret","44db05a21af06c583fc5b9d36c95b44c");
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri","http://passport.gmall.com:8085/vlogin");
        paramMap.put("code","44db05a21af06c583fc5b9d36c95b44c");// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        String access_token_json = HttpclientUtil.doPost(s3, paramMap);

       Map<String,String> access_map = JSON.parseObject(access_token_json,Map.class);

       System.out.println(access_map.get("access_token"));
       System.out.println(access_map.get("uid"));

        return access_map.get("access_token");
    }

    public static Map<String,String> getUser_info(){

        // 4 用access_token查询用户信息
        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.00HMAs7H0p5_hMdbefcb34140Lydjf&uid=6809985023";
        String user_json = HttpclientUtil.doGet(s4);
        Map<String,String> user_map = JSON.parseObject(user_json,Map.class);

        System.out.println(user_map.get("1"));

        return user_map;
    }


    public static void main(String[] args) {
        //String s1 = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=887348261&response_type=code&redirect_uri=http://passport.gmall.com:8085/vlogin");
        //System.out.println(s1);
//        String s2 = "http://passport.gmall.com:8085/vlogin?code=a6b1a40c1b32fbff512a17168094ca64";    //code
//        String s3 = "https://api.weibo.com/oauth2/access_token";//?client_id=887348261&client_secret=44db05a21af06c583fc5b9d36c95b44c&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8085/vlogin&code=CODE";
//        Map<String,String> paramMap = new HashMap<>();
//        paramMap.put("client_id","887348261");
//        paramMap.put("client_secret","44db05a21af06c583fc5b9d36c95b44c");
//        paramMap.put("grant_type","authorization_code");
//        paramMap.put("redirect_uri","http://passport.gmall.com:8085/vlogin");
//        paramMap.put("code","a6b1a40c1b32fbff512a17168094ca64");// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
//        String access_token_json = HttpclientUtil.doPost(s3, paramMap);
//
//        Map<String,String> access_map = JSON.parseObject(access_token_json,Map.class);
//
//        System.out.println(access_map.get("access_token"));
//        System.out.println(access_map.get("uid"));
//
//        // 4 用access_token查询用户信息
//        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.00dzK_ZG0joNDydf0cdede03dJyGJE&uid=6809985023";    //access_token
//        String user_json = HttpclientUtil.doGet(s4);
//        Map<String,String> user_map = JSON.parseObject(user_json,Map.class);
//
//        System.out.println(user_map.get("1"));

        getCode();
    }
}
