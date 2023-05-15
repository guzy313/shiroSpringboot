package com.my.shirospringboot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Gzy
 * @version 1.0
 * @Description 自定义系统初始化参数
 * @date create on 2023/5/14
 */
@Component
public class CustomInitSystemParams {
    /**
     * 系统名称
     */
    @Value("${systemName}")
    public String systemName;


    public void init(HttpServletRequest request) {
        request.setAttribute("systemName",systemName);
        System.out.println("自定义系统参数初始化成功..");
    }



}
