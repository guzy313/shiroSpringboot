package com.my.shirospringboot.shiro.web.account;

import com.alibaba.fastjson.JSON;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.shiro.service.UserService;
import com.my.shirospringboot.shiro.service.impl.UserServiceImpl;
import com.my.shirospringboot.shiro.vo.RoleVo;
import com.my.shirospringboot.shiro.vo.UserRoleVo;
import com.my.shirospringboot.shiro.vo.UserVo;
import com.my.shirospringboot.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Controller
@RequestMapping("/user")
public class UserAction {
    private static final Logger log = LoggerFactory.getLogger(UserAction.class);
    @Autowired
    private UserServiceImpl userService;

    /**
     * @description: 用户列表页面
     * @return
     */
    @RequestMapping("/listInitialize")
    public String listInitialize(){
        return "userManager";
    }


    /**
     * @Description: 用户的分页查询
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
        UserVo userVo = new UserVo();//防止空指针
        userVo.setId(id);
        try {
            List<Map<String,Object>> list =  userService.findUserList(userVo,pageSize,pageIndex,keyword);
            Long total = userService.countUserList(userVo);
            ModelMap modelMap = new ModelMap();
            modelMap.addAttribute("data",list);
            modelMap.addAttribute("total",total);
            modelMap.addAttribute("success",true);
            return modelMap;
        }catch (Exception e){
            log.error("查询用户错误",e.getMessage());
            throw new RuntimeException("查询用户错误");
        }
    }

    /**
     * @Description: 新增修改用户信息
     * @param
     * @return boolean
     */
    @RequestMapping("/save.action")
    @ResponseBody
    public Boolean save(HttpServletRequest request){
        String userVoJsonStr = request.getParameter("userVo");
        UserVo userVo = JSON.parseObject(userVoJsonStr,UserVo.class);
        try {
            return userService.saveOrUpdateUser(userVo);
        }catch (Exception e){
            log.error("保存失败",e.getMessage());
            return false;
        }
    }


//    /**
//     * @Description: 新增修改用户信息
//     * @param
//     * @return boolean
//     */
//    @RequestMapping("/save.action")
//    @ResponseBody
//    public Boolean save(@RequestBody UserVo userVo){
//
//        try {
//            return userService.saveOrUpdateUser(userVo);
//        }catch (Exception e){
//            log.error("保存失败",e.getMessage());
//            return false;
//        }
//    }


    /**
     * @Description: 保存 用户分配角色
     * @param
     * @return boolean
     */
    @RequestMapping("/saveDispatchRoles.action")
    @ResponseBody
    public Boolean saveDispatchRoles(HttpServletRequest request){
        String userId = request.getParameter("userId");
        String roleIds = request.getParameter("roleIds");
        UserRoleVo userRoleVo = new UserRoleVo();
        userRoleVo.setUserId(userId);
        userRoleVo.setRoleIds(roleIds);
        try {
            return userService.saveDispatchRoles(userRoleVo);
        }catch (Exception e){
            log.error("用户分配角色失败",e.getMessage());
            return false;
        }
    }


    /**
     * @Description: 删除用户信息
     * @param
     * @return boolean
     */
    @RequestMapping("/delete.action")
    @ResponseBody
    public Boolean delete(HttpServletRequest request){
        String id = request.getParameter("id");
        if(!StringUtils.hasLength(id)){
            throw new RuntimeException("id不能为空");
        }
        UserVo userVo = new UserVo();
        userVo.setId(id);
        try {
            return userService.deleteUser(userVo);
        }catch (Exception e){
            log.error("删除失败",e.getMessage());
            return false;
        }
    }

    /**
     * @Description: 验证用户名唯一
     * @param
     * @return boolean
     */
    @RequestMapping("/checkLoginName")
    @ResponseBody
    public Boolean checkLoginName(HttpServletRequest request){
        String userVoJsonStr = request.getParameter("userVo");
        UserVo userVo = JSON.parseObject(userVoJsonStr,UserVo.class);
        try {
            return userService.checkLoginName(userVo);
        }catch (Exception e){
            log.error("验证失败",e.getMessage());
            return false;
        }
    }

}
