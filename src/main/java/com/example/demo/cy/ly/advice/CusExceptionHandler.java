package com.example.demo.cy.ly.advice;

import com.alibaba.fastjson.JSON;
import com.example.demo.cy.ly.dao.Result;
import com.example.demo.cy.ly.exception.CusErrorCodes;
import com.example.demo.cy.ly.exception.CusException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CusExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model, HttpServletRequest request, HttpServletResponse response) {

        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            Result result;
            if(e instanceof CusException){
                result = Result.errorOf((CusException) e);
            }else {
                result =  Result.errorOf(CusErrorCodes.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(result));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        }
        if(e instanceof CusException){
            model.addAttribute("message",e.getMessage());
        }else {
            model.addAttribute("message", CusErrorCodes.SYS_ERROR);
        }
        return new ModelAndView("error");
    }

}
