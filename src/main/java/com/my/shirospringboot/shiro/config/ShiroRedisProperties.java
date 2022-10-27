package com.my.shirospringboot.shiro.config;

import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
//@Component
@ConfigurationProperties(prefix = "my.shirospringboot.shiro.redis")
public class ShiroRedisProperties implements Serializable{

    /**
     * redis连接密码
     */
    private String password;
    /**
     * redis连接地址
     */
    private String nodes;

    /**
     * 连接超时时间
     */
    private int connectTimeout;

    /**
     * 最小空闲连接数
     */
    private int minIdle;

    /**
     * 最大连接数
     */
    private int maxActive;

    /**
     * 等待数据返回超时时间
     */
    private int timeout;

    /**
     * 全局超时时间
     */
    private int globalTimeout;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getGlobalTimeout() {
        return globalTimeout;
    }

    public void setGlobalTimeout(int globalTimeout) {
        this.globalTimeout = globalTimeout;
    }
}
