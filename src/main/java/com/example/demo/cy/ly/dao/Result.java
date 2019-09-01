package com.example.demo.cy.ly.dao;

import com.example.demo.cy.ly.exception.CusErrorCodes;
import com.example.demo.cy.ly.exception.CusException;

import java.io.Serializable;

public class Result implements Serializable{
    private Integer success;
    private String message;
    private Object data;

    public Result(Integer success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Result() {
    }

    public Result(Integer success, String message) {
        this.success = success;
        this.message = message;
    }
    public static Result errorOf(Integer code, String messgae){
        Result result = new Result();
        result.setMessage(messgae);
        result.setSuccess(code);
        return result;
    }
    public static Result errorOf(CusErrorCodes errorCode){
        return errorOf(errorCode.getCode(),errorCode.getMessage());
    }
    public static Result successOf(){
        Result result = new Result();
        result.setMessage("操作成功");
        result.setSuccess(200);
        return result;
    }
    public static Result successOf( Object data){
        Result result = new Result();
        result.setMessage("操作成功");
        result.setSuccess(200);
        result.setData(data);
        return result;
    }

    public static Result errorOf(CusException e) {
        return Result.errorOf(e.getCode(),e.getMessage());
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
