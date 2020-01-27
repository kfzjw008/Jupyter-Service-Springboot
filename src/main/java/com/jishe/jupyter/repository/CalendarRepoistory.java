package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Calendar;
import com.jishe.jupyter.entity.Questions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @program: jupyter
 * @description: 根据ID值查询指定的试题
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 14:30
 **/
public interface CalendarRepoistory extends CustomizedRepoistory<Calendar, String> {

    List<Calendar>  findByYearAndMonthAndDay(int year,int month,int day);
}
