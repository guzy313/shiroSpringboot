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
    public static final Integer HASH_ITERATIONS = 33;

    /**
     * permission 权限最高级父id
     */
    public static final String ROOT_PARENT_ID = "0";

    /**
     * 系统permission菜单权限(不允许被删除)
     */
    public static final String[] SYSTEM_PERMISSION_IDS = {"001","001001","001002","001003","001004"};

    /**
     *  同一用户一定时间内允许登录失败次数 - 密码匹配器中使用
     */
    public static final Integer USER_ALLOW_FAIL_COUNT = 5;

    /**
     * 同一用户登录失败禁止重试时间(ms) - 密码匹配器中使用
     */
    public static final Integer USER_LOGIN_FAIL_TIME = 1800000;

    /**
     * 允许账号 并发会话 个数限制
     */
    public static final Integer ACTIVE_NUM_LIMIT = 3;

}