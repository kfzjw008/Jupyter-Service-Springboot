package com.jishe.jupyter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: jupyter
 * @description: 恒星实体类，主要用于数据导入
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-21 16:48
 **/
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Stars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String bayer;
    private String fransted;
    private String variable_star;
    private String hd;
    private String hip;
    private String right_ascension;
    private String declination;
    private String apparent_magnitude;
    private String absolute_magnitude;
    private String distance;
    private String classification;
    private String notes;
    private String constellation;
    private String ancient_chinese_name;
}
