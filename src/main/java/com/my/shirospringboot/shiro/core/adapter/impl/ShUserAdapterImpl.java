package com.my.shirospringboot.shiro.core.adapter.impl;

import com.my.shirospringboot.mapper.ShPermissionMapper;
import com.my.shirospringboot.mapper.ShRolesMapper;
import com.my.shirospringboot.mapper.ShUsersMapper;
import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.shiro.core.adapter.ShUserAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author guzy
 * @version 1.0
 * @description 
 */
@Repository
public class ShUserAdapterImpl implements ShUserAdapter {
    @Autowired
    private ShUsersMapper shUsersMapper;
    @Autowired
    private ShPermissionMapper shPermissionMapper;
    @Autowired
    private ShRolesMapper shRolesMapper;

    @Override
    public List<ShUsers> findUserByLoginName(String loginName){
        List<ShUsers> users = shUsersMapper.findShUserByName(loginName);
        return users;
    }


    @Override
    public List<ShPermission> findAllPermissions() {
        List<ShPermission> shPermissionList = shPermissionMapper.findAll();
        return shPermissionList;
    }

    @Override
    public List<ShPermission> findPermissionsByUserId(String userId) {
        List<ShPermission> shPermissionList = shPermissionMapper.findListByUserId(userId);
        return shPermissionList;
    }

    @Override
    public List<ShRoles> findRolesByUserId(String userId) {
        return shRolesMapper.findRolesByUserId(userId);
    }
}
