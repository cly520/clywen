package com.example.demo.cy.ly.controller;

import com.example.demo.cy.ly.dao.CommentDao;
import com.example.demo.cy.ly.dao.Result;
import com.example.demo.cy.ly.enums.CommentTypeEnum;
import com.example.demo.cy.ly.exception.CusErrorCodes;
import com.example.demo.cy.ly.javaBean.Comment;
import com.example.demo.cy.ly.javaBean.User;
import com.example.demo.cy.ly.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Result post(@RequestBody Comment comment, HttpServletRequest request){
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if(user == null){
                return Result.errorOf(CusErrorCodes.NO_LOGIN);
            }else {
                if(StringUtils.isBlank(comment.getContent())){
                    return Result.errorOf(CusErrorCodes.COMMENT_CONTENT_NOT_FOUND);
                }
                comment.setCommentator(user.getId());
                commentService.insert(comment,user);
                return Result.successOf();
            }
        }

    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public Result comments(@PathVariable(value = "id") Integer id){
        List<CommentDao> commentDaos = commentService.findByTypeComment(id, CommentTypeEnum.COMMENT);
        return Result.successOf(commentDaos);
    }

}
