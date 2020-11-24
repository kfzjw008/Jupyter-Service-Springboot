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
    private Counter modifyUser;
    private Counter Verifytoken;

    @PostConstruct
    private void init() {
        AddIntergal = registry.counter("app_requests_method_count", "method", "AddIntergalController.core");
        UserIntergal = registry.counter("app_requests_method_count", "method", "UserIntergalController.core");
        modifyUser = registry.counter("app_requests_method_count", "method", "modifyUserController.core");
        Verifytoken = registry.counter("app_requests_method_count", "method", "VerifytokenController.core");
    }

    @GetMapping("/AddIntergal")
    public Map AddIntergal(int count, String name, String token, String openid) {
        //用户申请积分接口
        try {
            AddIntergal.increment();

        } catch (Exception e) {
            return Map.of("Result", "Failed");
        }
        return Map.of("Result", WechatUserService.AddIntergal(count, name, token, openid));
    }

    @GetMapping("/UserIntergal")
    public Map UserIntergal(String token, String openid) {
        //返回用户当日积分信息
        try {
            UserIntergal.increment();
        } catch (Exception e) {
            return Map.of("Result", "Failed");
        }
        return Map.of("Result", WechatUserService.UserIntergal(token, openid));
    }
    @GetMapping("/alljf")
    public Map alljf(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("jf", WechatUserService.alljf(request));
    }


    @GetMapping("/modifyuser")
    public Map modifyUser(String school, String schoolid, String name, String openid, String token) {
        //用户资料修改接口
        try {
            modifyUser.increment();
        } catch (Exception e) {
            return Map.of("Result", "Failed");
        }
        return Map.of("Result", WechatUserService.modifyUser(school, schoolid, name, openid, token));
    }

    @GetMapping("/feedback")
    public Map feedback(String name, String title, String content, String tel, int questionnumber) {
        return Map.of("Result", WechatUserService.feedback(name, title, content, tel, questionnumber));
    }
    @GetMapping("/allfeedback")
    public Map allfeedback(@RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("Result", WechatUserService.allfeedback(request));
    }
    @GetMapping("/alluser")
    public Map allu(@RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("Result", WechatUserService.all(request));
    }
    @GetMapping("/insertboard")
    public Map insertboard(String content, String title) {
        return WechatUserService.insertboard(content, title);
    }

    @GetMapping("/board")
    public Map CorrectRatequestion(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("Board", WechatUserService.board(request));
    }

    @GetMapping("/admin")
    public boolean admin(String name, String password) {
        if (name.equals("admin") && password.equals("kfzjw0000")) {
            return true;
        } else return false;
    }

    @GetMapping("/Verifytoken")
    public Map VerifyJWT(String token) {
        try {
            Verifytoken.increment();
        } catch (Exception e) {
            return Map.of("result", "Failed");
        }
        return Map.of("result", QuestionService.VerifyJWT(token));
    }

}
