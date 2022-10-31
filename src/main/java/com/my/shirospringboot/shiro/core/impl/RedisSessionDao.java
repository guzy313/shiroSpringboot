package com.my.shirospringboot.shiro.core.impl;

import com.my.shirospringboot.shiro.config.ShiroRedisProperties;
import com.my.shirospringboot.shiro.constant.CacheConstant;
import com.my.shirospringboot.utils.ShiroRedissonSerialize;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author Gzy
 * @version 1.0
 * @Description: 自定义统一sessionDao 实现(redis会话管理Dao)
 */
public class RedisSessionDao extends AbstractSessionDAO{
    /**
     * @Description: 注入redisson客户端类
     */
    @Resource(name = "redissonClientForShiro")
    private RedissonClient redissonClient;


    /**
     * @Description: 全局超时时间
     */
    private long globalTimeout;

    /**
     * @Description: 创建session(将会话存入redis缓存中)
     * @param session 会话对象
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        //创建唯一标识sessionID
        Serializable sessionId = generateSessionId(session);
        //为session会话指定唯一的sessionID
        assignSessionId(session,sessionId);
        //将session会话放入redis缓存中(当前会话缓存)
        String key = CacheConstant.GROUP_CAS + sessionId.toString();
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.trySet(
                ShiroRedissonSerialize.serialize(session),//interface不能序列化需要使用序列化工具
                this.globalTimeout/1000,//获取全局会话超时时间
                TimeUnit.SECONDS);
        return sessionId;
    }

    /**
     * @Description: 从redis缓存中读取存储的session(会话对象)
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        String key = CacheConstant.GROUP_CAS + sessionId.toString();
        //从redis缓存中取出会话对象
        RBucket<String> bucket = redissonClient.getBucket(key);
        //将取出的会话对象(String类型)进行反序列化成Object对象，强制类型转换 还原成Session
        Session session = (Session)ShiroRedissonSerialize.deserialize(bucket.get());
        return session;
    }

    /**
     * @Description: 更新session(将redis缓存中的会话更新)
     * @param session
     * @throws UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        //创建唯一标识sessionID
        Serializable sessionId = generateSessionId(session);
        //更新缓存内的session
        String key = CacheConstant.GROUP_CAS + sessionId;
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(ShiroRedissonSerialize.serialize(session),this.globalTimeout/1000,TimeUnit.SECONDS);
    }

    /**
     * @Description: 删除缓存中的session会话
     * @param session
     */
    @Override
    public void delete(Session session) {
        //创建唯一标识sessionID
        Serializable sessionId = generateSessionId(session);
        //删除缓存内的session
        String key = CacheConstant.GROUP_CAS + sessionId;
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.delete();
    }

    /**
     * @Description: 获取当前活跃的用户数量
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        // TODO -方案1 精准-存储对象 方案2 存储数量 (添加计数器)
        return Collections.emptySet();
    }

    public void setGlobalTimeout(long globalTimeout) {
        this.globalTimeout = globalTimeout;
    }
}
