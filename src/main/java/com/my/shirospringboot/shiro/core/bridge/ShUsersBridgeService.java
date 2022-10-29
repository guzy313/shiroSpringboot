package com.my.shirospringboot.shiro.core.bridge;

import com.my.shirospringboot.mapper.ShUsersMapper;
import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.shiro.core.base.ShiroUser;
import org.apache.shiro.authz.AuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author guzy
 * @version 1.0
 * @description 用户信息桥接（后期做缓存）
 */
public interface ShUsersBridgeService {

    /**
     * @Description 通过 登录名 查询 用户信息
     * @param loginName
     * @return 用户信息
     */
    ShUsers findUserByLoginName(String loginName);


    /**
     * @Description 通过用户ID查询角色名称列表
     * @param userId
     * @return
     */
    List<String> findRoleNamesByUserId(String userId);


    /**
     * @Description 通过 缓存名称(角色常量+当前会话ID),用户ID 查询 角色标签列表
     * @param key,userId
     * @return Labels
     */
    List<String> findRoleLabelsByUserId(String key,String userId);


    /**
     * @Description 通过用户ID查询 权限id集合
     * @param userId
     * @return 权限id集合
     */
    List<String> findPermissionIdsByUserId(String userId);

    /**
     * @Description 通过用户ID查询 权限名称集合
     * @param userId
     * @return
     */
    List<String> findPermissionNamesByUserId(String userId);


    /**
     * @Description 查询所有权限集合 -一般为管理员权限
     * @param
     * @return
     */
    List<ShPermission> findAllPermissions();

    /**
     * @Description 通过用户ID查询 权限集合 userName用来判断是否管理员权限
     * @param userId
     * @return
     */
    List<ShPermission> findPermissionsByUserId(String userName,String userId);

    /**
     * @Description 通过 缓存名称(角色常量+当前会话ID), 用户ID查询 权限标签集合
     * @param key,userId
     * @return
     */
    List<String> findPermissionLabelsByUserId(String key,String userId);


    /**
     * @Description 授权方法 (查询用户的角色、权限并存入)
     * @param shiroUser
     * @return
     */
    AuthorizationInfo getAuthorizationInfo(ShiroUser shiroUser);

    /**
     * @Description 将登录信息(角色、权限标签)写入缓存（查询方法内有判断是否存在缓存逻辑）
     * @param shiroUser
     */
    void loadUserAuthorityToCache(ShiroUser shiroUser);

}
