package com.my.shirospringboot.shiro.utils;


import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.shiro.core.base.ShiroUser;
import com.my.shirospringboot.utils.BeanUtils;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
public class SecurityUtils extends org.apache.shiro.SecurityUtils {

    /**
     * @Description 获取当前登录用户的ShiroUser对象(继承了用户类,用来封装数据)
     * @return ShiroUser
     */
    public static ShiroUser getShiroUser(){
        Subject subject = SecurityUtils.getSubject();
        ShiroUser shiroUser = (ShiroUser)subject.getPrincipal();
        return shiroUser;
    }

    /**
     * @Description 获取当前登录用户类对象 ShUsers (用来进行持久化操作)
     * @return ShUsers
     */
    public static ShUsers getShUsers(){
        Subject subject = SecurityUtils.getSubject();
        ShiroUser shiroUser = (ShiroUser)subject.getPrincipal();
        ShUsers shUsers = (ShUsers)BeanUtils.toBean(shiroUser, ShUsers.class);
        return shUsers;
    }

    /**
     * @Description: 获取当前登录用户的权限对象列表
     * @return
     */
    public static List<ShPermission> getLoginUserPermissions(){
        Subject subject = SecurityUtils.getSubject();
        ShiroUser shiroUser = (ShiroUser)subject.getPrincipal();
        List<ShPermission> permissionList = shiroUser.getPermissionList();
        return permissionList;
    }



}
