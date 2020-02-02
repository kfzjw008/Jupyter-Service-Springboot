package com.jishe.jupyter.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @program: jupyter
 * @description: 排行榜视图实体类
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-16 14:55
 **/
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ranklist_integral")
public class ranklist_integral {
    @Id  //这里与表对应的实体类不同，只写@Id 注解即可
    @Column(name = "nickname")
    String nickname;
    @Column(name = "sum")
    int sum;


}
