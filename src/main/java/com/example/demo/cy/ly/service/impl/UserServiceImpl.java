package com.example.demo.cy.ly.service.impl;

import com.example.demo.cy.ly.javaBean.User;
import com.example.demo.cy.ly.javaBean.UserExample;
import com.example.demo.cy.ly.mappers.UserMapper;
import com.example.demo.cy.ly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findByToken(String token) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andTokenEqualTo(token);
        List<User> users = userMapper.selectByExample(example);
        return users;
    }



    @Override
    public User findById(Integer createId) {
        return userMapper.selectByPrimaryKey(createId);
    }

    @Override
    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insertSelective(user);
        } else {
            User dbUser = users.get(0);
            if (dbUser != null) {
                dbUser.setGmtModified(user.getGmtCreate());
                dbUser.setToken(user.getToken());
                dbUser.setAvatarUrl(user.getAvatarUrl());
                dbUser.setName(user.getName());
                userMapper.updateByPrimaryKey(dbUser);
            }
        }
    }
}
