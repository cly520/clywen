package com.example.demo.cy.ly.controller;

import com.example.demo.cy.ly.dao.NotificationDao;
import com.example.demo.cy.ly.enums.NotificationTypeEnum;
import com.example.demo.cy.ly.javaBean.User;
import com.example.demo.cy.ly.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable("id") Integer id ,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }
        NotificationDao notificationDao =notificationService.read(id,user);
        if(NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDao.getType() ||
                NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDao.getType()){
            return "redirect:/question/"+notificationDao.getOuterId();
        }else {
            return "redirect:/";
        }
    }
}
