package com.my.shirospringboot.shiro.web.account;

import com.alibaba.fastjson.JSON;
import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.service.impl.LoginServiceImpl;
import com.my.shirospringboot.shiro.service.impl.PermissionServiceImpl;
import com.my.shirospringboot.shiro.service.impl.RoleServiceImpl;
import com.my.shirospringboot.shiro.vo.RoleVo;
import com.my.shirospringboot.shiro.vo.UserVo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Controller
@RequestMapping("/role")
public class RoleAction {
    private static final Logger log = LoggerFactory.getLogger(RoleAction.class);
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private PermissionServiceImpl permissionService;

    /**
     * @Description: 跳转到角色初始化页面 需要SuperAdmin 或者 MangerRole角色才能操作
     * @return
     */
    @RequestMapping("/listInitialize")
//    @RequiresRoles(value = {"SuperAdmin","MangerRole"},logical = Logical.OR) //暂时注掉 TODO
    public ModelAndView listInitialize(){
        return new ModelAndView("roleManager");
    }



    /**
     * @Description: 角色的分页查询
     * @param request
     * @param pageSize
     * @param pageIndex
     * @return map
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public ModelMap list(HttpServletRequest request, Integer pageSize, Integer pageIndex){
        String id = request.getParameter("id");
        String keyword = request.getParameter("keyword");
        RoleVo roleVo = new RoleVo();//防止空指针
        roleVo.setId(id);
        try {
            List<Map<String,Object>> list =  roleService.findRolesList(roleVo,pageSize,pageIndex,keyword);
            Long total = roleService.countRolesList(roleVo);
            ModelMap modelMap = new ModelMap();
            modelMap.addAttribute("data",list);
            modelMap.addAttribute("total",total);
            modelMap.addAttribute("success",true);
            return modelMap;
        }catch (Exception e){
            log.error("查询角色错误",e.getMessage());
            throw new RuntimeException("查询角色错误");
        }
    }

    /**
     * @Description: 通过角色ID查询角色权限
     * @param id
     * @return map
     */
    @RequestMapping("/getPermissionListByRoleId.action")
    @ResponseBody
    public ModelMap getPermissionListByRoleId(String id){
        try {
            Map<String,Object> map =  permissionService.findRoleHasPermissionsForCascade(id);
            ModelMap modelMap = new ModelMap();
            modelMap.addAttribute("data",map.get("data"));
            modelMap.addAttribute("selectedPermissionIds",map.get("selectedPermissionIds"));
            modelMap.addAttribute("halfSelectedPermissionIds",map.get("halfSelectedPermissionIds"));
            modelMap.addAttribute("success",true);
            return modelMap;
        }catch (Exception e){
            log.error("查询角色错误",e.getMessage());
            throw new RuntimeException("查询角色错误");
        }
    }


    /**
     * @Description: 打开新增页面
     * @param roleVo
     * @return
     */
    @RequestMapping("/input.action")
    public ModelAndView input(RoleVo roleVo){
        try {

            ModelAndView modelAndView = new ModelAndView("/role/role-input");
            if(!StringUtils.hasLength(roleVo.getId())){//修改情况
                //查询角色拥有的权限列表
                List<ShPermission> roleHasPermissions = permissionService.findRoleHasPermissions(roleVo.getId());
                //拼接多个权限id字符串
                StringBuffer permissionIds = new StringBuffer();
                for (int i = 0; i < roleHasPermissions.size(); i++) {
                    ShPermission shPermission = roleHasPermissions.get(i);
                    permissionIds.append(shPermission.getId());
                    if(i != roleHasPermissions.size() - 1){
                        permissionIds.append(",");
                    }
                }
                //将多个权限id返回用以回显
                roleVo.setHasPermissionIds(permissionIds.toString());
            }
            //将回显数据放入用以返回
            modelAndView.addObject("role",roleVo);
            return modelAndView;
        }catch (Exception e){
            log.error("新增页面查询错误",e.getMessage());
            throw new RuntimeException("新增页面查询错误");
        }
    }

    /**
     * @Description: 保存角色 以及对应关联的权限信息
     * @param request
     * @return
     */
    @RequestMapping("/save.action")
    @ResponseBody
    public Boolean save(HttpServletRequest request){
        String roleVoJsonStr = request.getParameter("roleVo");
        RoleVo roleVo = JSON.parseObject(roleVoJsonStr,RoleVo.class);
        try {
            return roleService.saveOrUpdateRole(roleVo);
        }catch (Exception e){
            log.error("保存失败",e.getMessage());
            return false;
        }
    }

    /**
     * @Description: 保存角色权限
     * @param request
     * @return
     */
    @RequestMapping("/saveRoleHasPermissions.action")
    @ResponseBody
    public Boolean saveRoleHasPermissions(HttpServletRequest request){
        String roleId = request.getParameter("roleId");
        String permissionIds = request.getParameter("permissionIds");
        List<String> permissionIdsList
                = Arrays.asList(permissionIds.split(","));
        try {
            return roleService.saveRoleHasPermissions(roleId,permissionIdsList);
        }catch (Exception e){
            log.error("保存失败",e.getMessage());
            return false;
        }
    }

    /**
     * @Description: 角色启用
     * @param ids
     * @return
     */
    @RequestMapping("/start.action")
    @ResponseBody
    public String start(String ids){
        String[] idArray = ids.split(",");
        List<String> list = new ArrayList<>();
        for (String s:idArray) {
            list.add(s);
        }
       try {
           Boolean flag = roleService.updateByIds(list, SuperConstant.YES);
           if(flag){
               return "启用成功";
           }
       }catch (Exception e){
            log.error("启用失败",e.getMessage());
       }
       return "启用失败";
    }



    /**
     * @Description: 删除角色信息
     * @param
     * @return boolean
     */
    @RequestMapping("/delete.action")
    @ResponseBody
    public Boolean delete(HttpServletRequest request){
        String id = request.getParameter("id");
        if(!com.my.shirospringboot.utils.StringUtils.hasLength(id)){
            throw new RuntimeException("id不能为空");
        }
        RoleVo roleVo = new RoleVo();
        roleVo.setId(id);
        try {
            return roleService.deleteRole(roleVo);
        }catch (Exception e){
            log.error("删除失败",e.getMessage());
            return false;
        }
    }



}
