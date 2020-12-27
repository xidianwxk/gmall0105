package com.wxk.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.wxk.gmall.bean.UmsMember;
import com.wxk.gmall.service.UserService;
import com.wxk.gmall.util.HttpclientUtil;
import com.wxk.gmall.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wxk
 * @creat 2020-12-12 14:01
 */

@Controller
public class PassportController {

    @Reference
    UserService userService;


    @RequestMapping("login")
    @ResponseBody
    public String login(UmsMember umsMember , HttpServletRequest request){

        String token = "";

        //调用用户服务，验证用户名+密码
        UmsMember umsMemberLogin = userService.login(umsMember);

        if(umsMemberLogin!=null){
            //登录成功
            //jwt制作token
            String memberLoginId = umsMemberLogin.getId();
            String umsMemberLoginNickname = umsMemberLogin.getNickname();
            Map<String , Object> userMap = new HashMap<>();
            userMap.put("memberId" , memberLoginId);
            userMap.put("nickname" , umsMemberLoginNickname);

            //盐值
            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();// 从request中获取ip
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }

            // 将来需要改进的  ！！  按照设计的算法对参数进行加密后，生成token
            token = JwtUtil.encode("2019gmall0105", userMap, ip);
            // 将token存入redis一份
            userService.addUserToken(token,memberLoginId);

        }else{
            // 登录失败
            token = "fail";
        }

        return token;
    }

    @RequestMapping("index")
    public String index(String ReturnUrl , ModelMap modelMap){
        modelMap.put("ReturnUrl" , ReturnUrl);
        return "index";
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token,String currentIp,HttpServletRequest request){
        // 通过jwt校验token真假
        Map<String,String> map = new HashMap<>();

        Map<String, Object> decode = JwtUtil.decode(token, "2019gmall0105", currentIp);

        if(decode!=null){
            map.put("status","success");
            map.put("memberId",(String)decode.get("memberId"));
            map.put("nickname",(String)decode.get("nickname"));
        }else{
            map.put("status","fail");
        }
        return JSON.toJSONString(map);
    }


    //微博登录
    @RequestMapping("vlogin")
    //@ResponseBody
    public String vlogin(String code,HttpServletRequest request){

        // 授权码换取access_token
        // 换取access_token

        String s3 = "https://api.weibo.com/oauth2/access_token?";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id","887348261");
        paramMap.put("client_secret","44db05a21af06c583fc5b9d36c95b44c");
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri","http://passport.gmall.com:8085/vlogin");
        paramMap.put("code",code);// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        String access_token_json = HttpclientUtil.doPost(s3, paramMap);

        Map<String,Object> access_map = JSON.parseObject(access_token_json,Map.class);

        // access_token换取用户信息
        String uid = (String)access_map.get("uid");
        String access_token = (String)access_map.get("access_token");
        String show_user_url = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;
        String user_json = HttpclientUtil.doGet(show_user_url);
        Map<String,Object> user_map = JSON.parseObject(user_json,Map.class);

        // 将用户信息保存数据库，用户类型设置为微博用户
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceType("2");
        umsMember.setAccessCode(code);
        umsMember.setAccessToken(access_token);
        umsMember.setSourceUid((String)user_map.get("idstr"));
        umsMember.setCity((String)user_map.get("location"));
        umsMember.setNickname((String)user_map.get("screen_name"));
        String g = "0";
        String gender = (String)user_map.get("gender");
        if(gender.equals("m")){
            g = "1";
        }
        umsMember.setGender(g);

        UmsMember umsCheck = new UmsMember();
        umsCheck.setSourceUid(umsMember.getSourceUid());
        //通过UID  检查用户是否已经存在
        UmsMember umsMemberCheck = userService.checkOauthUser(umsCheck);

        if(umsMemberCheck == null){
            //未社交登陆过  不用保存
            umsMember = userService.addOauthUser(umsMember);
        }else{

            umsMember = umsMemberCheck;
        }

        // 生成jwt的token，并且重定向到首页，携带该token   ==》  抽取一个方法
        String token = null;
        String memberId = umsMember.getId();      //rpc的主键返回策略失效
        String nickname = umsMember.getNickname();
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("memberId",memberId);
        userMap.put("nickname",nickname);


        String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
        if(StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();// 从request中获取ip
            if(StringUtils.isBlank(ip)){
                ip = "127.0.0.1";
            }
        }

        // 按照设计的算法对参数进行加密后，生成token
        token = JwtUtil.encode("2019gmall0105", userMap, ip);

        // 将token存入redis一份
        userService.addUserToken(token,memberId);


        return "redirect:http://search.gmall.com:8083/index?token="+token;
    }

}


    //进一步完善
//    @RequestMapping("login")
//    @ResponseBody
//    public String login(UmsMember umsMember){
//
//        //调用用户服务，验证用户名+密码
//        return "token";
//    }
//
//    @RequestMapping("index")
//    public String index(String ReturnUrl , ModelMap modelMap){
//
//        modelMap.put("ReturnUrl" , ReturnUrl);
//        return "index";
//    }
//
//    @RequestMapping("verify")
//    @ResponseBody
//    public String verify(String token){
//
//        // 通过jwt校验token真假
//        return "success";
//    }

