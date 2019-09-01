package com.example.demo.cy.ly.service;

import com.example.demo.cy.ly.dao.NotificationDao;
import com.example.demo.cy.ly.dao.PageDao;
import com.example.demo.cy.ly.javaBean.User;

public interface NotificationService {
    PageDao findAllByUserId(Integer id, Integer page, Integer size);

    Long unreadCount(Integer id);

    NotificationDao read(Integer id, User user);
}
