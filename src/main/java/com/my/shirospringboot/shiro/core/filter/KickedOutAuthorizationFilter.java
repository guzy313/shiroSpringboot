package com.my.shirospringboot.shiro.core.filter;

import com.my.shirospringboot.shiro.constant.CacheConstant;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.core.impl.MySessionManager;
import com.my.shirospringboot.shiro.utils.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.crazycake.shiro.RedisSessionDAO;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Collection;

/**
 * @author Gzy
 * @version 1.0
 * @Description: 实现并发人数控制-redis队列
 * 根据sessionID从sessionManager拿到session,
 * 用sessionDao操作session会话
 */
public class KickedOutAuthorizationFilter extends AccessControlFilter{
    private  final Logger log = LoggerFactory.getLogger(this.getClass());
    private RedisSessionDAO redisSessionDAO;
    private MySessionManager sessionManager;
    private RedissonClient redissonClient;

    public KickedOutAuthorizationFilter() {
    }

    public KickedOutAuthorizationFilter(RedisSessionDAO redisSessionDAO, MySessionManager sessionManager, RedissonClient redissonClient) {
        this.redisSessionDAO = redisSessionDAO;
        this.sessionManager = sessionManager;
        this.redissonClient = redissonClient;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //获取当前登录用户的sessionID队列
        RQueue<String> queue = redissonClient.
                getQueue(CacheConstant.ACTIVE_SESSIONID_QUEUE_PREFIX + SecurityUtils.getShUsers().getLoginName());
        if(queue.size() >= SuperConstant.ACTIVE_NUM_LIMIT){
            //并发登录人数已满,则将最先登录的用户 从队列头部踢出
            String willBeKickUserSessionId = queue.poll();

            //将当前用户sessionID存入队列尾部
            //TODO
        }{
            //并发人数未满
            //将当前用户sessionID存入队列尾部
        }

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return false;
    }

}
