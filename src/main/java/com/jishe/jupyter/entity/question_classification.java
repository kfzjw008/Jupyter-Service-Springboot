package com.jishe.jupyter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: jupyter
 * @description: 试题分类类别实体类，一个分类可以有多个试题，一个试题只对应一个类
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-12 20:54
 **/
@Getter
@Setter
@NoArgsConstructor
@Entity
public class question_classification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String Title;//分类名称
    private String Description;//分类描述

    public question_classification(int ide, String title, String description) {
        id = ide;
        Title = title;
        Description = description;
    }

    @OneToMany(mappedBy = "question_classification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Questions> QuestionList;
    @Column(columnDefinition = "DATETIME NOT NULL " + "DEFAULT CURRENT_TIMESTAMP ON UPDATE " + "CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime updateTime;//资料最后更新时间
    @Column(columnDefinition = "TIMESTAMP NOT NULL " + "DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;//首次添加时间

}
