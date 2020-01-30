package com.jishe.jupyter.repository;

import com.jishe.jupyter.entity.Questions;
import com.jishe.jupyter.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @program: jupyter
 * @description: 依照分类查询试题
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 11:58
 **/
public interface TagRepository extends CustomizedRepoistory<Tag, String> {

    Tag findByName(String name);
}
