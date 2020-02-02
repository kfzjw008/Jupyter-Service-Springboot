package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Board;
import com.jishe.jupyter.entity.Questions;
import com.jishe.jupyter.entity.WechatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @program: jupyter
 * @description: 依照分类查询试题
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 11:58
 **/
public interface BoardRepository extends CustomizedRepoistory<Board, String> {

    Page<Board> findAll(Pageable pageable);
    @Query("SELECT h FROM Board h ")
    List<Board> list(@Param("id") int id);
}
