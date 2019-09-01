package com.example.demo.cy.ly.exception;

public enum CusErrorCodes implements CusErrorCode {
    QUESTION_NOT_FOUND(2001,"你找的问题不存在，要不换一个试试吧"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"您未登录，请先登录再进行操作"),
    SYS_ERROR(2004,"服务器异常，请稍后再试试！！"),
    TYPE_PARAM_NOT_FOUND(2005,"评论类型不存在或错误"),
    COMMENT_NOT_FOUND(2006,"回复的评论不存在，要不换一个再试试吧"),
    COMMENT_CONTENT_NOT_FOUND(2007,"评论内容不能为空"),
    REQUEST_NOT_FOUND(2008,"你这个请求错了，换一个再试试吧"),
    SERVICE_NOT_FOUND(2009,"服务器升温了 ，等降温以后再试试吧"),
    READ_NOTIFICATION_FAIL(2010,"兄弟你到了不该来的地方"),
    NOTIFICATION_NOT_FOUND(2011,"该消息不存在"),
    FILE_UPLOAD_FAIL(2012,"图片上传失败"),
    ;

    private String message;
    private Integer code;

     public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    CusErrorCodes(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

}