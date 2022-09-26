package com.my.shirospringboot.shiro.service;

import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.shiro.vo.RoleVo;
import com.my.shirospringboot.shiro.vo.UserVo;

import java.util.List;
import java.util.Map;

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


    /**
     * @Description: 查询用户分页列表
     * @param userVo
     * @param pageSize
     * @param pageIndex
     * @return List<Map<String,Object>>
     * @throws Exception
     */
    List<Map<String,Object>> findUserList(UserVo userVo, Integer pageSize, Integer pageIndex,String keyword) throws Exception;

    /**
     * @Description: 查询用户分页前总数
     * @param userVo
     * @return
     * @throws Exception
     */
    Long countUserList(UserVo userVo) throws Exception;

    /**
     * @Description: 新增修改用户信息
     * @param userVo
     * @return
     * @throws Exception
     */
    Boolean saveOrUpdateUser(UserVo userVo) throws Exception;

    /**
     * @Description: 删除用户
     * @param userVo
     * @return
     * @throws Exception
     */
    Boolean deleteUser(UserVo userVo) throws Exception;

    /**
     * @Description: 验证用户名唯一
     * @param userVo
     * @return
     * @throws Exception
     */
    Boolean checkLoginName(UserVo userVo) throws Exception;



}
