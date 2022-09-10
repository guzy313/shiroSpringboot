package com.my.shirospringboot.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author guzy
 * @version 1.0
 * @description
 */
@TableName("sh_permission")
public class ShPermission {
    //主键
    private String id;
    //父ID
    private String parentId;
    //权限名称
    private String permissionName;
    //资源路径
    private String path;
    //资源标签
    private String label;
    //图标
    private String icon;
    //是否子节点
    private String isLeaf;
    //资源类型
    private String type;
    //排序
    private Integer sort;
    //描述
    private String description;
    //系统code
    private String systemCode;
    //是否系统根节点
    private String isSystemRoot;
    //是否有效
    private String enableFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getIsSystemRoot() {
        return isSystemRoot;
    }

    public void setIsSystemRoot(String isSystemRoot) {
        this.isSystemRoot = isSystemRoot;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
