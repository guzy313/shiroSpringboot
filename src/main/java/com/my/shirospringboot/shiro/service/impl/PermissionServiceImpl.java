package com.my.shirospringboot.shiro.service.impl;

import com.my.shirospringboot.mapper.ShPermissionMapper;
import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.service.PermissionService;
import com.my.shirospringboot.shiro.vo.PermissionVo;
import com.my.shirospringboot.utils.BeanUtils;
import com.my.shirospringboot.utils.PageUtils;
import com.my.shirospringboot.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Map<String, Object> findPermissionsForCascade() throws Exception {
        Map<String,Object> resultMap = new HashMap<>();

        List<ShPermission> listAll = shPermissionMapper.findAll();
        List<PermissionVo> listVo = new ArrayList<>();
        for (ShPermission p:listAll ) {
            PermissionVo permissionVo = new PermissionVo();
            BeanUtils.copyPropertiesIgnoreNull(p,permissionVo);
            listVo.add(permissionVo);
        }

        //最终结果
        List<Map<String,Object>> resultList = new ArrayList<>();
        for (PermissionVo p:listVo ) {
            if(p.getId().length() == 3){
                //取出1级菜单
                Map<String,Object> map = new HashMap<>();
                map.put("label",p.getPermissionName());
                map.put("id",p.getId());
                if(getTree(listVo,p.getId()).size() > 0){
                    map.put("children",getTree(listVo,p.getId()));
                }
                resultList.add(map);
            }
        }
        //最终结果
        resultMap.put("data",resultList);
        return resultMap;
    }


    @Override
    public List<ShPermission> findPermissionList(PermissionVo shPermissionVo, Integer pageSize, Integer pageIndex) {
        //分页结果集合对象
        List<Map<String,Object>> resultList = null;
        //查询结果集合对象
        List<ShPermission> list = null;
        //查询条件集合
        Map<String,Object> conditionMap = new HashMap<>();

        //通过权限ID查询对应权限
        if(StringUtils.hasLength(shPermissionVo.getId())){
            conditionMap.put("id",shPermissionVo.getId());
            list = shPermissionMapper.selectByMap(conditionMap);
            //转map集合
            resultList = BeanUtils.objectListToMapList(list);
            return list;
        }

        //直接查询全部权限列表
        list = shPermissionMapper.findAll();

//        if(list.size() > 0 && pageSize != null && pageIndex != null){
//            //实现分页的序号功能 并且转换成Map List
//            resultList = PageUtils.paging(list,pageSize,pageIndex);
//        }else{
//            resultList = new ArrayList<>();
//        }
        return list;
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


        //半选中权限id(即有子集并且子集没有全选中至少选中一个——解决elementUI tree回显问题)
        List<String> halfSelectedPermissionIds = new ArrayList<>();//半选父节点下面的子节点集合

        //一个节点存在子节点则不会单独选中父节点，一定是选中了某个子节点才会选中父节点，一个节点没有子节点，选中则选中本身
        //elementui tree中：选中子节点会自动联动半选父节点,如果子节点全部选中,则会自动选中父节点
        //所以回显数组只需要返回最下面的子节点就行
        for (ShPermission s:list) {
            boolean hasChildren = false;
            for (ShPermission all:listAll) {
                if(s.getId().equals(all.getParentId())){
                    hasChildren = true;
                }
            }
            if(!hasChildren){//如果没有子节点，则选中父节点
                halfSelectedPermissionIds.add(s.getId());
            }
        }


        //最终结果
        resultMap.put("data",resultList);
        //选中权限id-用来给前端选中节点数组初始化
        resultMap.put("selectedPermissionIds",selectedPermissionIds);
        //半选中权限id(即有子集并且子集没有全选中至少选中一个——解决elementUI tree回显问题)
        resultMap.put("halfSelectedPermissionIds",halfSelectedPermissionIds);

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
            shPermission.setId(this.generatePermissionId(shPermission.getParentId()));
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
        //系统权限不允许删除
        for (String s: SuperConstant.SYSTEM_PERMISSION_IDS ) {
            if(s.equals(shPermission.getId())){
                return false;
            }
        }

        if(StringUtils.hasLength(shPermission.getId())){
            int delete = shPermissionMapper.deleteById(shPermission.getId());
            if(delete > 0){
                return true;
            }
        }
        return false;
    }

    /**
     * 生成权限ID方法
     * @param parentId
     * @return
     */
    public String generatePermissionId(String parentId){
        List<ShPermission> all = shPermissionMapper.findAll();
        int max = 0;
        if(StringUtils.isNotEmpty(parentId)){
            //新建子级的情况

            for (ShPermission s:all) {
                if(parentId.equals(s.getParentId())){//遍历父级ID相同的权限，用来取最大权限
                    if(max < Integer.parseInt(s.getId())){
                        max = Integer.parseInt(s.getId());
                    }
                }
            }
        }else{
            //无父级情况-生成最高级
            for (ShPermission s:all) {
                if(StringUtils.isEmpty(s.getParentId())){//遍历父级ID相同的权限，用来取权限ID的最大值
                    if(max < Integer.parseInt(s.getId())){
                        max = Integer.parseInt(s.getId());
                    }
                }
            }
        }
        max ++;
        StringBuffer permissionId = new StringBuffer();
        int len = 3 - Integer.toString(max).length();
        for (int i = 0; i < len; i++) {
            permissionId.append("0");
        }
        permissionId.append(Integer.toString(max));

        return permissionId.toString();
    }


}
