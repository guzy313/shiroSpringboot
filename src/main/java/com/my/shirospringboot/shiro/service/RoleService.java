package com.my.shirospringboot.shiro.service;

import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.shiro.vo.RoleVo;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 角色接口
 */
public interface RoleService {

    List<ShRoles> findRolesList(RoleVo roleVo,Integer rows,Integer page) throws Exception;

    Long countRolesList(RoleVo roleVo) throws Exception;


}
