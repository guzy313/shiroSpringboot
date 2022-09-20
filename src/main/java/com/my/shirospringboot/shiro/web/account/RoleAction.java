package com.my.shirospringboot.shiro.web.account;

import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.service.impl.LoginServiceImpl;
import com.my.shirospringboot.shiro.service.impl.PermissionServiceImpl;
import com.my.shirospringboot.shiro.service.impl.RoleServiceImpl;
import com.my.shirospringboot.shiro.vo.RoleVo;
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

import java.util.ArrayList;
import java.util.List;

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
    @RequiresRoles(value = {"SuperAdmin","MangerRole"},logical = Logical.OR)
    public ModelAndView listInitialize(){
        return new ModelAndView("role/role-listInitialize");
    }

    /**
     * @Description: 角色的分页查询
     * @param roleVo
     * @param rows
     * @param page
     * @return map
     */
    @RequestMapping("/list")
    @ResponseBody
    public ModelMap list(RoleVo roleVo,Integer rows,Integer page){
        try {
            List<ShRoles> list =  roleService.findRolesList(roleVo,rows,page);
            Long total = roleService.countRolesList(roleVo);
            ModelMap modelMap = new ModelMap();
            modelMap.addAttribute("data",list);
            modelMap.addAttribute("total",total);
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
    @RequestMapping("/input")
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
     * @param roleVo
     * @return
     */
    @RequestMapping("/save")
    public Boolean save(@ModelAttribute("role")RoleVo roleVo){
        try {
            return roleService.saveOrUpdateRole(roleVo);
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
    @RequestMapping("/start")
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







}
