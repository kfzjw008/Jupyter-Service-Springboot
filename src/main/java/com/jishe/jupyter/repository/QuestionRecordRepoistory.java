package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.UserRecord;
import com.jishe.jupyter.entity.WechatUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @program: jupyter
 * @description: 主要用于查找用户试题记录
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-15 11:34
 **/
public interface QuestionRecordRepoistory extends CustomizedRepoistory<UserRecord, String> {
    @Query("SELECT h FROM UserRecord h ")
    List<WechatUser> list();
}
