package com.jishe.jupyter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: jupyter
 * @description: 试题类，用于展现知识问答模块的天文试题
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-12 10:52
 **/
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "questionBody")
    private String questionBody;//题目题干
    private String a;
    private String b;
    private String c;
    private String d;
    private String Correct_Answer;//正确答案

    @OneToMany(mappedBy = "Questions", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserRecord> UserRecord;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "question_classification_id")//设置在表中的关联字段(外键)
    private question_classification question_classification;//试题分类，推荐基准
    private String questionAnalysis;//试题解析
    private int Difficulty;//难易度，1为容易，5为困难
    private String Image;//题目图片，选填
    @Column(columnDefinition = "DATETIME NOT NULL " + "DEFAULT CURRENT_TIMESTAMP ON UPDATE " + "CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime updateTime;//资料最后更新时间
    @Column(columnDefinition = "TIMESTAMP NOT NULL " + "DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;//首次添加题目时间
}
