package com.my.shirospringboot.shiro.service.impl;

import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.shiro.service.RoleService;
import com.my.shirospringboot.shiro.vo.RoleVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Service
public class RoleServiceImpl implements RoleService {


    @Override
    public List<ShRoles> findRolesList(RoleVo roleVo, Integer rows, Integer page) throws Exception {



        return null;
    }

    @Override
    public Long countRolesList(RoleVo roleVo) throws Exception {
        return null;
    }
}
