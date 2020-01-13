package com.jishe.jupyter.service;

import com.alibaba.fastjson.JSONObject;
import com.jishe.jupyter.component.JWT;
import com.jishe.jupyter.component.RequestUtil;
import com.jishe.jupyter.entity.WechatUser;
import com.jishe.jupyter.repository.UserRepository;
import com.jishe.jupyter.repository.UserfindRepository;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

import com.jishe.jupyter.global;

/**
 * @program: jupyter
 * @description: 微信用户服务模块，仅供微信小程序用户登录使用，主要包括登录、用户资料修改等等只有登录用户可以使用的模块。
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-08 22:32
 **/
@Service
@Transactional
public class WechatUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserfindRepository userfindRepository;
    private String appid = "wxe57f6e85d8263355";
    private String appsecret = "53d5c7fbfd6b286d71c9bb7dbb6fff20";
    WechatUser user = new WechatUser();
    private String OpenId;
    private String session_key;


    /**
     * @return :WechatUser对象
     * @name: 用户登录模块
     * @description: 微信用户登录服务模块，该方法用于实现用户登录。
     * @input: WechatUser对象，一般只包括code
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-08 22:35
     **/
    public WechatUser Login(WechatUser user) {
        global glo = new global();
        RequestUtil request= new RequestUtil();
        String  resultString= request.CreateRequestUtil("https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appsecret + "&js_code=" + user.getCode() + "&grant_type=authorization_code");
        // 解析json
        JSONObject jsonObject = (JSONObject) JSONObject.parse(resultString);
        session_key = jsonObject.get("session_key") + "";
        OpenId = jsonObject.get("openid") + "";
        System.out.println("session_key==" + session_key);
        System.out.println("OpenId==" + OpenId);
        WechatUser LoginUser = new WechatUser();
        JWT util = new JWT();

        if (userfindRepository.find(OpenId) != null) {
            LoginUser = userfindRepository.find(OpenId);
            LoginUser.setNickname(user.getNickname());
            LoginUser.setProvince(user.getProvince());
            LoginUser.setCity(user.getCity());
            LoginUser.setCode(user.getCode());
            LoginUser.setOpenId(OpenId);
            LoginUser.setSession(session_key);
            String token = util.createJWT(glo.getExpiration_time(), LoginUser);
            LoginUser.setJWTToken(token);
            user = LoginUser;
            userRepository.save(LoginUser);
        } else {
            user.setOpenId(OpenId);
            user.setSession(session_key);
            String token = util.createJWT(glo.getExpiration_time(),user);
            user.setJWTToken(token);
            userRepository.save(user);
            userRepository.refresh(user);
        }
        return user;
    }
}
