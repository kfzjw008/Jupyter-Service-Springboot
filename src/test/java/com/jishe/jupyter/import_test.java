package com.jishe.jupyter;

import com.jishe.jupyter.entity.Questions;
import com.jishe.jupyter.entity.Tag;
import com.jishe.jupyter.repository.QuestionFindRepoistory;
import com.jishe.jupyter.repository.QuestionRepository;
import com.jishe.jupyter.repository.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * @program: jupyter
 * @description: 测试jwt加密token的情况
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-11 00:18
 **/
@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest
@Service
@Transactional
public class import_test {
    @Autowired
    private QuestionFindRepoistory QuestionFindRepoistory;
    @Autowired
    private TagRepository tagRepository;

    @Test
    public void contextLoads() throws IOException {
        int i = 56;
        for(i=1;i<=1010;i++){
            Questions q = QuestionFindRepoistory.find(i);
            String text = q.getQuestionBody() + q.getA() + q.getB() + q.getC() + q.getD();
            String Alltext = "";
            StringReader sr = new StringReader(text);
            IKSegmenter ik = new IKSegmenter(sr, true);
            Lexeme lex = null;
            int count;
            while ((lex = ik.next()) != null) {
                Tag tag = new Tag();
                if (tagRepository.findByName(lex.getLexemeText()) == null) {
                    tag.setName(lex.getLexemeText());
                    tag.setCount(1);
                    tagRepository.save(tag);
                    tagRepository.refresh(tag);
                } else {
                    tag = tagRepository.findByName(lex.getLexemeText());
                    count = tag.getCount();
                    count = count + 1;
                    tag.setCount(count);
                    tagRepository.save(tag);
                }

                Alltext = Alltext + lex.getLexemeText() + "|";

            }
            System.out.print("第" + i + "题：---------------计算完成");

        }
        System.out.println("---------------全部完成");
    }
}
