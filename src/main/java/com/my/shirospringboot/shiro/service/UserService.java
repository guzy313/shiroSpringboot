package com.my.shirospringboot.shiro.service;

/**
 * @author Gzy
 * @version 1.0
 * @Description 用户接口
 */
public interface UserService {

    /**
     * 保存新密码
     * @return  boolean
     * @throws Exception
     */
    Boolean saveNewPassword(String oldPassword,String newPassword) throws Exception;



}
