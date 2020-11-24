package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Feedback;
import com.jishe.jupyter.entity.WechatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CustomizedRepoistory<WechatUser, String> {

    @Query("SELECT h FROM WechatUser h ")
    List<WechatUser> list(@Param("OpenId") String OpenId);
    @Query("SELECT h FROM WechatUser h ")
    Page<WechatUser> all(Pageable page);
}