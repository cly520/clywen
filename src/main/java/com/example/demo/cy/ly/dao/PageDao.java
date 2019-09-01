package com.example.demo.cy.ly.dao;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PageDao<T> implements Serializable{
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private Integer totalPage;
    private List<Integer> pageList = new ArrayList<>();

    public void setPagination(Integer count, Integer page, Integer size) {
        if(page < 1){
            page = 1;
        }
        if(count % size != 0){
            totalPage = (count/size)+1;
        }else {
            totalPage = count/size;
        }
        if(page > totalPage){
            page = totalPage;
        }
        this.page = page;
        pageList.add(page);
        for (int i = 1; i<=3;i++){
            if(page - i >0){
                pageList.add(0,page-i);
            }
            if(page+i <= totalPage){
                pageList.add(page+i);
            }
        }
//        是否展示到达上一页按钮
        if(page == 1){
            showFirstPage = false;
            showPrevious = false;
        }else {
            showFirstPage = true;
            showPrevious = true;
        }
//        是否展示到达下一页按钮
        if(page == totalPage){
            showNext = false;
            showEndPage = false;
        }else {
            showNext = true;
            showEndPage = true;
        }

    }


}
