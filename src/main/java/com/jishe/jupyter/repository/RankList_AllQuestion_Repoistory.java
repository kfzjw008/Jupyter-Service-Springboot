package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Questions;
import com.jishe.jupyter.entity.WechatUser;
import com.jishe.jupyter.entity.ranklist_allquestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * @program: jupyter
 * @description: 试题排行榜数据库接口，主要用于展示所有试题的练习排行
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-16 14:31
 **/
public interface RankList_AllQuestion_Repoistory extends CustomizedRepoistory<ranklist_allquestion, String>   {
    Page<ranklist_allquestion> findAll(Pageable pageable);

    ranklist_allquestion findByNickname(String nickname);
}
