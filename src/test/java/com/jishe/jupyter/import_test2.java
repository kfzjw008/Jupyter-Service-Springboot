package com.jishe.jupyter;

import com.jishe.jupyter.entity.Questions;
import com.jishe.jupyter.entity.Tag;
import com.jishe.jupyter.repository.QuestionFindRepoistory;
import com.jishe.jupyter.repository.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
public class import_test2 {
    public double getDegree(ArrayList<String> str1, ArrayList<String> str23){
        Map<String, int[]> Spacemoment = new HashMap<String, int[]>();
        int[] Spacearr = null;
        int size1=str1.size();
        String[] strass = (String[])str1.toArray(new String[size1]);
        for(int i=0; i<strass.length; ++i){
            if(Spacemoment.containsKey(strass[i]))
                ++(Spacemoment.get(strass[i])[0]);
            else{
                Spacearr = new int[2];
                Spacearr[0] = 1;
                Spacemoment.put(strass[i], Spacearr);
            }
        }
        int size2=str23.size();
        String[] strs2 = (String[])str23.toArray(new String[size2]);
        for(int i=0; i<strs2.length; ++i){
            if(Spacemoment.containsKey(strs2[i]))++(Spacemoment.get(strs2[i])[1]);
            else{
                Spacearr = new int[2];
                Spacearr[1] = 1;
                Spacemoment.put(strs2[i], Spacearr);
            }
        }
        double vector1mod = 0.00,vector2mod = 0.00,vectorProduct = 0.00;
        Iterator iter = Spacemoment.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            Spacearr = (int[])entry.getValue();
            vector1mod += Spacearr[0]*Spacearr[0];
            vector2mod += Spacearr[1]*Spacearr[1];
            vectorProduct += Spacearr[0]*Spacearr[1];
        }
        vector1mod = Math.sqrt(vector1mod);
        vector2mod = Math.sqrt(vector2mod);
        return (vectorProduct/(vector1mod*vector2mod));
    }

}








