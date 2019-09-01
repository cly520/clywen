package com.example.demo.cy.ly.mappers;

import com.example.demo.cy.ly.dao.QuestionSearchDao;
import com.example.demo.cy.ly.javaBean.Question;

import java.util.List;

public interface QuestionOtherMapper {
     List<Question> selectTags(Question question);

    Integer countBySearch(QuestionSearchDao questionSearchDao);

    List<Question> selectBySearchWithRowbounds(QuestionSearchDao questionSearchDao);
}
