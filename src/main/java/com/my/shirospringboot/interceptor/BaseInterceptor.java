package com.my.shirospringboot.interceptor;

import com.my.shirospringboot.shiro.utils.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BaseInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        System.out.println(uri);
        try {
            Subject subject = SecurityUtils.getSubject();
            boolean authenticated = subject.isAuthenticated();//是否登录
            if(authenticated){
                return true;
            }else{
                System.out.println("======================未登录成功============================================");
                response.sendRedirect("/account/login");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
