package com.my.shirospringboot.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author Gzy
 * @version 1.0
 * @Description 角色-权限对应关系表
 */
@TableName("sh_role_permission")
public class ShRolePermission {
    private String id;
    private String enableFlag;
    private String roleId;
    private String permissionId;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
}
