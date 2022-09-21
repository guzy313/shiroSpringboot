package com.my.shirospringboot.config;

import com.my.shirospringboot.interceptor.BaseInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器注册配置
 */
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BaseInterceptor()).addPathPatterns("/**").
                excludePathPatterns("/","/account/login","/login/usersLogin","/login/usersLoginOut").
                excludePathPatterns("/static/","/templates","/templates/error","/static/js","/static/css");
    }

//    /**
//     * @Description: 过滤静态资源
//     * @param registry
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/");
//
//    }



}
