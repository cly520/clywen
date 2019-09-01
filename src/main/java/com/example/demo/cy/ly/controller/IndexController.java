package com.example.demo.cy.ly.controller;

import com.example.demo.cy.ly.dao.PageDao;
import com.example.demo.cy.ly.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

     @GetMapping("/")
     public String index( Model model ,
                         @RequestParam(value ="page",defaultValue = "1") Integer page,
                         @RequestParam(value ="size",defaultValue = "9") Integer size,
                         @RequestParam(value ="search",required = false) String search)
     {
         PageDao pageDao = questionService.findAll(page,size,search);
         model.addAttribute("pageDao",pageDao);
         model.addAttribute("search",search);
         return "index";
    }
}
