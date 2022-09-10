package com.my.shirospringboot.shiro.core.base;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author guzy
 * @version 1.0
 * @description 自定义token
 */
public class SimpleToken extends UsernamePasswordToken {
    private String tokenType;

    private String quickPassword;


    public SimpleToken(String tokenType,String username, String password) {
        super(username, password);
        this.tokenType = tokenType;
    }

    public SimpleToken(String tokenType,String quickPassword,String username, String password) {
        super(username, password);
        this.tokenType = tokenType;
        this.quickPassword = quickPassword;
    }

}
