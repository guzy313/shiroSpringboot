package com.my.shirospringboot.shiro.service;

import com.my.shirospringboot.pojo.ShPermission;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 权限接口
 */
public interface PermissionService {

    List<ShPermission> findPermissionList(ShPermission shPermission,Integer rows,Integer page);

}
