package com.my.shirospringboot.shiro.service.impl;

import com.my.shirospringboot.shiro.core.base.ShiroUser;
import com.my.shirospringboot.shiro.core.base.SimpleToken;
import com.my.shirospringboot.shiro.core.bridge.ShUsersBridgeService;
import com.my.shirospringboot.shiro.service.LoginService;
import com.my.shirospringboot.shiro.utils.SecurityUtils;
import com.my.shirospringboot.shiro.vo.LoginVo;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Service
@Log4j2
public class LoginServiceImpl implements LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    private ShUsersBridgeService shUsersBridgeService;

    @Override
    public Map<String, String> route(LoginVo loginVo) {
        Map<String,String> map = new HashMap<>();
        try {
            SimpleToken token =
                    new SimpleToken(null, loginVo.getLoginName(), loginVo.getPassword());
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            //缓存用户的权限信息进入缓存
            this.loadAuthorityToCache(token);
        }catch (UnknownAccountException e){
           log.error("登录异常",e);
           throw new UnknownAccountException(e);
        }catch (IncorrectCredentialsException e){
            log.error("登录异常",e);
            throw new IncorrectCredentialsException(e);
        }
        //返回sessionId
        map.put("authorizationKey",SecurityUtils.getSubject().getSession().getId().toString());
        return map;
    }

    /**
     * @Description: 登录成功后缓存用户的权限信息进入缓存
     * @param token
     */
    private void loadAuthorityToCache(SimpleToken token){
        //登录成功后缓存用户的权限信息进入缓存
        ShiroUser shiroUser = SecurityUtils.getShiroUser();
        shUsersBridgeService.loadUserAuthorityToCache(shiroUser);
    }


}
