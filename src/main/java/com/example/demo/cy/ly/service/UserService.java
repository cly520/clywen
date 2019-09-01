package com.example.demo.cy.ly.service;

import com.example.demo.cy.ly.javaBean.User;

import java.util.List;

public interface UserService {
    List<User> findByToken(String token);

    User findById(Integer createId);

    void createOrUpdate(User user);
}
