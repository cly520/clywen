package com.example.demo.cy.ly.service;

import com.example.demo.cy.ly.dao.PageDao;
import com.example.demo.cy.ly.dao.QuestionDao;
import com.example.demo.cy.ly.javaBean.Question;

import java.util.List;

public interface QuestionService {

    PageDao findAll(Integer page, Integer size, String search);

    void insert(Question question);

    PageDao findAllByUserId(String accountId, Integer page, Integer size);

    QuestionDao findById(Integer id);

    void update(Question question);

    void addViewCount(Integer id);

    List<QuestionDao> selectTags(QuestionDao questionDao);
}
