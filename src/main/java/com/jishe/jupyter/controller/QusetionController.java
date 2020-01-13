package com.jishe.jupyter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @program: jupyter
 * @description: 试题业务层
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-12 11:01
 **/
@Slf4j
@RestController
@RequestMapping("/api/question")
public class QusetionController {


    @GetMapping("/ModuleExercises")
    public Map ModuleExercises(String token) {
        //此处实现模块练习模块搜索
        return Map.of("SAO", 1);
    }

    @GetMapping("/ModuleExercisesDetails")
    public Map ModuleExercisesDetails(String token, int module, int page) {
        //此处实现模块练习试题搜索，基于页码和模块编号
        return Map.of("SAO", 1);
    }

    @GetMapping("/RecommendedExercises")
    public Map RecommendedExercises(String token) {
        //此处实现随机推荐练习模块
        return Map.of("SAO", 1);
    }

    @GetMapping("/Find")
    public Map Find(String token, String word) {
        //此处实现试题搜索模块
        return Map.of("SAO", 1);
    }


}
