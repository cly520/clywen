package com.example.demo.cy.ly.service.impl;

import com.example.demo.cy.ly.dao.CommentDao;
import com.example.demo.cy.ly.enums.CommentTypeEnum;
import com.example.demo.cy.ly.enums.NotificationStatusEnum;
import com.example.demo.cy.ly.enums.NotificationTypeEnum;
import com.example.demo.cy.ly.exception.CusErrorCodes;
import com.example.demo.cy.ly.exception.CusException;
import com.example.demo.cy.ly.javaBean.*;
import com.example.demo.cy.ly.mappers.CommentMapper;
import com.example.demo.cy.ly.mappers.NotificationMapper;
import com.example.demo.cy.ly.mappers.QuestionMapper;
import com.example.demo.cy.ly.mappers.UserMapper;
import com.example.demo.cy.ly.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void insert(Comment comment, User user) {
        if(comment.getParentId()==0 || comment.getParentId() ==null){
            throw new CusException(CusErrorCodes.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CusException(CusErrorCodes.TYPE_PARAM_NOT_FOUND);
        }
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
//            回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                throw new CusException(CusErrorCodes.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if(question == null){
                throw new CusException(CusErrorCodes.QUESTION_NOT_FOUND);
            }
            Integer commentConter = dbComment.getCommentConter();
            if(commentConter == null){
                commentConter = 0;
            }
            commentConter = commentConter +1;
            dbComment.setCommentConter(commentConter);
            dbComment.setGmtModified(System.currentTimeMillis());
            commentMapper.updateByPrimaryKey(dbComment);
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModified(System.currentTimeMillis());
            commentMapper.insertSelective(comment);
            createNotify(comment, dbComment.getCommentator(), user.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT,question.getId());
        }else {
//            回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CusException(CusErrorCodes.QUESTION_NOT_FOUND);
            }
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModified(System.currentTimeMillis());
            commentMapper.insertSelective(comment);
            Integer commentCount = question.getCommentCount();
            commentCount = commentCount + 1;
            question.setCommentCount(commentCount);
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.updateByPrimaryKey(question);
            createNotify(comment,question.getCreateId(),user.getName(),question.getTitle(),NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    private void createNotify(Comment comment, Integer receiver, String notifierName, String outTitle, NotificationTypeEnum notificationTypeEnum, Integer outId) {
        if(receiver.equals(comment.getCommentator())){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationTypeEnum.getType());
        notification.setOuterId(outId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOutTitle(outTitle);
        notificationMapper.insertSelective(notification);
    }

    @Override
    public List<CommentDao> findByTypeComment(Integer id, CommentTypeEnum type) {
        CommentExample example = new CommentExample();
        example.createCriteria()
        .andParentIdEqualTo(id)
        .andTypeEqualTo(type.getType());
        example.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(example);
        if(comments.size() ==0){
            return new ArrayList<>();
        }
        Set<Integer> set = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(set);
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        List<CommentDao> commentDaos = comments.stream().map(comment -> {
            CommentDao commentDao = new CommentDao();
            commentDao.setComment(comment);
            commentDao.setUser(userMap.get(comment.getCommentator()));
            return commentDao;
        }).collect(Collectors.toList());
        return commentDaos;
    }


}
