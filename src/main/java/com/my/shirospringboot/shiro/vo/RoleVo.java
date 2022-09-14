package com.my.shirospringboot.shiro.vo;

import com.my.shirospringboot.pojo.ShRoles;

/**
 * @author Gzy
 * @version 1.0
 * @Description 角色视图对象
 */
public class RoleVo extends ShRoles {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
