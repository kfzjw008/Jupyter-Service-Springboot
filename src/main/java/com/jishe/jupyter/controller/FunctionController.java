package com.jishe.jupyter.controller;

import com.jishe.jupyter.service.FunctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @program: jupyter
 * @description: 基于实现系统功能的业务层
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-10 21:53
 **/
@Slf4j
@RestController
@RequestMapping("/api/function")
public class FunctionController {
    @Autowired
    private FunctionService FunctionService;
    @GetMapping("/SearchSAO")
    public Map getSAO(Double LON, Double LAT) {
        //此处实现观测适宜度评价计算
        return FunctionService.GetSAO(LON, LAT);
    }
    @GetMapping("/StarSearch")
    public Map test(String string,int page) throws Exception {
        //此处实现恒星查询
            return    Map.of("Result", FunctionService.Search(string,page));
    }

    @GetMapping("/EightPlanets")
    public Map GetEightPlanets() {
        //此处实现八大行星数值返回
        return Map.of("Result", FunctionService.GetEightPlanets());
    }

    @GetMapping("/SearchSunriseset")
    public Map getSunriseset(int year,int month,int day ,int zone,double lon,double lat) {
        //此处实现日出日落日期计算
        return Map.of("Result",  FunctionService.GetSunriseset(year,month,day,zone,lon,lat));
    }
    @GetMapping("/SearchSMoonPhase")
    public Map getMoonPhase(int year,int month,int day ) {
        //此处实现月相检索
        return Map.of("Result",  FunctionService.GetMoonPhase(year,month,day));
    }
    @GetMapping("/Calendar")
    public Map getCalendar(int year,int month,int day ) {
        //此处实现月相检索
        return Map.of("Result",  FunctionService.GetCalendar(year,month,day));
    }
}
