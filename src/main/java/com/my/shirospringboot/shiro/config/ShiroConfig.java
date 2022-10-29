package com.my.shirospringboot.shiro.config;

import com.my.shirospringboot.shiro.core.ShiroDbRealm;
import com.my.shirospringboot.shiro.core.filter.RolesOrAuthorizationFilter;
import com.my.shirospringboot.shiro.core.impl.ShiroDbRealmImpl;
import com.my.shirospringboot.utils.PropertiesUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.io.FileInputStream;
import java.util.*;

/**
 * @Description 权限管理控制类
 * @author Gzy
 * @version 1.0
 */


@Configuration
@EnableConfigurationProperties({ShiroRedisProperties.class})
@ComponentScan(basePackages = {"com.my.shirospringboot.shiro.core",})
@Log4j2
public class ShiroConfig {


    @Bean("redissonClientForShiro")
    public RedissonClient redissonClient(ShiroRedisProperties shiroRedisProperties){
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


    //创建权限管理器(入口,MAIN！！！！！！)
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //管理realm
        securityManager.setRealm(this.shiroDbRealm());
        //管理会话
        securityManager.setSessionManager(this.sessionManager());
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
        shiroDbRealm.setCachingEnabled(true);
        return new ShiroDbRealmImpl();
    }

    //创建会话管理器
    @Bean
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //关闭会话更新(因为没有配置会话定时任务,所以直接用最下面的设置过期时间,性能消耗比较大)
        sessionManager.setSessionValidationSchedulerEnabled(false);
        //设置cookie状态为开启(生效cookie)
        sessionManager.setSessionIdCookieEnabled(true);
        //指定cookie创建方式(创建方式是 当前类中simpleCookie())
        sessionManager.setSessionIdCookie(this.simpleCookie());
        //设置会话的过期时间((60 * 60 * 1000);//单位为毫秒)
        //此处设置为1小时过期
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        return sessionManager;
    }

    //创建生命周期管理
    @Bean(value = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    //创建AOP增强(使用注解授权方式)
    @Bean
    @DependsOn(value = "lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    //配合DefaultAdvisorAutoProxyCreator 事项注解权限校验
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(){
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
        return filtersMap;
    }




}
