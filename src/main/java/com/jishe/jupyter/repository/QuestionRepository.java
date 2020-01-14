package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Questions;
import com.jishe.jupyter.entity.WechatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @program: jupyter
 * @description: 依照分类查询试题
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 11:58
 **/
public interface QuestionRepository extends CustomizedRepoistory <Questions, String>   {
    @Query("SELECT h FROM Questions h WHERE h.question_classification.id=:id ")
  //  Questions  find(@Param("id") int id, Pageable pageable);
    Page<Questions> findAll(@Param("id") int id, Pageable pageable);
}
