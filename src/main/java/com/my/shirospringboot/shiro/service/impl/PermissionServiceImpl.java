package com.my.shirospringboot.shiro.service.impl;

import com.my.shirospringboot.mapper.ShPermissionMapper;
import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.shiro.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private ShPermissionMapper shPermissionMapper;
    @Override
    public List<ShPermission> findPermissionList(ShPermission shPermission, Integer rows, Integer page) {
        return null;
    }
}
