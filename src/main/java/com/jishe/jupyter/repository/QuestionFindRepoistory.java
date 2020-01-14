package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Questions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @program: jupyter
 * @description: 根据ID值查询指定的试题
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 14:30
 **/
public interface QuestionFindRepoistory extends CustomizedRepoistory<Questions, String> {
    @Query("SELECT h FROM Questions h WHERE h.id=:id ")
    Questions find(@Param("id") int id);
}
