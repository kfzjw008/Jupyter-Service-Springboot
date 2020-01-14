package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.WechatUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CustomizedRepoistory<WechatUser, String> {
//指定学生全部资料

    @Query("SELECT h FROM WechatUser h ")
    List<WechatUser> list(@Param("OpenId") String OpenId);
}

