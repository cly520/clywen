package com.example.demo.cy.ly.controller;

import com.example.demo.cy.ly.dao.ResultDao;
import com.example.demo.cy.ly.provider.UCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private UCloudProvider uCloudProvider;

    @RequestMapping("/upload")
    public ResultDao upload(HttpServletRequest request) {
        ResultDao resultDao = new ResultDao();
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("editormd-image-file");
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            resultDao.setSuccess(1);
            resultDao.setUrl(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultDao;
    }
}
