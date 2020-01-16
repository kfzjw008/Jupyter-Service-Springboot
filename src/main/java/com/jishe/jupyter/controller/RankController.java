package com.jishe.jupyter.controller;

import com.jishe.jupyter.service.QuestionService;
import com.jishe.jupyter.service.RankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/AllQuestion")
    public Map Allquestion( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("AllQuestion", RankService.allquestions(request));
    }
    @PostMapping("/CorrectRate")
    public Map CorrectRatequestion( @RequestParam(value = "page", defaultValue = "1") Integer page,
                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);
        return Map.of("CorrectRate", RankService.CurrentQuestion(request));
    }

}
