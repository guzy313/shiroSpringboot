package com.my.shirospringboot.shiro.service;

import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.shiro.vo.RoleVo;

import java.util.List;

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
    List<ShRoles> findRolesList(RoleVo roleVo,Integer rows,Integer page) throws Exception;

    /**
     * @Description: 查询角色分页前总数
     * @param roleVo
     * @return
     * @throws Exception
     */
    Long countRolesList(RoleVo roleVo) throws Exception;

    boolean saveOrUpdateRole(RoleVo roleVo) throws Exception;

    /**
     * @Description: 通过ID、标志更新角色是否启用
     * @param ids
     * @param flag
     * @return
     * @throws Exception
     */
    boolean updateByIds(List<String> ids,String flag) throws Exception;

}
