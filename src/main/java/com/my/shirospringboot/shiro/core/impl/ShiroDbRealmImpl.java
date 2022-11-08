package com.my.shirospringboot.shiro.core.impl;

import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.shiro.constant.CacheConstant;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.core.ShiroDbRealm;
import com.my.shirospringboot.shiro.core.base.ShiroUser;
import com.my.shirospringboot.shiro.core.base.SimpleToken;
import com.my.shirospringboot.shiro.core.base.TryLimitHashedCredentialsMatcher;
import com.my.shirospringboot.shiro.core.bridge.impl.ShUsersBridgeServiceImpl;
import com.my.shirospringboot.shiro.utils.SecurityUtils;
import com.my.shirospringboot.utils.BeanUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author guzy
 * @version 1.0
 * @description
 */
public class ShiroDbRealmImpl extends ShiroDbRealm {
    @Autowired
    private ShUsersBridgeServiceImpl shUsersBridgeService;

    @Autowired
    private SimpleCacheServiceImpl simpleCacheService;


    //注入redisson客户端类
    @Resource(name = "redissonClientForShiro")
    private RedissonClient redissonClient;


    /**
     * @Description 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //token 获取令牌信息
        SimpleToken customToken = (SimpleToken)token;
        //查询User对象
        ShUsers user = shUsersBridgeService.findUserByLoginName(customToken.getUsername());
        if(user == null){
            throw new UnknownAccountException("用户不存在");
        }
        //构建认证令牌对象
        ShiroUser shiroUser = new ShiroUser();
        BeanUtils.copyPropertiesIgnoreNull(user,shiroUser);
        //桥接器内会通过登录名称来判断是否管理员权限
        List<ShPermission> permissionIdList =
                shUsersBridgeService.findPermissionsByUserId(shiroUser.getLoginName(),user.getId());
        shiroUser.setPermissionList(permissionIdList);
        //构建认证信息对象 ! 此处把用户的完整信息shiroUser放入认证信息对象
        return new SimpleAuthenticationInfo(shiroUser,
                shiroUser.getPassword(), ByteSource.Util.bytes(shiroUser.getSalt()),this.getName());
    }

    /**
     * @Description 授权-将用户的角色、权限标签写入令牌
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ShiroUser shiroUser = (ShiroUser)principalCollection.getPrimaryPrincipal();
        //从桥接器获取授权信息
        AuthorizationInfo authorizationInfo = shUsersBridgeService.getAuthorizationInfo(shiroUser);

        return authorizationInfo;
    }

    /**
     * @Description: 重写清除缓存方法
     * @param principals
     */
    @Override
    protected void doClearCache(PrincipalCollection principals) {
        super.doClearCache(principals);
        ///////////以下为需要清除的自定义缓存////////////
        //根据不同会话ID获取缓存
        ShiroUser shiroUser = (ShiroUser)principals.getPrimaryPrincipal();
        String sessionId = SecurityUtils.getShiroSessionId();
        //登录用户名称缓存
        String loginName = CacheConstant.FIND_USER_BY_LOGINNAME + shiroUser.getLoginName();
        //角色标签 缓存
        String roleLabelKey = CacheConstant.ROLE_LABEL_KEY + sessionId;
        //资源/权限标签 缓存
        String permissionLabelKey = CacheConstant.PERMISSION_LABEL_KEY + sessionId;
        //资源/权限 对象 缓存
        String permissionsKey = CacheConstant.PERMISSION_KEY + sessionId;

        simpleCacheService.removeCache(loginName);
        simpleCacheService.removeCache(roleLabelKey);
        simpleCacheService.removeCache(permissionLabelKey);
        simpleCacheService.removeCache(permissionsKey);
    }

    /**
     * @Description: 密码比较器
     */
    @Override
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new TryLimitHashedCredentialsMatcher(redissonClient);
        //设置加密算法 散列算法1
        credentialsMatcher.setHashAlgorithmName(SuperConstant.SHA1);
        //设置加密次数
        credentialsMatcher.setHashIterations(SuperConstant.HASH_ITERATIONS);
        //设置当前realm的密码匹配器 使密码匹配器生效
        this.setCredentialsMatcher(credentialsMatcher);
    }
}
