package com.my.shirospringboot.shiro.vo;

import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShRoles;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 角色视图对象
 */
public class RoleVo extends ShRoles {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 角色拥有的权限列表
     */
    private List<ShPermission> permissionList;
    /**
     * 角色拥有的权限id字符串 格式: id1,id2,...
     */
    private String hasPermissionIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ShPermission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<ShPermission> permissionList) {
        this.permissionList = permissionList;
    }
}
