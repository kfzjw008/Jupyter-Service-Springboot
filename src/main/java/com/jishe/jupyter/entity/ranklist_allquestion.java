package com.jishe.jupyter.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
@Table(name = "ranklist_allquestion")
public class ranklist_allquestion {
    @Id  //这里与表对应的实体类不同，只写@Id 注解即可
    @Column(name = "nickname")
    String nickname;
    @Column(name = "Allquestion")
    int Allquestion;


}
