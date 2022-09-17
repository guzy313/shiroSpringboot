package com.my.shirospringboot.shiro.core.bridge.impl;

import com.my.shirospringboot.mapper.ShUsersMapper;
import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.shiro.core.adapter.ShUserAdapter;
import com.my.shirospringboot.shiro.core.base.ShiroUser;
import com.my.shirospringboot.shiro.core.bridge.ShUsersBridgeService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guzy
 * @version 1.0
 * @description 用户信息桥接（后期做缓存）
 */
@Service
public class ShUsersBridgeServiceImpl implements ShUsersBridgeService {
    @Autowired
    private ShUserAdapter shUserAdapter;

    @Override
    public ShUsers findUserByLoginName(String loginName){
        List<ShUsers> shUsersList = shUserAdapter.findUserByLoginName(loginName);
        if(shUsersList.size() == 1){
            return shUsersList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<String> findRoleNamesByUserId(String userId) {
        List<ShRoles> roles = shUserAdapter.findRolesByUserId(userId);
        List<String> roleNameList = new ArrayList<>();
        for (ShRoles s:roles) {
            roleNameList.add(s.getRoleName());
        }
        return roleNameList;
    }

    @Override
    public List<String> findRoleLabelsByUserId(String userId) {
        List<ShRoles> roles = shUserAdapter.findRolesByUserId(userId);
        List<String> roleLabelList = new ArrayList<>();
        for (ShRoles s:roles) {
            roleLabelList.add(s.getLabel());
        }
        return roleLabelList;
    }

    @Override
    public List<String> findPermissionIdsByUserId(String userId) {
        List<ShPermission> shPermissionList = shUserAdapter.findPermissionsByUserId(userId);
        List<String> permissionIds = new ArrayList<>();
        for (ShPermission p:shPermissionList) {
            permissionIds.add(p.getId());
        }
        return permissionIds;
    }

    @Override
    public List<String> findPermissionNamesByUserId(String userId) {
        List<ShPermission> shPermissionList = shUserAdapter.findPermissionsByUserId(userId);
        List<String> permissionNames = new ArrayList<>();
        for (ShPermission p:shPermissionList) {
            permissionNames.add(p.getPermissionName());
        }
        return permissionNames;
    }

    @Override
    public List<String> findPermissionLabelsByUserId(String userId) {
        List<ShPermission> shPermissionList = shUserAdapter.findPermissionsByUserId(userId);
        List<String> permissionLabels = new ArrayList<>();
        for (ShPermission p:shPermissionList) {
            permissionLabels.add(p.getLabel());
        }
        return permissionLabels;
    }

    @Override
    public AuthorizationInfo getAuthorizationInfo(ShiroUser shiroUser) {
        //查询 用户 对应的 角色标识
        List<String> roleLabels = this.findRoleLabelsByUserId(shiroUser.getId());
        //查询 用户 对应的 资源标识
        List<String> permissionLabels = this.findPermissionLabelsByUserId(shiroUser.getId());
        //构建 授权信息类
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(roleLabels);//将角色标签存入 授权信息
        authorizationInfo.addStringPermissions(permissionLabels);//将权限标签 存入 授权信息

        return authorizationInfo;
    }
}