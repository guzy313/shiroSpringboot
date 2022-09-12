package com.my.shirospringboot.shiro.service;

import com.my.shirospringboot.shiro.vo.LoginVo;

import java.util.Map;

/**
 * @author Gzy
 * @version 1.0
 * @Description 登录接口
 */
public interface LoginService {

    public Map<String,String> route(LoginVo loginVo) ;

}
