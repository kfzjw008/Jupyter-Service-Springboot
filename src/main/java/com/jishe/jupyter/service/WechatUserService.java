package com.jishe.jupyter.service;

import com.alibaba.fastjson.JSONObject;
import com.jishe.jupyter.component.JWT;
import com.jishe.jupyter.component.RequestUtil;
import com.jishe.jupyter.entity.Integral;
import com.jishe.jupyter.entity.WechatUser;
import com.jishe.jupyter.global;
import com.jishe.jupyter.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jishe.jupyter.repository.JFRepoistory;
import com.jishe.jupyter.repository.JFFindRepoistory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: jupyter
 * @description: 微信用户服务模块，仅供微信小程序用户登录使用，主要包括登录、用户资料修改，积分等等只有登录用户可以使用的模块。
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

    @Autowired
    private JFFindRepoistory JFFindRepoistory;
    @Autowired
    private JFRepoistory JFRepoistory;

    private String appid = "wxe57f6e85d8263355";
    private String appsecret = "53d5c7fbfd6b286d71c9bb7dbb6fff20";
    WechatUser user = new WechatUser();
    private String OpenId;
    private String session_key;
    LocalDateTime localDateTime = LocalDateTime.now();
    LocalDateTime minTime = localDateTime.with(LocalTime.MIN);
    LocalDateTime maxTime = localDateTime.with(LocalTime.MAX);

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
        RequestUtil request = new RequestUtil();
        String resultString = request.CreateRequestUtil("https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appsecret + "&js_code=" + user.getCode() + "&grant_type=authorization_code");
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
            System.out.println(token);
            LoginUser.setJWTToken(token);
            user = LoginUser;
            userRepository.save(LoginUser);
        } else {
            user.setOpenId(OpenId);
            user.setSession(session_key);
            String token = util.createJWT(glo.getExpiration_time(), user);
            System.out.println(token);
            user.setJWTToken(token);
            userRepository.save(user);
            userRepository.refresh(user);
        }

        return user;
    }


    //添加积分模块
    public String AddIntergal(int count, String name, String token, String openid) {
        if (!VerifyJWT(token)) return "Failed";
        if (verityJF(name, count, openid)) {
            WechatUser user = userfindRepository.find(openid);
            Integral i = new Integral();
            i.setName(name);
            i.setCount(count);
            i.setWechatUser(user);
            JFRepoistory.save(i);
            JFRepoistory.refresh(i);
            return "Success";
        } else {
            return "Failed";
        }

    }

    //返回用户积分模块
    public Map UserIntergal(String token, String openid) {
        if (!VerifyJWT(token)) return Map.of("Message", "Failed");
        Map<Object, Object> ALL= new HashMap<>();
        Map<Object, Object> scoremap= new HashMap<>();
        Map<Object, Object> statusmap= new HashMap<>();
        scoremap.put("Practice", JFRepoistory.todayscore(userfindRepository.find(OpenId).getId(), "练习积分", minTime, maxTime));
        scoremap.put("Right", JFRepoistory.todayscore(userfindRepository.find(OpenId).getId(), "连对积分", minTime, maxTime));
        scoremap.put("QianDao", JFRepoistory.todayscore(userfindRepository.find(OpenId).getId(), "签到积分", minTime, maxTime));
        scoremap.put("Share", JFRepoistory.todayscore(userfindRepository.find(OpenId).getId(), "分享积分", minTime, maxTime));
        //true为允许进行任务
        statusmap.put("Practice", verityJF("练习积分", 1, OpenId));
        statusmap.put("Right", verityJF("连对积分", 10, OpenId));
        statusmap.put("QianDao", verityJF("签到积分", 10, OpenId));
        statusmap.put("Share", verityJF("分享积分", 8, OpenId));
        ALL.put("Score", scoremap);
        ALL.put("Status", statusmap);
        ALL.put("ALLScoreToday",JFRepoistory.todayallscore(userfindRepository.find(OpenId).getId(), minTime, maxTime) );
        ALL.put("AllScore", JFRepoistory.allscore(userfindRepository.find(OpenId).getId()));
        return ALL;
    }

    public static boolean VerifyJWT(String token) {
        return QuestionService.VerifyJWT(token);
    }

    public boolean verityJF(String name, int count, String openid) {
/*
*1、练习积分：每日练习题目数量（含所有做题数量）每5题积分+1，每日上限50。name=练习积分,一次+1，一天50次。
2、连对积分：挑战模式连对5题积分+10，连对10题再加10，每日上限20。//前端判断连对题数，直接传回通用接口,name=连对积分
2、签到积分：每日签到积分+3，连续签到，第二天积分比第一天+1，最高为10，每日一次。name=签到积分
3、分享积分：分享一次+8，每日上限3次。name=分享积分
* */

        List<Integral> j = JFFindRepoistory.findByNameAndInsertTimeBetween(name, minTime, maxTime);
        if (name.equals("练习积分")) {
            return j.size() < 50 && count == 1;
        }
        if (name.equals("连对积分")) {
            return j.size() < 2 && count == 10;
        }
        if (name.equals("签到积分")) {
            return j.size() < 1 && count <= 10;
        }
        if (name.equals("分享积分")) {
            return j.size() < 3 && count == 8;
        }
        if(name.equals("活动积分")){
            return true;
        }else return false;
    }
}
