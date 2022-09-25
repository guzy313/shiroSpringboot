package com.my.shirospringboot.shiro.web.account;

import com.alibaba.fastjson.JSON;
import com.my.shirospringboot.pojo.ShRoles;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.shiro.service.UserService;
import com.my.shirospringboot.shiro.service.impl.UserServiceImpl;
import com.my.shirospringboot.shiro.vo.RoleVo;
import com.my.shirospringboot.shiro.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * @param userVo
     * @param rows
     * @param page
     * @return map
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public ModelMap list(UserVo userVo, Integer rows, Integer page){
        try {
            List<UserVo> list =  userService.findUserList(userVo,rows,page);
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
//    @ResponseBody
//    @RequestMapping(value = "save.action", produces = "application/json;charset=UTF-8")
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
     * @Description: 删除用户信息
     * @param
     * @return boolean
     */
    @RequestMapping("/delete.action")
    @ResponseBody
    public Boolean delete(HttpServletRequest request){
        String userVoJsonStr = request.getParameter("userVo");
        UserVo userVo = JSON.parseObject(userVoJsonStr,UserVo.class);
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
