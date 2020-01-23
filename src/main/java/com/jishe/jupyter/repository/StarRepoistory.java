package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Stars;
import com.jishe.jupyter.entity.UserRecord;
import com.jishe.jupyter.entity.WechatUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @program: jupyter
 * @description: 查询恒星数据
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-21 16:53
 **/
public interface StarRepoistory extends CustomizedRepoistory<Stars, String>{
    @Query("SELECT h FROM Stars  h   WHERE h.id=:id")
    Stars find(@Param("id") int id);

}
