package com.my.shirospringboot.shiro.core.impl;

import com.my.shirospringboot.utils.StringUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author Gzy
 * @version 1.0
 * @Description: Jwt 会话管理器
 */
public class JwtSessionManager extends MySessionManager{
    //从请求中获得session的key
    public static final String AUTHORIZATION = "jwtToken";
    //自定义注入的资源类型名称
    public static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";


    //通过构造函数从spring容器中获取该组件
    private JwtTokenManager jwtTokenManager;

    public JwtSessionManager() {
    }


    public JwtSessionManager(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //从http头中取出jsonWebToken
        String jwtToken = WebUtils.toHttp(request).getHeader(JwtSessionManager.AUTHORIZATION);
        if(StringUtils.isNotEmpty(jwtToken)){
            //解析令牌
            Claims claims = jwtTokenManager.decodeToken(jwtToken);
            //从令牌中获取sessionId(存的时候源码中setId的key是jti)
            String sessionId = String.valueOf(claims.get("jti"));
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                    ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID,sessionId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID,Boolean.TRUE);
            return sessionId;
        }else{
            return super.getSessionId(request, response);
        }

    }
}
