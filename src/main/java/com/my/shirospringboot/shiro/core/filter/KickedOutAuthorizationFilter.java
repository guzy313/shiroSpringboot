package com.my.shirospringboot.shiro.core.filter;

import com.my.shirospringboot.shiro.constant.CacheConstant;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.core.impl.MySessionManager;
import com.my.shirospringboot.shiro.utils.SecurityUtils;
import com.my.shirospringboot.utils.ShiroRedissonSerialize;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.crazycake.shiro.RedisSessionDAO;
import org.redisson.api.RBucket;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Gzy
 * @version 1.0
 * @Description: 实现并发人数控制-redis队列
 * 删除用户队列中过期的会话
 * 根据sessionID从redis拿到session,
 * 用redis操作session会话
 *
 */
public class KickedOutAuthorizationFilter extends AccessControlFilter{
    private  final Logger log = LoggerFactory.getLogger(this.getClass());
    private RedisSessionDAO redisSessionDAO;
    private MySessionManager sessionManager;
    private RedissonClient redissonClient;
    private int timout;

    public KickedOutAuthorizationFilter() {
    }

    public KickedOutAuthorizationFilter(RedisSessionDAO redisSessionDAO, MySessionManager sessionManager, RedissonClient redissonClient,int timout) {
        this.redisSessionDAO = redisSessionDAO;
        this.sessionManager = sessionManager;
        this.redissonClient = redissonClient;
        this.timout = timout;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //获取当前会话是否登录成功
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        log.info("登录并发过滤器中是否认证成功:"+authenticated);
        //如果当前登录用户认证成功,则开始操作当前账号的用户sessionID的队列
        if(authenticated){
            String sessionId = SecurityUtils.getShiroSessionId();
            //获取当前登录用户的sessionID队列
            RQueue<String> queue = redissonClient.
                    getQueue(CacheConstant.ACTIVE_SESSIONID_QUEUE_PREFIX + SecurityUtils.getShUsers().getLoginName());
            //判断当前会话ID是否出于队列中，不处于队列中则进行操作
            boolean contains = queue.contains(sessionId);
            if(!contains){
                //查询队列中已经过期的会话进行删除
                List<String> queueSessionIdList = queue.readAll();
                RBucket<String> queueSessionIdBucket = null;
                for (String queueSessionId:queueSessionIdList) {
                    queueSessionIdBucket = redissonClient.getBucket(CacheConstant.SESSION_PREFIX + queueSessionId);
                    boolean exists = queueSessionIdBucket.isExists();
                    if(!exists){
                        log.info("删除过期会话sessionID:" + queueSessionId);
                        queue.remove(queueSessionId);
                    }
                }
                //如果不存在当前会话ID则进行队列操作
                //判断账号当前在线人数是否已经达到上限
                if(queue.size() >= SuperConstant.ACTIVE_NUM_LIMIT){
                    //并发登录人数已满,则:
                    // 将最先登录的用户从队列头部踢出,将当前用户sessionID存入队列尾部
                    String willBeKickUserSessionId = queue.pollLastAndOfferFirstTo(sessionId);
                    log.info("被踢出的用户sessionID:" + willBeKickUserSessionId);
                    //通过redisson删除被踢出用户在redis缓存中的会话
                    String willBeKickUserSessionKey = CacheConstant.SESSION_PREFIX + willBeKickUserSessionId;
                    RBucket<Object> bucket = redissonClient.getBucket(willBeKickUserSessionKey);
                    bucket.delete();
                    //将当前用户sessionID存入队列尾部
                    queue.expire(this.timout,TimeUnit.SECONDS);
                    queue.add(sessionId);
                }{
                    //并发人数未满
                    //将当前用户sessionID存入队列尾部
                    queue.add(sessionId);
                    queue.expire(this.timout,TimeUnit.SECONDS);
                    log.info("成功将当前用户sessionID存入队列");
                }
            }
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("11111111111111111111111111111111111111111");
        return true;
    }

}
