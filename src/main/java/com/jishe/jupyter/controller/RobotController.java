package com.jishe.jupyter.controller;

import com.jishe.jupyter.component.JWT;
import com.jishe.jupyter.entity.WechatUser;
import com.jishe.jupyter.service.QuestionService;
import com.jishe.jupyter.service.RankService;
import com.jishe.jupyter.service.WechatUserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: jupyter
 * @description: 机器人模块
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-11-24 15:21
 **/
@Slf4j
@RestController
@RequestMapping("/api/robot")
public class RobotController {

    @Autowired
    private RankService RankService;
    @Autowired
    private WechatUserService WechatUserService;
    @Autowired
    MeterRegistry registry;
    private Counter AddIntergal;
    private Counter UserIntergal;
    private Counter modifyUser;
    private Counter Verifytoken;
    @GetMapping("/talk")
    public Map Talk(String content) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date date = new Date(System.currentTimeMillis());
        String STR=" ";
        Map<String, String> map = new HashMap<String,String>();
        //以下开始判断返回值
if(content.equals("你好")){
    STR="你好！我是智能小星！你可以向我提出天文的问题，我都会尝试解答的！当然我的功能也正在不断完善中，有的问题还不能完全学会，这还需要我的不断学习呢！";
}

else{
    STR="对不起，我还不知道你在说什么呢~";
}



        map.put("content", STR);
        map.put("time", formatter.format(date));
        return map;
    }


}
