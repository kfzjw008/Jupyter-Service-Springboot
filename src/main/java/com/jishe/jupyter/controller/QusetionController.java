package com.jishe.jupyter.controller;

import com.jishe.jupyter.service.QuestionService;
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
    @Autowired
    MeterRegistry registry;
    private Counter ModuleExercises;
    private Counter ModuleExercisesDetails;
    private Counter GetQuestion;
    private Counter RandomGetQuestion;
    private Counter RecommendedExercises;
    private Counter Search;
    private Counter PutRecords;
    private Counter insert;
    private Counter update;
    private Counter tz;
    @PostConstruct
    private void init() {
        ModuleExercises = registry.counter("app_requests_method_count", "method", "ModuleExercisesController.core");
        ModuleExercisesDetails = registry.counter("app_requests_method_count", "method", "ModuleExercisesDetailsController.core");
        GetQuestion = registry.counter("app_requests_method_count", "method", "GetQuestionController.core");
        RandomGetQuestion = registry.counter("app_requests_method_count", "method", "RandomGetQuestionController.core");
        RecommendedExercises = registry.counter("app_requests_method_count", "method", "RecommendedExercisesController.core");
        Search = registry.counter("app_requests_method_count", "method", "SearchController.core");
        PutRecords = registry.counter("app_requests_method_count", "method", "PutRecordsController.core");
        insert = registry.counter("app_requests_method_count", "method", "insertController.core");
        update = registry.counter("app_requests_method_count", "method", "updateController.core");
       tz = registry.counter("app_requests_method_count", "method", "tzController.core");
    }

    @PostMapping("/ModuleExercises")
    public Map ModuleExercises(String token) {
        //此处已经实现模块练习模块搜索
        try {
            ModuleExercises.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        return Map.of("Module", QuestionService.GetClassification(token));
    }

    //此处实现模块练习试题搜索，基于页码和模块编号
    @PostMapping("/ModuleExercisesDetails")
    public Map ModuleExercisesDetails(String token, int module, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            ModuleExercisesDetails.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("ModuleExercisesDetails", QuestionService.GetModuleExercises(token, module, request));
    }

    @PostMapping("/GetQuestion")
    public Map GetQuestion(String token, int id) {
        //此处已经实现指定题目搜索
        try {
            GetQuestion.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        return Map.of("Question_Result", QuestionService.GetQuestion(token, id));
    }

    @PostMapping("/RandomGetQuestion")
    public Map RandomGetQuestion(String token) {
        //此处已经实现随机获得试题
        try {
            RandomGetQuestion.increment();
        } catch (Exception e) {
            return (Map) e;
        }
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
        try {
            RecommendedExercises.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        return Map.of("SAO", 1);
    }

    @PostMapping("/Search")
    public Map Find(String token, String word, @RequestParam(value = "page", defaultValue = "1") Integer page,
                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        //此处已经实现试题搜索模块
        try {
            Search.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("SearchResult", QuestionService.GetQuestionSearch(token, word, request));
    }

    @PostMapping("/PutRecords")
    public Map PutRecords(String token, int id, String source, String answer, String openid) {
        //此处实现推送练习记录模块
        try {
            PutRecords.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        return Map.of("PutResult", QuestionService.PutPrcacticeRecord(token, id, source, answer, openid));
    }


    @PostMapping("/update")
    public Map updatequestion(int id, String content, String a, String b, String c, String d, String current, String analysis, int question_classification_id, int difficulty, String image) {
        try {
            update.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        String s = QuestionService.updateQuestion(id, content, a, b, c, d, current, analysis, question_classification_id, difficulty, image);
        return Map.of("Result", s);
    }

    @PostMapping("/insert")
    public Map insertquestion(int id, String content, String a, String b, String c, String d, String current, String analysis, int question_classification_id, int difficulty, String image) {
        try {
            insert.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        String s = QuestionService.insertQuestion(content, a, b, c, d, current, analysis, question_classification_id, difficulty, image);
        return Map.of("Result", s);
    }

    @PostMapping("/updaterecord")
    public Map  ranklist_tz(String openid,int count,String nickname){
        try {
            tz.increment();
        } catch (Exception e) {
            return (Map) e;
        }
        return QuestionService.ranklist_tz(openid, count, nickname);
    }

}
