package com.my.shirospringboot.unUse.impl;

import com.my.shirospringboot.shiro.config.ShiroRedisProperties;
import com.my.shirospringboot.unUse.cache.RedisCache;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;


/**
 * @author Gzy
 * @version 1.0
 * @Description redis缓存管理器 --未使用
 */

public class RedisCacheManager implements CacheManager{

    //获取日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ShiroRedisProperties shiroRedisProperties;

    /**
     * @Description: 注入redisson客户端类
     */
    @Resource(name = "redissonClientForShiro")
    private RedissonClient redissonClient;



    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new RedisCache<K, V>(
                shiroRedisProperties.getGlobalTimeout(),
                redissonClient);// 为了简化代码的编写，此处直接new一个Cache
    }

}
