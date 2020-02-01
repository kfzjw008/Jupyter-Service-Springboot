package com.jishe.jupyter;

import com.jishe.jupyter.repository.StarssRepoistory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import  com.jishe.jupyter.repository.StarssRepoistory;
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
    /*
    *1、练习积分：每日练习题目数量（含所有做题数量）每5题积分+1，每日上限50。name=练习积分,一次+1，一天50次。
    2、连对积分：挑战模式连对5题积分+10，连对10题+20，每日上限20。//前端判断连对题数，直接传回通用接口,name=连对积分
    2、签到积分：每日签到积分+3，连续签到，第二天积分比第一天+1，最高为10，每日一次。name=签到积分
    3、分享积分：分享一次+8，每日上限3次。name=分享积分
    * */
    public int Expiration_time = 36000000;//过期时间，设置为10个小时
    public static  StarssRepoistory StarssRepoistory=new StarssRepoistory();//elastic Search链接
}
