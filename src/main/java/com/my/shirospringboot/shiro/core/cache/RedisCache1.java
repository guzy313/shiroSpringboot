package com.my.shirospringboot.shiro.core.cache;

import com.my.shirospringboot.shiro.config.ShiroRedisProperties;
import com.my.shirospringboot.utils.ApplicationContextUtils;
import com.my.shirospringboot.utils.ShiroRedissonSerialize;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Gzy
 * @version 1.0
 * @Description: 自定义redis缓存
 */
public class RedisCache1<K,V> implements Cache<K,V> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private RedissonClient redissonClient;

    private RedisTemplate redisTemplate;

    public RedisCache1(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public RedisCache1(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
    }


    @Autowired
    private ShiroRedisProperties shiroRedisProperties;


    @Override
    public V get(K k) throws CacheException {
        logger.info("get key:" + k);
//        logger.info("redisTemplate:"+redisTemplate);
//        V v = (V)ShiroRedissonSerialize.deserialize(String.valueOf(redisTemplate.opsForValue().get(k)));
//        V v = (V)redisTemplate.opsForValue().get(k);
//        return v;
        RBucket<String> bucket = redissonClient.getBucket(String.valueOf(k));
        V v = (V)ShiroRedissonSerialize.deserialize(bucket.get());
        logger.info("====================get的结果:" + v);
        return v;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        logger.info("put key:" + k);
//        logger.info("redisTemplate:"+redisTemplate);
//        redisTemplate.opsForValue().set(k,v, 1000, TimeUnit.SECONDS);
//        redisTemplate.opsForValue().set(k,ShiroRedissonSerialize.serialize(v), 1000, TimeUnit.SECONDS);
        RBucket<String> bucket =  redissonClient.getBucket(String.valueOf(k));
        bucket.set(ShiroRedissonSerialize.serialize(v),1000,TimeUnit.SECONDS);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        logger.info("delete key:" + k);
        return null;
    }

    @Override
    public void clear() throws CacheException {
        logger.info("clear key..");
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


    
    
}
