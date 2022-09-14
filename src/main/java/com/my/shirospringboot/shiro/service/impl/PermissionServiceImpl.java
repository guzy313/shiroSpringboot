package com.my.shirospringboot.shiro.service.impl;

import com.my.shirospringboot.mapper.ShPermissionMapper;
import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.shiro.service.PermissionService;
import com.my.shirospringboot.shiro.vo.PermissionVo;
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
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private ShPermissionMapper shPermissionMapper;
    @Override
    public List<ShPermission> findPermissionList(PermissionVo shPermissionVo, Integer rows, Integer page) {

        return null;
    }

    @Override
    public Long countPermissionList(PermissionVo permissionVo) throws Exception {
        return null;
    }

    @Override
    public ShPermission findPermissionById(String id) throws Exception {
        return shPermissionMapper.selectById(id);
    }

    @Override
    public List<ShPermission> findPermissionListByParentId(String parentId) throws Exception {
        List<ShPermission> list = shPermissionMapper.findListByParentId(parentId);
        return list;
    }

    @Override
    public boolean savePermission(ShPermission shPermission) throws Exception {

        if(!StringUtils.hasLength(shPermission.getId())){
            //新增
            int insert = shPermissionMapper.insert(shPermission);
            if(insert > 0){
                return true;
            }
        }else{
            //修改
            int update = shPermissionMapper.updateById(shPermission);
            if(update > 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deletePermission(ShPermission shPermission) throws Exception {
        if(StringUtils.hasLength(shPermission.getId())){
            int delete = shPermissionMapper.deleteById(shPermission.getId());
            if(delete > 0){
                return true;
            }
        }
        return false;
    }
}
