package com.my.shirospringboot.shiro.constant;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
public class CacheConstant extends SuperConstant{

    public final static String GROUP_CAS = "group_shiro:";

    public final static String ROLE_KEY = GROUP_CAS + "role_key";

    public final static String PERMISSION_KEY = GROUP_CAS + "permissions_key";

    public final static String PERMISSION_KEY_IDS = GROUP_CAS + "permission_key_ids";

    public final static String FIND_USER_BY_LOGINNAME = GROUP_CAS + "findUserByLoginName";

}
