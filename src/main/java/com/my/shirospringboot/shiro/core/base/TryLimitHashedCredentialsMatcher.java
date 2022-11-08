package com.my.shirospringboot.shiro.core.base;

import com.my.shirospringboot.shiro.constant.CacheConstant;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.utils.ShiroRedissonSerialize;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Gzy
 * @version 1.0
 * @Description: 重写密码比较器,用来限制登录的重试次数-未考虑不存在用户是否要存入缓存(防止恶意不存在账号输错挤占redis空间)
 */
@Component
public class TryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
    private  final Logger log = LoggerFactory.getLogger(this.getClass());

    //所有用户的登录失败重试次数用户key前缀
    private static final String TRY_COUNT_CACHE_MAP_NAME_PREFIX = "userTryCount:";

    //注入redisson客户端类
    private RedissonClient redissonClient;

    public TryLimitHashedCredentialsMatcher() {
    }

    public TryLimitHashedCredentialsMatcher(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        boolean loginSuccessOrNot = false;
        String principal = String.valueOf(token.getPrincipal());
        String cacheName = TRY_COUNT_CACHE_MAP_NAME_PREFIX + principal;
        Map<Object,Object> cacheMap = null;
        int count = 1;
        RBucket bucket = redissonClient.getBucket(cacheName);
        Object cache = bucket.get();
        if(cache == null){
            //未进行尝试登录情况(首次登录)
            cacheMap = new HashMap<>();
            cacheMap.put(cacheName,count);
            bucket.set(ShiroRedissonSerialize.serialize(cacheMap), SuperConstant.USER_LOGIN_FAIL_TIME / 1000, TimeUnit.SECONDS);
            loginSuccessOrNot = super.doCredentialsMatch(token, info);
        }else{
            cacheMap = (Map<Object,Object>)ShiroRedissonSerialize.deserialize(String.valueOf(cache));
            //已进行尝试登录情况
            count = Integer.parseInt(String.valueOf(cacheMap.get(cacheName)));
            if(count >= CacheConstant.USER_ALLOW_FAIL_COUNT){
                //登录尝试超过指定次数情况
                log.info("用户登录尝试次数过多,暂时无法登录");
                throw new ExcessiveAttemptsException("用户登录尝试次数过多,暂时无法登录");
            }else{
                //登录尝试未超过指定次数情况(计数)
                loginSuccessOrNot = super.doCredentialsMatch(token, info);
                count ++;
                cacheMap.put(cacheName,count);
                bucket.set(ShiroRedissonSerialize.serialize(cacheMap), SuperConstant.USER_LOGIN_FAIL_TIME / 1000, TimeUnit.SECONDS);
            }
        }
        if(loginSuccessOrNot){
            //如果登录成功,删除缓存中的登录失败计数
            bucket.delete();
        }else{
            log.info("登录失败,当前尝试次数" + count);
            throw new ExcessiveAttemptsException("用户名或密码错误");
        }
        return loginSuccessOrNot;
    }
}
