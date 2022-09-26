package com.my.shirospringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 请求 处理器映射器 配置文件
 */
//@Configuration
public class RequestMappingHandlerAdapterConfig {

    /**
     * @Description: 处理器映射器
     * @return
     */
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(){
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        //定义消息转换器列表
        List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>();
        //添加jackson http消息转换器
        httpMessageConverters.add(new MappingJackson2HttpMessageConverter());
        //将 消息转换器列表 设置给 请求处理器映射器
        requestMappingHandlerAdapter.setMessageConverters(httpMessageConverters);

        return requestMappingHandlerAdapter;
    }

}
