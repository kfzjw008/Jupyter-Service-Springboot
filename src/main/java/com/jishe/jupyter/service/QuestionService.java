package com.jishe.jupyter.service;

import com.jishe.jupyter.component.JWT;
import com.jishe.jupyter.entity.*;
import com.jishe.jupyter.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jishe.jupyter.repository.QuestionFindRepoistory;
import com.jishe.jupyter.repository.QuestionCountRepoistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: jupyter
 * @description: 主要用于知识问答模块的操作，此模块均需要用户登录
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-14 10:22
 **/
@Service
@Transactional
public class QuestionService {
    @Autowired
    private ClassificationRepoistory ClassificationRepoistory;
    @Autowired
    private QuestionRepository QuestionRepository;
    @Autowired
    private QuestionFindRepoistory QuestionFindRepoistory;
    @Autowired
    private QuestionCountRepoistory QuestionCountRepoistory;
    @Autowired
    private QuestionSearchRepository QuestionSearchRepository;
    @Autowired
    private QuestionRecordRepoistory QuestionRecordRepoistory;
    @Autowired
    private UserfindRepository userfindRepository;

    @Autowired
    private ranklist_tz_Repository ranklist_tz_Repository;

    /**
     * @return :按照页数依次返回的试题详情。
     * @name: 获取试题分类模块
     * @description: 用于获取所有试题的分类详情。
     * @input: 用户的token值
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-14 22:35
     **/
    public List<question_classification> GetClassification(String token) {
        if (!VerifyJWT(token)) return null;
        return ClassificationRepoistory.list();
    }

    /**
     * @name: 获取试题分类的所有试题
     * @description: 微信用户登录服务模块，该方法用于实现用户登录。
     * @input: WechatUser对象，一般只包括code
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-08 22:35
     **/
    public Page<Questions> GetModuleExercises(String token, int id, Pageable page) {
        if (!VerifyJWT(token)) return null;
        Page<Questions> QuestionsPage = QuestionRepository.findAll(id, page);
        return QuestionsPage;
    }

    /**
     * @return :id对应的试题
     * @name: 获取指定的试题
     * @description: 根据ID值获取指定的试题服务。
     * @input: token和id
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-14 14:35
     **/
    public Questions GetQuestion(String token, int id) {
        if (!VerifyJWT(token)) return null;
        return QuestionFindRepoistory.find(id);
    }

    /**
     * @name: 获取所有题目总数
     * @description: 获取题库中所有题目的总共数量。
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-14 14:35
     **/
    public int GetQuestionCount(String token) {
        if (!VerifyJWT(token)) return 0;
        System.out.println(QuestionCountRepoistory.counts());
        return QuestionCountRepoistory.counts();
    }

    /**
     * @name: 模糊查询试题
     * @description: 对题库进行模糊查询。
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-14 19:35
     **/
    public Page<Questions> GetQuestionSearch(String token, String word, Pageable page) {

        if (!VerifyJWT(token)) return null;
        System.out.println(QuestionCountRepoistory.counts());
        Page<Questions> QuestionsSearchPage = QuestionSearchRepository.findAllBy("%" + word + "%", page);
        return QuestionsSearchPage;
    }

    /**
     * @name: 提交试题记录
     * @description: 提交用户的试题记录。
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-15 11:35
     **/
    public int PutPrcacticeRecord(String token, int id, String Source, String answer, String OpenId) {
        if (!VerifyJWT(token)) return 0;
        UserRecord UserRecords = new UserRecord();
        UserRecords.setQuestions(QuestionFindRepoistory.find(id));
        UserRecords.setQuestionSource(Source);
        UserRecords.setUserAnswer(answer);
        UserRecords.setWechatUser(userfindRepository.find(OpenId));
        if (QuestionFindRepoistory.find(id).getCorrect_Answer().equals(answer)) {
            UserRecords.setT_OR_F(true);
        } else {
            UserRecords.setT_OR_F(false);
        }
        QuestionRecordRepoistory.save(UserRecords);
        QuestionRecordRepoistory.refresh(UserRecords);
        return 1;
    }

    public Map ranklist_tz(String openid, int count, String nickname) {
        WechatUser user = userfindRepository.find(openid);
        if (count <= 0) {
            count = 0;
        }
        if (ranklist_tz_Repository.findByOpenid(openid) != null) {
            ranklist_tz r = ranklist_tz_Repository.findByOpenid(openid);
            if (count > r.getCount()) {
                r.setCount(count);
                r.setNickname(nickname);
                ranklist_tz_Repository.save(r);
                return Map.of("result", r);
            }
        } else {
            ranklist_tz r = new ranklist_tz();
            r.setCount(count);
            r.setNickname(user.getNickname());
            r.setOpenid(user.getOpenId());
            ranklist_tz_Repository.save(r);
            ranklist_tz_Repository.refresh(r);
            return Map.of("result", r);
        }
        return Map.of("result", 1);
    }


    public String insertQuestion(String content, String a, String b, String c, String d, String current, String analysis, int question_classification_id, int difficulty, String image) {
        Questions questions = new Questions();
        if (content != null) questions.setQuestionBody(content);
        if (a != null) questions.setA(a);
        if (b != null) questions.setB(b);
        if (c != null) questions.setC(c);
        if (d != null) questions.setD(d);
        if (current != null) questions.setCorrect_Answer(current);
        if (analysis != null) questions.setQuestionAnalysis(analysis);
        if (question_classification_id != 0) questions.setDifficulty(difficulty);
        if (image != null) questions.setImage(image);
        QuestionFindRepoistory.save(questions);
        QuestionFindRepoistory.refresh(questions);
        return "success";
    }

    public String updateQuestion(int id, String content, String a, String b, String c, String d, String current, String analysis, int question_classification_id, int difficulty, String image) {
        Questions questions = QuestionFindRepoistory.find(id);
        if (content != null) questions.setQuestionBody(content);
        if (b != null) questions.setB(b);
        if (c != null) questions.setC(c);
        if (current != null) questions.setCorrect_Answer(current);
        if (question_classification_id != 0) questions.setDifficulty(difficulty);
        if (a != null) questions.setA(a);
        if (analysis != null) questions.setQuestionAnalysis(analysis);
        if (d != null) questions.setD(d);
        if (image != null) questions.setImage(image);
        QuestionFindRepoistory.save(questions);
        return "success";
    }

    public static boolean VerifyJWT(String token) {
        JWT util = new JWT();
        try {
            util.parseJWT(token);
        } catch (Exception e) {
            System.out.println("抛出异常！");
            return false;
        }
        return true;
    }


}
