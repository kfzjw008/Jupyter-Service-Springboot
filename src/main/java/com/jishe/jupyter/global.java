package com.jishe.jupyter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @program: jupyter
 * @description: 全局变量的直接配置与设置
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-11 11:18
 **/
@Getter
@Setter
@NoArgsConstructor
public class global {
    public int Expiration_time = 36000000;//过期时间，设置为10个小时
}
