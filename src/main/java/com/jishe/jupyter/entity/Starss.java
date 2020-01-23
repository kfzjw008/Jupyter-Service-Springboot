package com.jishe.jupyter.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
@Document(indexName="stellar_data",type="Starss")
public class Starss {
    @Id
    @Field(type = FieldType.Long,store = true,analyzer = "ik_smart")
    private int id;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String name;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String bayer;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String fransted;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String variable_star;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String hd;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String hip;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String right_ascension;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String declination;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String apparent_magnitude;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String absolute_magnitude;

    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String distance;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String classification;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String notes;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String constellation;
    @Field(type = FieldType.text,store = true,analyzer = "ik_smart")
    private String ancient_chinese_name;
}
