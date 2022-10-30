package com.my.shirospringboot.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author guzy
 * @version 1.0
 * @description 角色表
 */
@TableName("sh_roles")
public class ShRoles implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private String id;
    //角色名称
    private String roleName;
    //角色标志
    private String label;
    //角色描述
    private String description;
    //排序
    private Integer sort;
    //是否有效
    private String enableFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    @Override
    public String toString() {
        return "ShRoles{" +
                "id='" + id + '\'' +
                ", roleName='" + roleName + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", sort=" + sort +
                ", enableFlag='" + enableFlag + '\'' +
                '}';
    }
}
