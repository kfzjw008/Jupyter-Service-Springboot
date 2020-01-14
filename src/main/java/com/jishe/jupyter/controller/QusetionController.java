package com.jishe.jupyter.controller;

import com.jishe.jupyter.service.QuestionService;
import com.jishe.jupyter.service.WechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private QuestionService QuestionService;

    @PostMapping("/ModuleExercises")
    public Map ModuleExercises(String token) {
        //此处已经实现模块练习模块搜索
        return Map.of("Module", QuestionService.GetClassification(token));
    }

    //此处实现模块练习试题搜索，基于页码和模块编号
    @PostMapping("/ModuleExercisesDetails")
    public Map ModuleExercisesDetails(String token, int module, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);

        return Map.of("ModuleExercisesDetails", QuestionService.GetModuleExercises(token, module, request));
    }

    @PostMapping("/GetQuestion")
    public Map GetQuestion(String token, int id) {
        //此处已经实现模块练习模块搜索
        return Map.of("Question_Result", QuestionService.GetQuestion(token, id));
    }

    @PostMapping("/RandomGetQuestion")
    public Map RandomGetQuestion(String token) {
        //此处已经实现模块练习模块搜索
        int id = 1 + (int) (Math.random() * (QuestionService.GetQuestionCount(token)));
        if (id == 0) {
            return Map.of("Question_Result", "null");
        } else {
            return Map.of("Question_Result", QuestionService.GetQuestion(token, id));
        }

    }

    @PostMapping("/RecommendedExercises")
    public Map RecommendedExercises(String token) {
        //此处实现随机推荐练习模块
        return Map.of("SAO", 1);
    }

    @PostMapping("/Search")
    public Map Find(String token, String word, @RequestParam(value = "page", defaultValue = "1") Integer page,
                    @RequestParam(value = "size", defaultValue = "10") Integer size ) {
        //此处实现试题搜索模块
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("SearchResult", QuestionService.GetQuestionSearch(token,word,request));
    }

}
