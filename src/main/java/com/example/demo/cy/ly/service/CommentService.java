package com.example.demo.cy.ly.service;

import com.example.demo.cy.ly.dao.CommentDao;
import com.example.demo.cy.ly.enums.CommentTypeEnum;
import com.example.demo.cy.ly.javaBean.Comment;
import com.example.demo.cy.ly.javaBean.User;

import java.util.List;

public interface CommentService {
    void insert(Comment comment, User user);

    List<CommentDao> findByTypeComment(Integer id, CommentTypeEnum type);
}
