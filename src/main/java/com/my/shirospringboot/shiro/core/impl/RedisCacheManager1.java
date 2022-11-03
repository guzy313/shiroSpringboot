package com.my.shirospringboot.shiro.core.impl;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
public class RedisCacheManager1 implements CacheManager {

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        System.out.println("=========:"+s);
        return null;
    }
}
