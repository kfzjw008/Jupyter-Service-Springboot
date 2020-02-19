package com.jishe.jupyter.controller;

import com.jishe.jupyter.repository.RankList_AllQuestion_Repoistory;
import com.jishe.jupyter.service.RankService;
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
@RequestMapping("/api/rank")
public class RankController {

    @Autowired
    private RankService RankService;
    @Autowired
    MeterRegistry registry;
    private Counter AllQuestion;
    private Counter CorrectRate;
    private Counter integral;
    private Counter tz;
    private Counter myrecord;
    @PostConstruct
    private void init() {
        AllQuestion = registry.counter("app_requests_method_count", "method", "AllQuestionRankController.core");
        CorrectRate = registry.counter("app_requests_method_count", "method", "CorrectRateRankController.core");
        tz = registry.counter("app_requests_method_count", "method", "tzRankController.core");
        integral = registry.counter("app_requests_method_count", "method", "integralRankController.core");
        myrecord = registry.counter("app_requests_method_count", "method", "myrecordController.core");
    }

    @GetMapping("/AllQuestion")
    public Map Allquestion(@RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);
        try {
            AllQuestion.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        return Map.of("AllQuestion", RankService.allquestions(request));
    }

    @GetMapping("/CorrectRate")
    public Map CorrectRatequestion(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            CorrectRate.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("CorrectRate", RankService.CurrentQuestion(request));
    }

    @GetMapping("/integralRate")
    public Map integral(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            integral.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("integralRate", RankService.integral(request));
    }
    @GetMapping("/tzRate")
    public Map tz(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            tz.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("tzRate", RankService.tz(request));
    }

    @GetMapping("/myrecord")
    public Map myrecord(String nick){
        try {
            myrecord.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        return RankService.myrecord(nick);
    }
}
