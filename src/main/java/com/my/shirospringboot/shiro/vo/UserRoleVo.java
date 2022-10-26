package com.my.shirospringboot.shiro.vo;

/**
 * @author Gzy
 * @version 1.0
 * @Description 用户分配角色 vo
 */
public class UserRoleVo {
    private String userId;
    private String roleIds;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "UserRoleVo{" +
                "userId='" + userId + '\'' +
                ", roleIds='" + roleIds + '\'' +
                '}';
    }
}
