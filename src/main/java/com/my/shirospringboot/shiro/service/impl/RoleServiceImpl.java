package com.my.shirospringboot.shiro.service.impl;

import com.my.shirospringboot.mapper.ShPermissionMapper;
import com.my.shirospringboot.mapper.ShRolePermissionMapper;
import com.my.shirospringboot.mapper.ShRolesMapper;
import com.my.shirospringboot.pojo.ShRolePermission;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.service.RoleService;
import com.my.shirospringboot.shiro.vo.RoleVo;
import com.my.shirospringboot.shiro.vo.UserVo;
import com.my.shirospringboot.utils.BeanUtils;
import com.my.shirospringboot.utils.PageUtils;
import com.my.shirospringboot.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private ShRolesMapper shRolesMapper;
    @Autowired
    private ShRolePermissionMapper shRolePermissionMapper;

    @Override
    public List<Map<String,Object>> findRolesList(RoleVo roleVo, Integer pageSize, Integer pageIndex,String keyword) throws Exception {
        //分页结果集合对象
        List<Map<String,Object>> resultList = null;
        //查询结果集合对象
        List<ShRoles> list = null;
        //查询条件集合
        Map<String,Object> conditionMap = new HashMap<>();

        //通过用户查询对应角色
        if(StringUtils.hasLength(roleVo.getUserId())){
            list = shRolesMapper.findRolesByUserId(roleVo.getUserId());
            //转map集合
            resultList = BeanUtils.objectListToMapList(list);
            return resultList;
        }

        //通过角色ID查询对应角色
        if(StringUtils.hasLength(roleVo.getId())){
            conditionMap.put("id",roleVo.getId());
            list = shRolesMapper.selectByMap(conditionMap);
            //转map集合
            resultList = BeanUtils.objectListToMapList(list);
            return resultList;
        }

        if(StringUtils.hasLength(keyword)){
            list = shRolesMapper.getShRolesByKeyword(keyword);
        }else{
            //直接查询全部角色列表
            list = shRolesMapper.findAll();
        }

        if(list.size() > 0 && pageSize != null && pageIndex != null){
            //实现分页的序号功能 并且转换成Map List
            resultList = PageUtils.paging(list,pageSize,pageIndex);
        }else{
            resultList = new ArrayList<>();
        }
        return resultList;
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

    @Override
    public boolean saveOrUpdateRole(RoleVo roleVo) throws Exception {
        if(!StringUtils.hasLength(roleVo.getId())){
            //新增
            ShRoles shRoles = (ShRoles)BeanUtils.toBean(roleVo, ShRoles.class);
            int insert = shRolesMapper.insert(shRoles);
            if(insert > 0){
                //开始保存角色对应的权限关系
                if(StringUtils.hasLength(roleVo.getHasPermissionIds())){
                    String[] permissionIdsArr = roleVo.getHasPermissionIds().split(",");
                    for (String s:permissionIdsArr) {
                        ShRolePermission shRolePermission = new ShRolePermission();
                        shRolePermission.setPermissionId(s);
                        shRolePermission.setRoleId(shRoles.getId());
                        shRolePermission.setEnableFlag(SuperConstant.YES);
                        shRolePermissionMapper.insert(shRolePermission);
                    }
                }
                return true;
            }
        }else{
            //修改
            ShRoles shRoles = (ShRoles)BeanUtils.toBean(roleVo, ShRoles.class);
            int update = shRolesMapper.updateById(shRoles);
            if(update > 0){
                //开始更新角色对应的权限关系
               if(StringUtils.hasLength(roleVo.getHasPermissionIds())) {
                   //删除之前当前角色的权限
                   Map<String, Object> roleIdMap = new HashMap<>();
                   roleIdMap.put("roleId",shRoles.getId());
                   int deleteRoleHasPermissions = shRolePermissionMapper.deleteByMap(roleIdMap);
                   //保存角色的权限
                   List<String> permissionIdsArr = Arrays.asList(roleVo.getHasPermissionIds().split(","));//当前vo传过来的权限ID数组
                   for (String s:permissionIdsArr ) {
                       ShRolePermission shRolePermissionToSave = new ShRolePermission();
                       shRolePermissionToSave.setPermissionId(s);
                       shRolePermissionToSave.setRoleId(shRoles.getId());
                       shRolePermissionToSave.setEnableFlag(SuperConstant.YES);
                       shRolePermissionMapper.insert(shRolePermissionToSave);
                   }
                }
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean saveRoleHasPermissions(String roleId, List<String> permissionIdsList) throws Exception {

        //删除之前当前角色的权限
        Map<String, Object> roleIdMap = new HashMap<>();
        roleIdMap.put("roleId",roleId);
        int deleteRoleHasPermissions = shRolePermissionMapper.deleteByMap(roleIdMap);
        //保存角色的权限
       for (String s:permissionIdsList ) {
            ShRolePermission shRolePermissionToSave = new ShRolePermission();
            shRolePermissionToSave.setPermissionId(s);
            shRolePermissionToSave.setRoleId(roleId);
            shRolePermissionToSave.setEnableFlag(SuperConstant.YES);
            shRolePermissionMapper.insert(shRolePermissionToSave);
        }
        return true;
    }

    @Override
    public boolean updateByIds(List<String> ids,String flag) throws Exception {
        int update = shRolesMapper.updateEnableFlagByIds(ids, flag);
        if(update > 0){
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteRole(RoleVo roleVo) throws Exception {
        //此处还未考虑哪些情况下用户无法删除
        if("admin".equals(roleVo.getRoleName())){
            throw new RuntimeException("管理员角色无法进行删除操作");
        }

        //删除之前当前角色的权限①
        Map<String, Object> roleIdMap = new HashMap<>();
        roleIdMap.put("roleId",roleVo.getId());

        ShRoles shRoles = (ShRoles)BeanUtils.toBean(roleVo, ShRoles.class);
        int delete = shRolesMapper.deleteById(shRoles);
        if(delete > 0){
            //删除之前当前角色的权限②
            int deleteRoleHasPermissions = shRolePermissionMapper.deleteByMap(roleIdMap);
            return true;
        }
        return false;
    }


}
