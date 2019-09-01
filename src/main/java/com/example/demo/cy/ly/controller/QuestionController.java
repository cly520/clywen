package com.example.demo.cy.ly.controller;

import com.example.demo.cy.ly.dao.CommentDao;
import com.example.demo.cy.ly.dao.QuestionDao;
import com.example.demo.cy.ly.enums.CommentTypeEnum;
import com.example.demo.cy.ly.service.CommentService;
import com.example.demo.cy.ly.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id, Model model){
        QuestionDao questionDao = questionService.findById(id);
         List<QuestionDao> questionTags = questionService.selectTags(questionDao);
         if(questionTags == null){
             return "question";
         }
        model.addAttribute("questionTags",questionTags);
        questionService.addViewCount(id);
        model.addAttribute("question",questionDao);
        List<CommentDao> commentDao = commentService.findByTypeComment(id, CommentTypeEnum.QUESTION);
        if(commentDao == null ){
            return "question";
        }
        model.addAttribute("commentDao",commentDao);
        return "question";
    }
}
