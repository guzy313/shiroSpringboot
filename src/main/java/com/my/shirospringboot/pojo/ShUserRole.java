package com.my.shirospringboot.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author Gzy
 * @version 1.0
 * @Description 用户角色关联表
 */
@TableName("sh_user_role")
public class ShUserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String enableFlag;
    private String userId;
    private String roleId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
