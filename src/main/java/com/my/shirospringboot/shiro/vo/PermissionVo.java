package com.my.shirospringboot.shiro.vo;

import com.my.shirospringboot.pojo.ShPermission;

/**
 * @author Gzy
 * @version 1.0
 * @Description 权限视图对象
 */
public class PermissionVo extends ShPermission {
    private String roleId;
    private PermissionVo children;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public PermissionVo getChildren() {
        return children;
    }

    public void setChildren(PermissionVo children) {
        this.children = children;
    }
}
