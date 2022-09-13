package com.my.shirospringboot.shiro.web.account;

import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.shiro.service.impl.LoginServiceImpl;
import com.my.shirospringboot.shiro.service.impl.RoleServiceImpl;
import com.my.shirospringboot.shiro.vo.RoleVo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Controller
@RequestMapping("/roleAction")
public class RoleAction {
    private static final Logger log = LoggerFactory.getLogger(RoleAction.class);
    @Autowired
    private RoleServiceImpl roleService;

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

}
