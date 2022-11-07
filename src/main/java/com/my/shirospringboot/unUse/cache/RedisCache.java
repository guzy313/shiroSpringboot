package com.my.shirospringboot.unUse.cache;

import com.my.shirospringboot.shiro.config.ShiroRedisProperties;
import com.my.shirospringboot.shiro.constant.CacheConstant;
import com.my.shirospringboot.utils.ShiroRedissonSerialize;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Component
public class RedisCache<K,V> implements Cache<K,V>{ //获取日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * @Description: 注入redisson客户端类
     */
    @Resource(name = "redissonClientForShiro")
    private RedissonClient redissonClient;

    private ShiroRedisProperties shiroRedisProperties;
    /**
     * @Description: 全局超时时间(ms 毫秒)
     */
    private long globalTimeout = 1000;

    private String cacheName;

    public RedisCache() {
        super();
    }

    public RedisCache(long globalTimeout, RedissonClient redissonClient) {
        super();
        this.globalTimeout = globalTimeout;
        this.redissonClient = redissonClient;
    }

    /**
     * 通过key来获取对应的缓存对象
     * 通过源码我们可以发现，shiro需要的key的类型为Object，V的类型为AuthorizationInfo对象
     */
    @Override
    public V get(K key) throws CacheException {
        this.cacheName = CacheConstant.GROUP_CAS + key;
        logger.info("从"+this.cacheName+"中get key:" + key);
        RBucket<String> bucket = redissonClient.getBucket(this.cacheName);
        V v = (V)ShiroRedissonSerialize.deserialize(bucket.get());
        return v;
    }

    /**
     * 将权限信息加入缓存中
     */
    @Override
    public V put(K key, V value) throws CacheException {
        this.cacheName = CacheConstant.GROUP_CAS + key;
        logger.info("向"+this.cacheName+"中put value:"+value);
        RBucket<String> bucket = redissonClient.getBucket(this.cacheName);
        bucket.set(ShiroRedissonSerialize.serialize(value), this.globalTimeout, TimeUnit.SECONDS);
        return value;
    }

    /**
     * 将权限信息从缓存中删除
     */
    @Override
    public V remove(K key) throws CacheException {
        this.cacheName = CacheConstant.GROUP_CAS + key;
        logger.info("退出时执行的是remove函数,移除:"+this.cacheName);
        RBucket<String> bucket = redissonClient.getBucket(this.cacheName);
        V v = (V)ShiroRedissonSerialize.serialize(bucket.get());
        bucket.delete();
        return v;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    public long getGlobalTimeout() {
        return globalTimeout;
    }

    public void setGlobalTimeout(long globalTimeout) {
        this.globalTimeout = globalTimeout;
    }
}
