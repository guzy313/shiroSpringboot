package com.my.shirospringboot.shiro.service.impl;

import com.my.shirospringboot.mapper.ShRolesMapper;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.shiro.service.RoleService;
import com.my.shirospringboot.shiro.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private ShRolesMapper shRolesMapper;

    @Override
    public List<ShRoles> findRolesList(RoleVo roleVo, Integer rows, Integer page) throws Exception {
        //通过用户查询对应角色
        if(StringUtils.hasLength(roleVo.getUserId())){
            List<ShRoles> list = shRolesMapper.findRolesByUserId(roleVo.getUserId());
            return list;
        }
        //直接查询全部角色列表
        List<ShRoles> list = shRolesMapper.findAll();
        return list;
    }

    @Override
    public Long countRolesList(RoleVo roleVo) throws Exception {
        //通过用户查询对应角色 的数量
        if(StringUtils.hasLength(roleVo.getUserId())){
            List<ShRoles> list = shRolesMapper.findRolesByUserId(roleVo.getUserId());
            return Long.parseLong(String.valueOf(list.size())) ;
        }
        //直接查询全部角色列表 的数量
        List<ShRoles> list = shRolesMapper.findAll();
        return Long.parseLong(String.valueOf(list.size())) ;
    }
}
