package com.jishe.jupyter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: jupyter
 * @description: 天象日历实体类。
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-27 20:55
 **/
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int year;
    private int month;
    private int day;
    private String week;
    private String time;
    private String event;


}
