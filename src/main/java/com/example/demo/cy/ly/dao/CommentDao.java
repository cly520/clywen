package com.example.demo.cy.ly.dao;

import com.example.demo.cy.ly.javaBean.Comment;
import com.example.demo.cy.ly.javaBean.User;

import java.io.Serializable;

public class CommentDao implements Serializable {
    private Comment comment;
    private User user;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
