package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.ranklist_allquestion;
import com.jishe.jupyter.entity.ranklist_integral;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @program: jupyter
 * @description: 试题排行榜数据库接口，主要用于展示所有试题的练习排行
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-16 14:31
 **/
public interface RankList_integral_Repoistory extends CustomizedRepoistory<ranklist_integral, String>   {
    Page<ranklist_integral> findAll(Pageable pageable);

}
