package com.jishe.jupyter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private String QuestionBody;//题目题干
    private String A;
    private String B;
    private String C;
    private String D;
    private String Correct_Answer;//正确答案
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name="question_classification_id")//设置在表中的关联字段(外键)
    private question_classification question_classification;//试题分类，推荐基准
    private String QuestionAnalysis;//试题解析
    private int Difficulty;//难易度，1为容易，5为困难
    private String Image;//题目图片，选填
    @Column(columnDefinition = "DATETIME NOT NULL " + "DEFAULT CURRENT_TIMESTAMP ON UPDATE " + "CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime updateTime;//资料最后更新时间
    @Column(columnDefinition = "TIMESTAMP NOT NULL " + "DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;//首次添加题目时间
}
