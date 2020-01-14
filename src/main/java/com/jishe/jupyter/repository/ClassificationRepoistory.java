package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.question_classification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @program: jupyter
 * @description: 用于查询返回所有的试题分类
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 10:39
 **/
public interface ClassificationRepoistory extends CustomizedRepoistory <question_classification, String>  {

    @Query("SELECT h FROM question_classification h ")
    List<question_classification> list();
    //

}
