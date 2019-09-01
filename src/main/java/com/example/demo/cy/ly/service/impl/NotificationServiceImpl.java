package com.example.demo.cy.ly.service.impl;


import com.example.demo.cy.ly.dao.NotificationDao;
import com.example.demo.cy.ly.dao.PageDao;
import com.example.demo.cy.ly.enums.NotificationStatusEnum;
import com.example.demo.cy.ly.enums.NotificationTypeEnum;
import com.example.demo.cy.ly.exception.CusErrorCodes;
import com.example.demo.cy.ly.exception.CusException;
import com.example.demo.cy.ly.javaBean.Notification;
import com.example.demo.cy.ly.javaBean.NotificationExample;
import com.example.demo.cy.ly.javaBean.User;
import com.example.demo.cy.ly.mappers.NotificationMapper;
import com.example.demo.cy.ly.service.NotificationService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;


    @Override
    public PageDao findAllByUserId(Integer id, Integer page, Integer size) {
        PageDao<NotificationDao> pageDao = new PageDao();
        List<NotificationDao> notificationDaos = new ArrayList<>();
        Integer offSet = (page - 1) * size;
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offSet, size));
        if(notifications.size() == 0){
            return pageDao;
        }
        for (Notification notification : notifications) {
            NotificationDao notificationDao = new NotificationDao();
            BeanUtils.copyProperties(notification,notificationDao);
            notificationDao.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDaos.add(notificationDao);
        }
        pageDao.setData(notificationDaos);
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id);
        notificationExample.setOrderByClause("gmt_create desc");
        Integer count =(int)notificationMapper.countByExample(notificationExample);
        if(count ==0) {
            count = 1;
        }
        pageDao.setPagination(count, page, size);
        return pageDao;
    }

    @Override
    public Long unreadCount(Integer id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        long count = notificationMapper.countByExample(notificationExample);
        return count;
    }

    @Override
    public NotificationDao read(Integer id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CusException(CusErrorCodes.NOTIFICATION_NOT_FOUND);
        }
        if(!notification.getReceiver().equals(user.getId())){
            throw new CusException(CusErrorCodes.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDao notificationDao = new NotificationDao();
        BeanUtils.copyProperties(notification,notificationDao);
        notificationDao.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDao;
    }
}
