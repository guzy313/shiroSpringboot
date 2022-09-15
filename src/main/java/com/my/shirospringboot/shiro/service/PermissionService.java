package com.my.shirospringboot.shiro.service;

import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.shiro.vo.PermissionVo;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 权限接口
 */
public interface PermissionService {

    /**
     * @Description 查询权限分页列表
     * @param permissionVo
     * @param rows
     * @param page
     * @return
     */
    List<ShPermission> findPermissionList(PermissionVo permissionVo, Integer rows, Integer page);

    /**
     * @Description 查询分页前总数
     * @param permissionVo
     * @return
     * @throws Exception
     */
    Long countPermissionList(PermissionVo permissionVo) throws Exception;

    /**
     * @Description 查询一个权限对象 byId
     * @param id
     * @return
     * @throws Exception
     */
    ShPermission findPermissionById(String id) throws Exception;

    /**
     * 通过父ID去查子权限列表
     * @param parentId
     * @return
     * @throws Exception
     */
    List<ShPermission> findPermissionListByParentId(String parentId) throws Exception;

    /**
     * 通过 角色查询角色拥有的权限列表
     * @param roleId
     * @return
     * @throws Exception
     */
    List<ShPermission> findRoleHasPermissions(String roleId) throws Exception;

    boolean saveOrUpdatePermission(ShPermission shPermission) throws Exception;

    boolean deletePermission(ShPermission shPermission) throws Exception;


}
