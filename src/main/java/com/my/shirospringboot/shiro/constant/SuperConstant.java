package com.my.shirospringboot.shiro.constant;

/**
 * @author guzy
 * @version 1.0
 * @description
 */
public class SuperConstant {

    /**
     * 管理员登录名
     */
    public static final String ADMINISTRATOR_NAME = "admin";

    /**
     * 常量是
     */
    public static final String YES = "YES";

    /**
     * 常量否
     */
    public static final String NO = "NO";

    /**
     * 加密方式-散列算法1
     */
    public static final String SHA1 = "SHA-1";

    /**
     * 加密次数
     */
    public static final Integer HashIterations = 33;

    /**
     * permission 权限最高级父id
     */
    public static final String ROOT_PARENT_ID = "0";

    /**
     * 系统permission菜单权限(不允许被删除)
     */
    public static final String[] SYSTEM_PERMISSION_IDS = {"001","001001","001002","001003","001004"};
}