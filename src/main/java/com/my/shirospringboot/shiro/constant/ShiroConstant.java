package com.my.shirospringboot.shiro.constant;

/**
 * @author Gzy
 * @version 1.0
 * @Description: shiro常量类
 */
public class ShiroConstant {


    //jwtToken
    public static final String JWT_TOKEN = "jwtToken";

    //未认证提示错误代码
    public static final Integer NO_AUTH_CODE = 1;

    //未认证提示错误信息
    public static final String NO_AUTH_MESSAGE = "请先进行登录";

    //没有权限提示错误代码
    public static final Integer NO_PERMS_CODE = 2;

    //没有权限提示错误信息
    public static final String NO_PERMS_MESSAGE = "当前用户无对应权限";

    //没有角色提示错误代码
    public static final Integer NO_ROLES_CODE = 3;

    //没有角色提示错误信息
    public static final String NO_ROLES_MESSAGE = "当前用户无对应角色";

    //登录失败提示代码
    public static final Integer LOGIN_FAIL_CODE = 5;

    //登录失败提示信息
    public static final String LOGIN_FAIL_MESSAGE = "登录失败";


    //登录成功提示代码
    public static final Integer LOGIN_SUCCESS_CODE = 6;

    //登录成功提示信息
    public static final String LOGIN_SUCCESS_MESSAGE = "登录成功";

}
