package com.my.shirospringboot.shiro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author Gzy
 * @version 1.0
 * @Description: jwt配置文件
 */
@ConfigurationProperties(prefix = "my.shirospringboot.shiro.jwt")
public class JwtProperties implements Serializable {
    //签名密码
    private String base64EncodeSecretKey;


    public String getBase64EncodeSecretKey() {
        return base64EncodeSecretKey;
    }

    public void setBase64EncodeSecretKey(String base64EncodeSecretKey) {
        this.base64EncodeSecretKey = base64EncodeSecretKey;
    }
}
