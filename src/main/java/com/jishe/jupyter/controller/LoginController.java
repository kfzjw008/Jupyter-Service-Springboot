package com.jishe.jupyter.controller;

import com.jishe.jupyter.entity.WechatUser;
import com.jishe.jupyter.service.WechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: jupyter
 * @description: 基于实现用户登录的业务层
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-08 22:20
 **/
@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {
     @Autowired
     private WechatUserService UserService;
    @GetMapping("/wxlogin")
    public Map getLogin(WechatUser user) {
        user=UserService.Login(user);
        return Map.of("OpenId", user);
    }
}
