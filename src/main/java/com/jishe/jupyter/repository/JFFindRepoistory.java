package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Integral;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @program: jupyter
 * @description: 根据ID值查询指定的试题
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 14:30
 **/
public interface JFFindRepoistory extends CustomizedRepoistory<Integral, String> {

    List<Integral>findByNameAndInsertTimeBetween(String name, LocalDateTime insertTime, LocalDateTime insertTime2) ;

    @Query(value="SELECT h.count FROM Integral h where wechat_user_id=?1 and name=?2 and insertTime between ?3 and ?4")
    List<Integral>findByNameAndInsertTimeBetween2(int wechatUser,String name,LocalDateTime a,LocalDateTime b) ;


}
