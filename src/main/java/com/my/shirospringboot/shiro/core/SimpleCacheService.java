package com.my.shirospringboot.shiro.core;

import org.apache.shiro.cache.Cache;

/**
 * @author Gzy
 * @version 1.0
 * @Description 缓存管理接口
 */
public interface SimpleCacheService {

    /**
     * @Description 创建缓存
     * @param cacheName 缓存名称
     * @param cache 缓存对象
     */
    void createCache(String cacheName, Cache<Object,Object> cache);

    /**
     * @Description 获取缓存
     * @param cacheName 缓存名称
     * @return Cache 缓存对象
     */
    Cache<Object,Object> getCache(String cacheName);

    /**
     * @Description 删除缓存
     * @param cacheName 缓存名称
     */
    void removeCache(String cacheName);

    /**
     * @Description 更新缓存
     * @param cacheName 缓存名称
     * @param cache 缓存对象
     */
    void updateCache(String cacheName,Cache<Object,Object> cache);


}
