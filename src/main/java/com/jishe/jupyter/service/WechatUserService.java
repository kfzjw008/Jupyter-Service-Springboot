package com.jishe.jupyter.service;

import com.alibaba.fastjson.JSONObject;
import com.jishe.jupyter.component.JWT;
import com.jishe.jupyter.component.RequestUtil;
import com.jishe.jupyter.entity.Board;
import com.jishe.jupyter.entity.Feedback;
import com.jishe.jupyter.entity.Integral;
import com.jishe.jupyter.entity.WechatUser;
import com.jishe.jupyter.global;
import com.jishe.jupyter.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private FeedBackRepoistory feedBackRepoistory;

    @Autowired
    private BoardRepository BoardRepository;

    private String appid = "wxe57f6e85d8263355";
    private String appsecret = "53d5c7fbfd6b286d71c9bb7dbb6fff20";
    WechatUser user = new WechatUser();
    private String OpenId;
    private String session_key;
    private String qqappid = "1110395793";
    private String qqappsecret = "ZshmQwu0bgNWCRDR";

    /**
     * @return :WechatUser对象
     * @name: 用户登录模块
     * @description: 微信用户登录服务模块，该方法用于实现用户登录。
     * @input: WechatUser对象，一般只包括code
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-08 22:35
     **/
    public WechatUser Login(WechatUser user,int pt) {//默认为空，微信小程序。1为qq小程序
        String str1="";
        if(pt==0){
            str1="微信-";
        }
        if(pt==1){
            str1="QQ-";
        }
        global glo = new global();
        RequestUtil request = new RequestUtil();
        String resultString ="";
        if(pt==0){
            System.out.println("wx");
 resultString = request.CreateRequestUtil("https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appsecret + "&js_code=" + user.getCode() + "&grant_type=authorization_code");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(resultString);
            System.out.println(resultString);
            session_key = jsonObject.get("session_key") + "";
            OpenId = jsonObject.get("openid") + "";
            System.out.println("session_key==" + session_key);
            System.out.println("OpenId==" + OpenId);
        }
     if(pt==1){
         System.out.println("qq");
  resultString = request.CreateRequestUtil("https://api.q.qq.com/sns/jscode2session?appid=" + qqappid + "&secret=" + qqappsecret + "&js_code=" + user.getCode() + "&grant_type=authorization_code");
         JSONObject jsonObject = (JSONObject) JSONObject.parse(resultString);
         session_key = jsonObject.get("session_key") + "";
         OpenId = jsonObject.get("openid") + "";
         System.out.println("session_key==" + session_key);
         System.out.println("OpenId==" + OpenId);
     }
        // 解析json

        System.out.println("https://api.q.qq.com/sns/jscode2session?appid=" + qqappid + "&secret=" + qqappsecret + "&js_code=" + user.getCode() + "&grant_type=authorization_code");

        WechatUser LoginUser = new WechatUser();
        JWT util = new JWT();

        if (userfindRepository.find(OpenId) != null) {
            System.out.println(OpenId);
            LoginUser = userfindRepository.find(OpenId);
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
            System.out.println("fg");
            user.setOpenId(OpenId);
            user.setSession(session_key);
            user.setNickname(str1+user.getNickname());
            String token = util.createJWT(glo.getExpiration_time(), user);
            System.out.println(token);
            user.setJWTToken(token);
            userRepository.save(user);
            userRepository.refresh(user);
        }

        return user;
    }

    //用户资料维护
    public WechatUser modifyUser(String school, String schoolid, String name, String openid, String token) {
        WechatUser u = userfindRepository.find(openid);
        u.setName(name);
        u.setSchool(school);
        u.setSchoolid(schoolid);
        userRepository.save(u);
        return u;
    }

    //反馈添加
    public String feedback(String name, String title, String content, String tel, int questionnumber) {
        Feedback fd = new Feedback();
        fd.setContent(content);
        fd.setQuestionnumber(questionnumber);
        fd.setTel(tel);
        fd.setTitle(title);
        fd.setUsername(name);
        feedBackRepoistory.save(fd);
        feedBackRepoistory.refresh(fd);
        return "Success";
    }

    //公告添加与返回
    public Page<Board> board(Pageable page) {
        return BoardRepository.findAll(page);
    }

    public Page<Integral> alljf(Pageable page) {
        return JFRepoistory.findAll(page);
    }
    public Page<Feedback> allfeedback(Pageable page) {
        return feedBackRepoistory.allfeedback(page);
    }
    public Page<WechatUser> all(Pageable page) {
        return userRepository.all(page);
    }
    public Map insertboard(String content, String title) {
        Board b = new Board();
        b.setContent(content);
        b.setTitle(title);
        BoardRepository.save(b);
        BoardRepository.refresh(b);
        return Map.of("Result", b);
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
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime minTime = localDateTime.with(LocalTime.MIN);
        LocalDateTime maxTime = localDateTime.with(LocalTime.MAX);
        if (!VerifyJWT(token)) return Map.of("Message", "Failed");
        Map<Object, Object> ALL = new HashMap<>();
        Map<Object, Object> scoremap = new HashMap<>();
        Map<Object, Object> statusmap = new HashMap<>();
        System.out.println("cao" + userfindRepository.find(openid));
        int a1, a2, a3, a4,a5;
        System.out.println(minTime);
        System.out.println(maxTime);
        if (JFRepoistory.todayscore(userfindRepository.find(openid).getId(), "练习积分", minTime, maxTime) == null) {
            a1 = 0;
        } else {
            a1 = JFRepoistory.todayscore(userfindRepository.find(openid).getId(), "练习积分", minTime, maxTime);
        }
        if (JFRepoistory.todayscore(userfindRepository.find(openid).getId(), "连对积分", minTime, maxTime) == null) {
            a2 = 0;
        } else {
            a2 = JFRepoistory.todayscore(userfindRepository.find(openid).getId(), "连对积分", minTime, maxTime);
        }
        if (JFRepoistory.todayscore(userfindRepository.find(openid).getId(), "签到积分", minTime, maxTime) == null) {
            a3 = 0;
        } else {
            a3 = JFRepoistory.todayscore(userfindRepository.find(openid).getId(), "签到积分", minTime, maxTime);
        }
        if (JFRepoistory.todayscore(userfindRepository.find(openid).getId(), "分享积分", minTime, maxTime) == null) {
            a4 = 0;
        } else {
            a4 = JFRepoistory.todayscore(userfindRepository.find(openid).getId(), "分享积分", minTime, maxTime);
        }
        if (JFRepoistory.todayscore(userfindRepository.find(openid).getId(), "视频积分", minTime, maxTime) == null) {
            a5 = 0;
        } else {
            a5 = JFRepoistory.todayscore(userfindRepository.find(openid).getId(), "视频积分", minTime, maxTime);
        }
        System.out.println();
        scoremap.put("Practice", a1);
        scoremap.put("Right", a2);
        scoremap.put("QianDao", a3);
        scoremap.put("Share", a4);
        scoremap.put("sp", a5);
        //true为允许进行任务
        statusmap.put("Practice", verityJF("练习积分", 1, openid));
        statusmap.put("Right", verityJF("连对积分", 10, openid));
        statusmap.put("QianDao", verityJF("签到积分", 10, openid));
        statusmap.put("Share", verityJF("分享积分", 8, openid));
        statusmap.put("sp", verityJF("视频积分", 8, openid));
        ALL.put("Score", scoremap);
        ALL.put("Status", statusmap);
        ALL.put("ALLScoreToday", JFRepoistory.todayallscore(userfindRepository.find(openid).getId(), minTime, maxTime));
        ALL.put("AllScore", JFRepoistory.allscore(userfindRepository.find(openid).getId()));
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
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime minTime = localDateTime.with(LocalTime.MIN);
        LocalDateTime maxTime = localDateTime.with(LocalTime.MAX);
        System.out.println(minTime+" "+maxTime);
        List<Integral> j = JFFindRepoistory.findByNameAndInsertTimeBetween2(userfindRepository.find(openid).getId(),name, minTime, maxTime);
        System.out.println(j);
        System.out.println("size::"+name+j.size());
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
        if (name.equals("视频积分")) {
            return j.size() < 10 ;
        }
        if (name.equals("活动积分")) {
            return true;
        } else return false;
    }
}
