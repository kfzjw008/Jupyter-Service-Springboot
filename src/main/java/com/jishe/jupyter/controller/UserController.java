package com.jishe.jupyter.controller;

import com.jishe.jupyter.entity.WechatUser;
import com.jishe.jupyter.service.RankService;
import com.jishe.jupyter.service.WechatUserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @program: jupyter
 * @description: 排行榜的controller层
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-16 15:16
 **/
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private RankService RankService;
    @Autowired
    private WechatUserService WechatUserService;
    @Autowired
    MeterRegistry registry;
    private Counter AddIntergal;
    private Counter UserIntergal;

    @PostConstruct
    private void init() {
        AddIntergal = registry.counter("app_requests_method_count", "method", "AddIntergalController.core");
        UserIntergal = registry.counter("app_requests_method_count", "method", "UserIntergalController.core");
    }

    @PostMapping("/AddIntergal")
    public Map AddIntergal(int count, String name , String token,String openid ) {
        //用户申请积分接口
        try {
            AddIntergal.increment();

        } catch (Exception e) {
            return Map.of("Result", "Failed");
        }
        return Map.of("Result", WechatUserService.AddIntergal(count,name,token,openid));
    }
    @PostMapping("/UserIntergal")
    public Map UserIntergal(String token,String openid) {
        //返回用户当日积分信息
        try {
            UserIntergal.increment();
        } catch (Exception e) {
            return Map.of("Result", "Failed");
        }
        return Map.of("Result",  WechatUserService.UserIntergal(token,openid));
    }

}
