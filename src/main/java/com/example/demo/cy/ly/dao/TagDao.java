package com.example.demo.cy.ly.dao;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TagDao implements Serializable{
    private String categoryName;
    private List<String> tags;

}
