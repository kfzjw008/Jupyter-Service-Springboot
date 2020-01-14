package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @program: jupyter
 * @description: 主要用于试题的模糊查询
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 19:13
 **/
public interface QuestionSearchRepository extends CustomizedRepoistory<Questions, String>  {

    @Query(value = "select t from Questions t where t.a like %?1% or t.b like  %?1% or t.c like  %?1% or t.d like  %?1% or t.questionBody like  %?1% or t.questionAnalysis like  %?1% ")
    Page<Questions> findAllBy(String Word, Pageable pageable);
}
