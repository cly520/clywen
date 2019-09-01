package com.example.demo.cy.ly.service.impl;

import com.example.demo.cy.ly.dao.PageDao;
import com.example.demo.cy.ly.dao.QuestionDao;
import com.example.demo.cy.ly.dao.QuestionSearchDao;
import com.example.demo.cy.ly.exception.CusErrorCodes;
import com.example.demo.cy.ly.exception.CusException;
import com.example.demo.cy.ly.javaBean.Question;
import com.example.demo.cy.ly.javaBean.QuestionExample;
import com.example.demo.cy.ly.javaBean.User;
import com.example.demo.cy.ly.mappers.QuestionMapper;
import com.example.demo.cy.ly.mappers.QuestionOtherMapper;
import com.example.demo.cy.ly.service.QuestionService;
import com.example.demo.cy.ly.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionOtherMapper questionOtherMapper;

    @Override
    public PageDao findAll(Integer page, Integer size, String search) {
        QuestionSearchDao questionSearchDao = new QuestionSearchDao();
        PageDao<QuestionDao> pageDao = new PageDao();
        if(StringUtils.isNotBlank(search)){
            String[] tags = search.split(" ");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }
        questionSearchDao.setSearch(search);
        Integer count = questionOtherMapper.countBySearch(questionSearchDao);
        if(count == 0 ){
            count = 1;
        }
        pageDao.setPagination(count,page,size);
        Integer offSet = (page - 1) * size;
        questionSearchDao.setPage(offSet);
        questionSearchDao.setSize(size);
        List<Question> questionList = questionOtherMapper.selectBySearchWithRowbounds(questionSearchDao);
        List<QuestionDao> questionDaoList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userService.findById(question.getCreateId());
            QuestionDao questionDao = new QuestionDao();
            BeanUtils.copyProperties(question,questionDao);
            questionDao.setUser(user);
            questionDaoList.add(questionDao);
        }
        pageDao.setData(questionDaoList);
        return pageDao;
    }

    @Override
    public void insert(Question question) {
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.insertSelective(question);
    }

    @Override
    public PageDao findAllByUserId(String accountId, Integer page, Integer size) {
        Integer offSet = (page - 1) * size;
        QuestionExample example = new QuestionExample();
        example.createCriteria()
        .andAccountIdEqualTo(accountId);
        example.setOrderByClause("gmt_create desc");
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offSet,size));
        PageDao<QuestionDao> pageDao = new PageDao();
        List<QuestionDao> questionDaoList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userService.findById(question.getCreateId());
            QuestionDao questionDao = new QuestionDao();
            BeanUtils.copyProperties(question,questionDao);
            questionDao.setUser(user);
            questionDaoList.add(questionDao);
        }
        pageDao.setData(questionDaoList);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
        .andAccountIdEqualTo(accountId);
        questionExample.setOrderByClause("gmt_create desc");
        Integer count =(int)questionMapper.countByExample(questionExample);
        if(count ==0) {
          count = 1;
        }
        pageDao.setPagination(count, page, size);

        return pageDao;
    }

    @Override
    public QuestionDao findById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CusException(CusErrorCodes.QUESTION_NOT_FOUND);
        }
        QuestionDao questionDao = new QuestionDao();
        BeanUtils.copyProperties(question,questionDao);
        User user = userService.findById(question.getCreateId());
        questionDao.setUser(user);
        return questionDao;
    }

    @Override
    public void update(Question question) {
        question.setGmtModified(System.currentTimeMillis());
        questionMapper.updateByPrimaryKeySelective(question);
    }

    @Override
    public void addViewCount(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        Integer viewCount = question.getViewCount();
        viewCount = viewCount + 1;
        question.setViewCount(viewCount);
        int update = questionMapper.updateByPrimaryKey(question);
        if(update != 1){
            throw new CusException(CusErrorCodes.QUESTION_NOT_FOUND);
        }
    }

    @Override
    public List<QuestionDao> selectTags(QuestionDao queyDto) {
        if(StringUtils.isBlank(queyDto.getTag())){
            return new ArrayList<>();
        }
        String[] tags = queyDto.getTag().split(",");
        String newTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queyDto.getId());
        question.setTag(newTag);
        List<Question> questions = questionOtherMapper.selectTags(question);
        List<QuestionDao> questionDaos = questions.stream().map(p -> {
            QuestionDao questionDao = new QuestionDao();
            BeanUtils.copyProperties(p,questionDao);
            return questionDao;
        }).collect(Collectors.toList());
        return questionDaos;
    }
}
