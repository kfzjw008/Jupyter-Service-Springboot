package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Board;
import com.jishe.jupyter.entity.ranklist_tz;
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
public interface ranklist_tz_Repository extends CustomizedRepoistory<ranklist_tz, String> {
    @Query("select e from ranklist_tz e ORDER BY e.count DESC")
    Page<ranklist_tz> findByNicknameAndCountOrderByCountAtDesc(Pageable pageable);
   ranklist_tz findByOpenid(String openid);
    @Query("SELECT h FROM ranklist_tz h ")
    List<Board> list(@Param("id") int id);

    ranklist_tz  findByNickname(String nickname);
}
