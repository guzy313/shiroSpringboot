package com.my.shirospringboot.shiro.core.cache;

import com.my.shirospringboot.utils.ApplicationContextUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collection;
import java.util.Set;

/**
 * @author guzy
 * @version 1.0
 * @description
 */
public class RedisCache2<k,v> implements Cache<k,v> {

    private String cacheName;

    public RedisCache2() {
    }

    public RedisCache2(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public v get(k k) throws CacheException {
        System.out.println("get k:" + k);
        return (v) getRedisTemplate().opsForValue().get(k.toString());
    }

    @Override
    public v put(k k, v v) throws CacheException {
        System.out.println("put key:" + k);
        System.out.println("put value:" + v);
        getRedisTemplate().opsForValue().set(k.toString(), v);
        return null;
    }

    @Override
    public v remove(k k) throws CacheException {
        return (v) getRedisTemplate().opsForHash().delete(this.cacheName, k.toString());
    }

    @Override
    public void clear() throws CacheException {
        System.out.println("=============clear==============");
        getRedisTemplate().delete(this.cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<k> keys() {
        return getRedisTemplate().opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<v> values() {
        return getRedisTemplate().opsForHash().values(this.cacheName);
    }

    private RedisTemplate getRedisTemplate() {
        // 这里是通过自定义的获取bean的方法，进行转化得到的数据
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        // Creates a new StringRedisSerializer using UTF-8.序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // Creates a new StringRedisSerializer using UTF-8.序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
