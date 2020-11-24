package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Feedback;
import com.jishe.jupyter.entity.Integral;
import com.jishe.jupyter.entity.WechatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: jupyter
 * @description: 根据ID值查询指定的试题
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 14:30
 **/
public interface FeedBackRepoistory extends CustomizedRepoistory<Feedback, String> {
    @Query("SELECT h FROM Feedback h ")
    List<Feedback> list(@Param("id") int OpenId);
    @Query("SELECT h FROM Feedback h ")
    Page<Feedback> allfeedback(Pageable page);
}
