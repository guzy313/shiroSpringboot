package com.my.shirospringboot.shiro.core.impl;

import com.my.shirospringboot.shiro.core.cache.RedisCache2;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * @author guzy
 * @version 1.0
 * @description
 */
public class RedisCacheManager2 implements CacheManager {
    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
        System.out.println("getCache:"+cacheName+"......");
        return new RedisCache2<K,V>(cacheName);
    }
}
