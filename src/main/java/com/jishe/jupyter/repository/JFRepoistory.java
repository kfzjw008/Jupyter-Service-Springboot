package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Calendar;
import com.jishe.jupyter.entity.Integral;
import com.jishe.jupyter.entity.WechatUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @program: jupyter
 * @description: 根据ID值查询指定的试题
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 14:30
 **/
public interface JFRepoistory extends CustomizedRepoistory<Integral, String> {

    @Query("SELECT h FROM Integral h ")
    List<Integral> list();
    @Query(value = "SELECT sum(h.count) FROM Integral h where wechat_user_id=:user")
    Integer allscore(int user);
   @Query(value="SELECT sum(h.count) FROM Integral h where wechat_user_id=?1 and insertTime between ?2 and ?3")
   Integer  todayallscore(int wechatUser,LocalDateTime a,LocalDateTime b);
    @Query(value="SELECT sum(h.count) FROM Integral h where wechat_user_id=?1 and name=?2 and insertTime between ?3 and ?4")
    Integer  todayscore(int wechatUser,String name,LocalDateTime a,LocalDateTime b);
}
