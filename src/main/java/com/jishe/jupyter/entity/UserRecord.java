package com.jishe.jupyter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @program: jupyter
 * @description: 用户的答题记录实体类，每次对试题的调取都需要该类
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 16:15
 **/
@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "WechatUser_id")//设置在表中的关联字段(外键)
    private WechatUser WechatUser;//试题分类，推荐基准

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "Questions_id")//设置在表中的关联字段(外键)
    private Questions Questions;//试题分类，推荐基准

    @Column(columnDefinition = "DATETIME NOT NULL " + "DEFAULT CURRENT_TIMESTAMP ON UPDATE " + "CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime updateTime;//资料最后更新时间（上次登录时间）
    @Column(columnDefinition = "TIMESTAMP NOT NULL " + "DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;//首次登录时间
    private String UserAnswer;
    private boolean T_OR_F;
    private String QuestionSource;
}
