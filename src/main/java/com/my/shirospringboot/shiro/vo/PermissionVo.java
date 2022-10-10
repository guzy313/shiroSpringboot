package com.my.shirospringboot.shiro.vo;

import com.my.shirospringboot.pojo.ShPermission;

/**
 * @author Gzy
 * @version 1.0
 * @Description 权限视图对象
 */
public class PermissionVo extends ShPermission {
    private String roleId;
    private Boolean selected;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
