package com.example.demo.cy.ly.dao;

import lombok.Data;

@Data
public class NotificationDao {
    private Integer id;
    private Integer status;
    private Long gmtCreate;
    private Integer notifier;
    private String notifierName;
    private Integer outerId;
    private String outTitle;
    private String typeName;
    private Integer type;
}
