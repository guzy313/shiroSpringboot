package com.my.shirospringboot.shiro.service.impl;

import com.my.shirospringboot.mapper.ShUsersMapper;
import com.my.shirospringboot.shiro.core.base.ShiroUser;
import com.my.shirospringboot.shiro.service.UserService;
import com.my.shirospringboot.shiro.web.account.LoginAction;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Gzy
 * @version 1.0
 * @Description 用户接口实现
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private ShUsersMapper shUsersMapper;


    @Override
    public Boolean saveNewPassword(String oldPassword,String newPassword) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        ShiroUser principal = (ShiroUser)subject.getPrincipal();
        if(principal.getPassword().equals(oldPassword)){
            shUsersMapper.updatePassword(principal.getId(),newPassword);
            return true;
        }else{
            log.error("原密码不正确");
            return false;
        }
    }
}
