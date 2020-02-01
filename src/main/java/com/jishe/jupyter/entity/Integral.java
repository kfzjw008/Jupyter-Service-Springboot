package com.jishe.jupyter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @program: jupyter
 * @description: 积分记录实体类。
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-02-01 20:55
 **/
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Integral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int count;
    @Column(columnDefinition = "TIMESTAMP NOT NULL " + "DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;//首次登录时间
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "WechatUser_id")//设置在表中的关联字段(外键)
    private WechatUser WechatUser;//试题分类，推荐基准

}
