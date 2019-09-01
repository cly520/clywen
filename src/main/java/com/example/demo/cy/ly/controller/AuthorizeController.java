package com.example.demo.cy.ly.controller;

import com.example.demo.cy.ly.dao.AccessTokenDao;
import com.example.demo.cy.ly.dao.GithubUser;
import com.example.demo.cy.ly.javaBean.User;
import com.example.demo.cy.ly.provider.GithubProvider;
import com.example.demo.cy.ly.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.url}")
    private String redirectUrl;

    @Autowired
    private UserService userService;

    @GetMapping("callback")
    public String callback(@RequestParam(name = "code")String code, @RequestParam(name = "state") String state, HttpServletResponse response){
        AccessTokenDao accessTokenDao = new AccessTokenDao();
        accessTokenDao.setRedirect_uri(redirectUrl);
        accessTokenDao.setClient_id(clientId);
        accessTokenDao.setCode(code);
        accessTokenDao.setClient_secret(clientSecret);
        accessTokenDao.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDao);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser != null &&githubUser.getId() != null ){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setName(githubUser.getName());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);
            Cookie cookie = new Cookie("token", token);
            response.addCookie(cookie);
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
