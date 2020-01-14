package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Questions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @program: jupyter
 * @description: 用于返回题库中总题目数量
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 15:02
 **/
public interface QuestionCountRepoistory extends CustomizedRepoistory<Questions, String> {
    @Query("SELECT count(h) FROM Questions h  ")
    int counts();
}
