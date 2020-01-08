package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.WechatUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserfindRepository extends CustomizedRepoistory <WechatUser, String> {

    @Query("SELECT h FROM WechatUser h WHERE h.OpenId=:id ")
    WechatUser find(@Param("id") String openid);
}

