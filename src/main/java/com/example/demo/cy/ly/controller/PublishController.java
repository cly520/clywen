package com.example.demo.cy.ly.controller;

import com.example.demo.cy.ly.cache.TagCache;
import com.example.demo.cy.ly.dao.QuestionDao;
import com.example.demo.cy.ly.javaBean.Question;
import com.example.demo.cy.ly.javaBean.User;
import com.example.demo.cy.ly.service.QuestionService;
import com.example.demo.cy.ly.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping("/publish/{id}")
    public String question(@PathVariable("id") Integer id, Model model) {
        QuestionDao questionDao = questionService.findById(id);
        model.addAttribute("title", questionDao.getTitle());
        model.addAttribute("titledesc", questionDao.getTitleDesc());
        model.addAttribute("tag", questionDao.getTag());
        model.addAttribute("id", questionDao.getId());
        model.addAttribute("newTags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("newTags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title, @RequestParam("titledesc") String titledesc,
                            @RequestParam("tag") String tag, HttpServletRequest request, @RequestParam("id") Integer id, Model model) {
        model.addAttribute("title", title);
        model.addAttribute("titledesc", titledesc);
        model.addAttribute("tag", tag);
        model.addAttribute("id", id);
        if (title == null || title == "") {
            model.addAttribute("error", "问题不能为空");
            return "publish";
        }
        if (titledesc == null || titledesc == "") {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        String tagsEquals = TagCache.tagsEquals(tag);
        if (StringUtils.isNoneBlank(tagsEquals)) {
            model.addAttribute("error", "输入非法标签:" + tagsEquals);
            return "publish";
        }

        Question question = new Question();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        question.setTitle(title);
        question.setTitleDesc(titledesc);
        question.setTag(tag);
        question.setCreateId(user.getId());
        question.setAccountId(user.getAccountId());
        if (id != null) {
            question.setId(id);
            questionService.update(question);
        } else {
            questionService.insert(question);
        }
        return "redirect:/";
    }
}
