package com.my.shirospringboot.shiro.core.impl;

import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.core.ShiroDbRealm;
import com.my.shirospringboot.shiro.core.base.ShiroUser;
import com.my.shirospringboot.shiro.core.base.SimpleToken;
import com.my.shirospringboot.shiro.core.bridge.impl.ShUsersBridgeServiceImpl;
import com.my.shirospringboot.utils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author guzy
 * @version 1.0
 * @description
 */
public class ShiroDbRealmImpl extends ShiroDbRealm {
    @Autowired
    private ShUsersBridgeServiceImpl shUsersBridgeService;




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
        if(SuperConstant.ADMINISTRATOR_NAME.equals(shiroUser.getLoginName())){
            //管理员权限对象列表
            List<ShPermission> permissionIdList = shUsersBridgeService.findAllPermissions();
            shiroUser.setPermissionList(permissionIdList);

            //构建认证信息对象 ! 此处把用户的完整信息shiroUser放入认证信息对象
            return new SimpleAuthenticationInfo(shiroUser,
                    shiroUser.getPassword(), ByteSource.Util.bytes(shiroUser.getSalt()),this.getName());
         }else{
            List<ShPermission> permissionIdList = shUsersBridgeService.findPermissionsByUserId(user.getId());
            shiroUser.setPermissionList(permissionIdList);
            //构建认证信息对象 ! 此处把用户的完整信息shiroUser放入认证信息对象
            return new SimpleAuthenticationInfo(shiroUser,
                    shiroUser.getPassword(), ByteSource.Util.bytes(shiroUser.getSalt()),this.getName());
        }

    }

    /**
     * @Description 授权
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

    @Override
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法 散列算法1
        credentialsMatcher.setHashAlgorithmName(SuperConstant.SHA1);
        //设置加密次数
        credentialsMatcher.setHashIterations(SuperConstant.HashIterations);
        //设置当前realm的密码匹配器 使密码匹配器生效
        this.setCredentialsMatcher(credentialsMatcher);
    }
}
