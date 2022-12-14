package com.my.shirospringboot.shiro.config;

import com.my.shirospringboot.shiro.constant.CacheConstant;
import com.my.shirospringboot.shiro.core.ShiroDbRealm;
import com.my.shirospringboot.shiro.core.filter.*;
import com.my.shirospringboot.shiro.core.impl.*;
import com.my.shirospringboot.utils.PropertiesUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisClusterManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * @Description 权限管理控制类
 * @author Gzy
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties({ShiroRedisProperties.class})
@ComponentScan(basePackages = {"com.my.shirospringboot.shiro.core"})
@Log4j2
public class ShiroConfig {

    @Autowired
    private ShiroRedisProperties shiroRedisProperties;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Value("${crazyredis.nodes}")
    private String nodes;

    @Value("${crazyredis.host}")
    private String host;

    @Value("${crazyredis.timeout}")
    private int timeout;

    @Value("${crazyredis.password}")
    private String redisPassword;

    @Value("${crazyredis.clusteropen}")
    private Boolean clusterOpen;


    /**
     * @Description: 权限(调用redis)缓存(redisson)客户端
     * @param
     * @return
     */
    @Bean("redissonClientForShiro")
    public RedissonClient redissonClient(){
        //获取redis节点信息
        String[] nodes = shiroRedisProperties.getNodes().split(",");
        Config config = new Config();
        //创建配置信息 1.单机redis配置 2.集群redis配置
        //判断是集群还是单节点
        if(nodes.length == 1){
            //单机配置
            config.useSingleServer().setAddress(nodes[0])
                                    .setConnectTimeout(shiroRedisProperties.getConnectTimeout())
                                    .setConnectionMinimumIdleSize(shiroRedisProperties.getMinIdle())
                                    .setConnectionPoolSize(shiroRedisProperties.getMaxActive())
                                    .setTimeout(shiroRedisProperties.getTimeout())
                                    .setPassword(shiroRedisProperties.getPassword());
        }else if(nodes.length > 1){
            //集群配置
            config.useClusterServers().addNodeAddress(nodes)
                    .setConnectTimeout(shiroRedisProperties.getConnectTimeout())
                    .setMasterConnectionMinimumIdleSize(shiroRedisProperties.getMinIdle())
                    .setMasterConnectionPoolSize(shiroRedisProperties.getMaxActive())
                    .setTimeout(shiroRedisProperties.getTimeout())
                    .setPassword(shiroRedisProperties.getPassword());
        }else{
            return null;
        }
        //创建redisson客户端交给spring管理
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }



    ///////////////////以下为crazy reids集成//////////////////////////
    /**
     * redis集群管理器
     *
     * @return redisClusterManager
     */
    @Bean
    public RedisClusterManager redisClusterManager() {
        RedisClusterManager redisClusterManager = new RedisClusterManager();
        redisClusterManager.setHost(this.nodes);
        redisClusterManager.setPassword(this.redisPassword);
        return redisClusterManager;
    }

    /**
     * redis管理器
     *
     * @return redisManager
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setPassword(this.redisPassword);
        return redisManager;
    }

    /**
     * redis缓存管理器
     *
     * @param redisClusterManager redis集群管理器
     * @param redisManager        redis管理器
     * @return cacheManager
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        //指定redis管理器(集群或者单机模式)
        redisCacheManager.setRedisManager(this.clusterOpen ? this.redisClusterManager() : this.redisManager());
        // 针对不同的用户缓存，由于principal是ShiroUser，所以需是里面的字段(id)
        redisCacheManager.setPrincipalIdFieldName("id");
        //设置缓存的有效时间
        redisCacheManager.setExpire(this.timeout);

        return redisCacheManager;
    }


    //创建权限管理器(入口,MAIN！！！！！！)
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //管理会话
        securityManager.setSessionManager(this.sessionManager());
        //管理realm
        securityManager.setRealm(this.shiroDbRealm());
        return securityManager;
    }

    //创建cookie对象
    @Bean("simpleCookie")
    public SimpleCookie simpleCookie(){
        SimpleCookie simpleCookie = new SimpleCookie();
        //设置shiro的cookie名称(可以随便取名)
        simpleCookie.setName("ShiroSession");
        return simpleCookie;
    }

    //创建自定义realm
    @Bean(value = "shiroDbRealm")
    public ShiroDbRealm shiroDbRealm(){
        ShiroDbRealm shiroDbRealm = new ShiroDbRealmImpl();
        //设置缓存管理器
        shiroDbRealm.setCacheManager(this.redisCacheManager());
        //开启缓存管理
        shiroDbRealm.setCachingEnabled(true);
        //认证缓存开启
        shiroDbRealm.setAuthenticationCachingEnabled(true);
        shiroDbRealm.setAuthenticationCacheName("authenticationCache");
        //授权缓存开启
        shiroDbRealm.setAuthorizationCachingEnabled(true);
        shiroDbRealm.setAuthorizationCacheName("authorizationCaching");
        return new ShiroDbRealmImpl();
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(this.redisManager());
        redisSessionDAO.setExpire(this.timeout);
        //设置会话缓存key的前缀
        redisSessionDAO.setKeyPrefix(CacheConstant.SESSION_PREFIX);
        return redisSessionDAO;
    }


    @Bean
    public MySessionManager redisSessionManager(){
        return new MySessionManager();
    }

    @Bean
    public JwtSessionManager jwtSessionManager(){
        return new JwtSessionManager();
    }


    //创建会话管理器
//    @Bean
//    public DefaultWebSessionManager sessionManager(){
//        DefaultWebSessionManager sessionManager = this.redisSessionManager();
//
//        //设置 自定义的会话Dao(解决分布式问题,将会话写入redis缓存)
//        sessionManager.setSessionDAO(this.redisSessionDAO());
//
//        //设置删除无效会话
//        sessionManager.setDeleteInvalidSessions(true);
//        //关闭会话更新(因为没有配置会话定时任务,所以直接用最下面的设置过期时间,性能消耗比较大)
//        sessionManager.setSessionValidationSchedulerEnabled(false);
//        //设置cookie状态为开启(生效cookie)
//        sessionManager.setSessionIdCookieEnabled(true);
//        //指定cookie创建方式(创建方式是 当前类中simpleCookie())
//        sessionManager.setSessionIdCookie(this.simpleCookie());
//        //设置会话的过期时间((60 * 60 * 1000);//单位为毫秒)
//        //此处设置为半小时过期
//        sessionManager.setGlobalSessionTimeout(30 * 60 * 1000);
//        return sessionManager;
//    }

    //创建会话管理器
    @Bean
    public JwtSessionManager sessionManager(){
        JwtSessionManager sessionManager = this.jwtSessionManager();

        //设置 自定义的会话Dao(解决分布式问题,将会话写入redis缓存)
        sessionManager.setSessionDAO(this.redisSessionDAO());

        //设置删除无效会话
        sessionManager.setDeleteInvalidSessions(true);
        //关闭会话更新(因为没有配置会话定时任务,所以直接用最下面的设置过期时间,性能消耗比较大)
        sessionManager.setSessionValidationSchedulerEnabled(false);
        //设置cookie状态为开启(生效cookie)
        sessionManager.setSessionIdCookieEnabled(true);
        //指定cookie创建方式(创建方式是 当前类中simpleCookie())
        sessionManager.setSessionIdCookie(this.simpleCookie());
        //设置会话的过期时间((60 * 60 * 1000);//单位为毫秒)
        //此处设置为半小时过期
        sessionManager.setGlobalSessionTimeout(30 * 60 * 1000);
        return sessionManager;
    }



    //创建生命周期管理
//    @Bean(value = "lifecycleBeanPostProcessor")
//    public  LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
//        return new LifecycleBeanPostProcessor();
//    }

    //创建AOP增强(使用注解授权方式)
//    @Bean
//    @DependsOn(value = "lifecycleBeanPostProcessor")
//    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
//        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        advisorAutoProxyCreator.setProxyTargetClass(true);
//        return advisorAutoProxyCreator;
//    }

    //配合DefaultAdvisorAutoProxyCreator 事项注解权限校验
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(ShiroRedisProperties shiroRedisProperties){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }

    //创建shiro过滤器管理
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置权限管理器
        shiroFilterFactoryBean.setSecurityManager(this.defaultWebSecurityManager());

        //设置过滤器 一般不用自己设置
        //设置自定义过滤器(自己写的过滤器)
        shiroFilterFactoryBean.setFilters(this.customsFilters());

        //设置过滤器链
        Map<String, String> filterChainProperties = PropertiesUtils.getPropertiesMapByFileName("authentication.properties");
            //设置过滤地址
            shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainProperties);
            //设置登录地址
            shiroFilterFactoryBean.setLoginUrl("/account/login");
            //设置未授权访问跳转地址
            shiroFilterFactoryBean.setUnauthorizedUrl("/account/login");
        return shiroFilterFactoryBean;
    }




    /**
     * 自定义过滤器
     * @return filtersMap -自定义的过滤器Map<String, Filter>集合
     */
    private Map<String, Filter> customsFilters(){
        Map<String, Filter> filtersMap = new HashMap<>();
        filtersMap.put("roles-or",new RolesOrAuthorizationFilter());
        filtersMap.put("kickout",new KickedOutAuthorizationFilter(this.redisSessionDAO(),
                this.redisSessionManager(),
                this.redissonClient(),
                this.timeout
                ));
        filtersMap.put("jwt-authc",new JwtAuthenticationFilter(jwtTokenManager));
        filtersMap.put("jwt-perms",new JwtPermsFilter());
        filtersMap.put("jwt-roles",new JwtRolesFilter());
        return filtersMap;
    }




}
