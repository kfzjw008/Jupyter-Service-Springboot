package com.jishe.jupyter.service;

import com.jishe.jupyter.entity.*;
import com.jishe.jupyter.repository.RankList_AllQuestion_Repoistory;
import com.jishe.jupyter.repository.RankList_CurrentQuestion_Repoistory;
import com.jishe.jupyter.repository.ranklist_tz_Repository;
import com.jishe.jupyter.repository.RankList_integral_Repoistory;
import org.apache.tomcat.util.digester.ObjectCreationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jishe.jupyter.repository.RankList_CurrentQuestion_Repoistory;

import java.util.HashMap;
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
    @Autowired
    private RankList_integral_Repoistory RankList_integral_Repoistory;
    @Autowired
    private ranklist_tz_Repository ranklist_tz_Repository;
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
    public Page<ranklist_integral> integral(Pageable page) {
        return RankList_integral_Repoistory .findAll(page);
    }
    public Page<ranklist_tz> tz(Pageable page) {
        return ranklist_tz_Repository.findByNicknameAndCountOrderByCountAtDesc(page);
    }

    public Map myrecord(String nickname){
        Map<Object,Object> m =new HashMap<>();
        m.put("tz",ranklist_tz_Repository.findByNickname(nickname));
        m.put("integral",RankList_integral_Repoistory.findByNickname(nickname));
        m.put("CurrentQuestion", RankList_CurrentQuestion_Repoistory.findByNickname(nickname));
        m.put("allQuestion", rankList_allQuestion_repoistory.findByNickname(nickname));
        return m;
    }

}
