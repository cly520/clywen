package com.example.demo.cy.ly.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultDao implements Serializable {
    private Integer success;
    private String message;
    private String url;

    public ResultDao(Integer success, String message, String url) {
        this.success = success;
        this.message = message;
        this.url = url;
    }

    public ResultDao(Integer success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResultDao() {
    }
}
