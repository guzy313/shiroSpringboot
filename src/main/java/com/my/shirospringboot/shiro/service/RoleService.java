package com.my.shirospringboot.shiro.service;

import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.shiro.vo.RoleVo;
import com.my.shirospringboot.shiro.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * @author Gzy
 * @version 1.0
 * @Description 角色接口
 */
public interface RoleService {
    /**
     * @Description: 查询角色分页列表
     * @param roleVo
     * @param rows
     * @param page
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> findRolesList(RoleVo roleVo, Integer pageSize, Integer pageIndex,String keyword) throws Exception;

    /**
     * @Description: 查询角色分页前总数
     * @param roleVo
     * @return
     * @throws Exception
     */
    Long countRolesList(RoleVo roleVo) throws Exception;

    /**
     * @Description: 查询用户已分配角色ID列表
     * @param userId
     * @return
     * @throws Exception
     */
    List<String> findRolesList(String userId) throws Exception;

    /**
     * 保存角色
     * @param roleVo
     * @return
     * @throws Exception
     */
    boolean saveOrUpdateRole(RoleVo roleVo) throws Exception;


    boolean saveRoleHasPermissions(String roleId,List<String> permissionIdsList) throws Exception;


    /**
     * @Description: 通过ID、标志更新角色是否启用
     * @param ids
     * @param flag
     * @return
     * @throws Exception
     */
    boolean updateByIds(List<String> ids,String flag) throws Exception;

    /**
     * @Description: 删除用户
     * @param roleVo
     * @return
     * @throws Exception
     */
    Boolean deleteRole(RoleVo roleVo) throws Exception;


}
