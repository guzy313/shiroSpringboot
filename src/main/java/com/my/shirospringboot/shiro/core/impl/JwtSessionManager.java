package com.my.shirospringboot.shiro.core.impl;

import com.my.shirospringboot.utils.StringUtils;
import io.jsonwebtoken.Claims;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author Gzy
 * @version 1.0
 * @Description: Jwt 会话管理器
 */
public class JwtSessionManager extends MySessionManager{
    //通过构造函数从spring容器中获取该组件
    private JwtTokenManager jwtTokenManager;

    public JwtSessionManager() {
    }


    public JwtSessionManager(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String jwtToken = request.getParameter("JwtToken");
        if(StringUtils.isNotEmpty(jwtToken)){
            //解析令牌
            Claims claims = jwtTokenManager.decodeToken(jwtToken);
            //从令牌中获取sessionId
            return claims.getId();
        }else{
            return super.getSessionId(request, response);
        }

    }
}
