package com.my.shirospringboot.shiro.core.impl;

import com.my.shirospringboot.shiro.core.SimpleCacheService;
import com.my.shirospringboot.shiro.utils.SecurityUtils;
import com.my.shirospringboot.utils.ShiroRedissonSerialize;
import org.apache.shiro.cache.Cache;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Component
public class SimpleCacheServiceImpl implements SimpleCacheService {
    //注入redisson客户端类
    @Resource(name = "redissonClientForShiro")
    private RedissonClient redissonClient;


    @Override
    public void createCache(String cacheName, Cache<Object, Object> cache) {
        RBucket<String> bucket = redissonClient.getBucket(cacheName);
        bucket.trySet(ShiroRedissonSerialize.serialize(cache),
                SecurityUtils.getShiroSession().getTimeout()/1000, TimeUnit.SECONDS);
        //trySet和set的差别是trySet是在缓存中有对应名称的缓存则不进行操作,没有则新增
    }

    @Override
    public Cache<Object, Object> getCache(String cacheName) {
        RBucket<String> bucket = redissonClient.getBucket(cacheName);
        return (Cache<Object, Object>)ShiroRedissonSerialize.deserialize(bucket.get());
    }

    @Override
    public void removeCache(String cacheName) {
        RBucket<String> bucket = redissonClient.getBucket(cacheName);
        bucket.delete();
    }

    @Override
    public void updateCache(String cacheName, Cache<Object, Object> cache) {
        RBucket<String> bucket = redissonClient.getBucket(cacheName);
        bucket.set(ShiroRedissonSerialize.serialize(cache),
                SecurityUtils.getShiroSession().getTimeout()/1000, TimeUnit.SECONDS);
    }
}
