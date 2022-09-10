package com.my.shirospringboot.shiro.core;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.PostConstruct;

/**
 * @description 自定义realm抽象类
 * @author guzy
 * @version 1.0
 */
public abstract class ShiroDbRealm extends AuthorizingRealm {

    /**
     * @Description 授权
     * @param principalCollection
     * @return
     */
    protected abstract AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection);

    /**
     * @Description 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    protected abstract AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException;


    /**
     * @Description 自定义密码比较器
     */
    @PostConstruct //在类初始化的时候 加载被这个标签标注的方法
    public abstract void initCredentialsMatcher();

}
