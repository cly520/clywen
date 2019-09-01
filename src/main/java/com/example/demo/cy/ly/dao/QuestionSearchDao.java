package com.example.demo.cy.ly.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionSearchDao implements Serializable{
    private String search;
    private Integer page;
    private Integer size;

}
