package com.my.shirospringboot.shiro.service.impl;

import com.my.shirospringboot.mapper.ShPermissionMapper;
import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.shiro.service.PermissionService;
import com.my.shirospringboot.shiro.vo.PermissionVo;
import com.my.shirospringboot.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<ShPermission> findRoleHasPermissions(String roleId) throws Exception {
        List<ShPermission> list = shPermissionMapper.findRoleHasPermissions(roleId);
        return list;
    }

    @Override
    public Map<String,Object> findRoleHasPermissionsForCascade(String roleId) throws Exception {

        Map<String,Object> resultMap = new HashMap<>();

        //选中
        List<ShPermission> list = shPermissionMapper.findRoleHasPermissions(roleId);

        List<ShPermission> listAll = shPermissionMapper.findAll();

        List<PermissionVo> listVo = new ArrayList<>();
        for (ShPermission p:listAll ) {
            PermissionVo permissionVo = new PermissionVo();
            BeanUtils.copyPropertiesIgnoreNull(p,permissionVo);
            listVo.add(permissionVo);
        }

        for (PermissionVo p:listVo ) {
            String id = p.getId();
            p.setSelected(false);
            for (ShPermission s:list ) {
                //选中
                String s_id = s.getId();
                if(id.equals(s_id)){
                   p.setSelected(true);
                }
            }
        }
        //最终结果
        List<Map<String,Object>> resultList = new ArrayList<>();
        //选中权限id
        List<String> selectedPermissionIds = new ArrayList<>();
        for (PermissionVo p:listVo ) {
            if(p.getId().length() == 3){
                //取出1级菜单
                Map<String,Object> map = new HashMap<>();
                map.put("label",p.getPermissionName());
                map.put("id",p.getId());
                map.put("selected",p.getSelected());
                if(getTree(listVo,p.getId()).size() > 0){
                    map.put("children",getTree(listVo,p.getId()));
                }
                resultList.add(map);
            }
            if(p.getSelected() == true){
                selectedPermissionIds.add(p.getId());
            }
        }
        //最终结果
        resultMap.put("data",resultList);
        //选中权限id-用来给前端选中节点数组初始化
        resultMap.put("selectedPermissionIds",selectedPermissionIds);

        return resultMap;
    }

    /**
     * 获取树状子节点
     * @param list
     * @param id
     * @return
     */
    public List<Map<String,Object>> getTree(List<PermissionVo> list,String id){
        List<Map<String,Object>> resultList = new ArrayList<>();

        for (PermissionVo p:list ) {
            if(id.equals(p.getParentId())){
                Map<String,Object> map = new HashMap<>();
                map.put("label",p.getPermissionName());
                map.put("id",p.getId());
                map.put("selected",p.getSelected());
                if(getTree(list,p.getId()).size() > 0){
                    map.put("children",getTree(list,p.getId()));
                }
                resultList.add(map);
            }
        }
        return  resultList;
    }


    @Override
    public boolean saveOrUpdatePermission(ShPermission shPermission) throws Exception {

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
