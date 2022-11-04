package com.my.shirospringboot.shiro.core.impl;

import com.my.shirospringboot.shiro.core.cache.RedisCache1;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
public class RedisCacheManager1 implements CacheManager {
    /**
     * @Description: 注入redisson客户端类
     */
    @Resource(name = "redissonClientForShiro")
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     *
     * @param cacheName 认证或者授权缓存的统一名称
     * @param <K>
     * @param <V>
     * @return
     * @throws CacheException
     */
    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
        System.out.println("=========:"+cacheName);
//        return new RedisCache1<K,V>(redisTemplate);
        return new RedisCache1<K,V>(redissonClient);
    }
}
