package com.my.shirospringboot.shiro.core.adapter;

import com.my.shirospringboot.mapper.ShUsersMapper;
import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.pojo.ShUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author guzy
 * @version 1.0
 * @description
 */
@Repository
public interface ShUserAdapter {

    /**
     * @Description 通过 登录名 查询 用户列表
     * @param loginName
     * @return
     */
    List<ShUsers> findUserByLoginName(String loginName);


    /**
     * @Description 查询所有权限对象列表
     * @param
     * @return
     */
    List<ShPermission> findAllPermissions();

    /**
     * @Description 通过 用户id 查询权限对象列表
     * @param userId
     * @return
     */
    List<ShPermission> findPermissionsByUserId(String userId);

    /**
     * @Description 通过 用户id 查询角色对象列表
     * @param userId
     * @return
     */
    List<ShRoles> findRolesByUserId(String userId);

}
