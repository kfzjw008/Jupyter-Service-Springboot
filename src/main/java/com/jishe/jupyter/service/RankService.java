package com.jishe.jupyter.service;

import com.jishe.jupyter.entity.Questions;
import com.jishe.jupyter.entity.WechatUser;
import com.jishe.jupyter.entity.ranklist_allquestion;
import com.jishe.jupyter.entity.ranklist_correctrate;
import com.jishe.jupyter.repository.RankList_AllQuestion_Repoistory;
import com.jishe.jupyter.repository.RankList_CurrentQuestion_Repoistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jishe.jupyter.repository.RankList_CurrentQuestion_Repoistory;

import java.util.List;
import java.util.Map;

/**
 * @program: jupyter
 * @description: 排行榜服务层，主要提供排行榜的服务生产。
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-16 15:05
 **/
@Service
@Transactional
public class RankService {
    @Autowired
    private RankList_AllQuestion_Repoistory rankList_allQuestion_repoistory;
    @Autowired
    private RankList_CurrentQuestion_Repoistory RankList_CurrentQuestion_Repoistory;

    /**
     * * @name: 总练习数量排行
     *
     * @description: 微信用户登录服务模块，该方法用于实现用户登录。
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-16 22:35
     **/
    public Page<ranklist_allquestion> allquestions(Pageable page) {
        return rankList_allQuestion_repoistory.findAll(page);
    }

    public Page<ranklist_correctrate> CurrentQuestion(Pageable page) {
        return RankList_CurrentQuestion_Repoistory.findAll(page);
    }

}
