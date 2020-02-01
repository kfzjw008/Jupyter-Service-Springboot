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



}
