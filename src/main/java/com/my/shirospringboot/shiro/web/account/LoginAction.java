package com.my.shirospringboot.shiro.web.account;

import com.my.shirospringboot.shiro.service.UserService;
import com.my.shirospringboot.shiro.service.impl.LoginServiceImpl;
import com.my.shirospringboot.shiro.service.impl.UserServiceImpl;
import com.my.shirospringboot.shiro.vo.LoginVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gzy
 * @version 1.0
 * @Description 登录控制器
 */
@RequestMapping("/login")
@Controller
public class LoginAction {
    private static final Logger log = LoggerFactory.getLogger(LoginAction.class);
    @Autowired
    private LoginServiceImpl loginService;
    @Autowired
    private UserServiceImpl userService;




    /**
     * @Description 登录方法
     * @param loginVo
     * @return
     */
    @RequestMapping("/usersLogin")
    public ModelAndView usersLogin(LoginVo loginVo){
        ModelAndView modelAndView = new ModelAndView("login");
        String shiroLoginFailure = null;
        Map<String,String> map = new HashMap<>();
        try {
//            loginVo.setSystemCode();
            loginService.route(loginVo);
        }catch (UnknownAccountException e){
            log.error("登录异常:{}",e);
            shiroLoginFailure = "登录失败,账号不存在!";
            map.put("loginName",loginVo.getLoginName());
            map.put("shiroLoginFailure",shiroLoginFailure);
            modelAndView.addAllObjects(map);
        }catch (IncorrectCredentialsException e){
            log.error("登录异常:{}",e);
            shiroLoginFailure = "登录失败,密码不正确!";
            map.put("loginName",loginVo.getLoginName());
            map.put("shiroLoginFailure",shiroLoginFailure);
            modelAndView.addAllObjects(map);
        }catch (Exception e){
            log.error("登录异常:{}",e);
            shiroLoginFailure = "登录失败,请联系管理员!";
            map.put("loginName",loginVo.getLoginName());
            map.put("shiroLoginFailure",shiroLoginFailure);
            modelAndView.addAllObjects(map);
        }
        if(shiroLoginFailure!=null){
            log.info("登录失败");
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/menus/system");
        return modelAndView;
    }

    /**
     * @Description 登出方法
     * @return 登录页面跳转
     */
    @RequestMapping("/usersLoginOut.action")
    public ModelAndView usersLoginOut(){
        ModelAndView modelAndView = new ModelAndView("login");
        Subject subject = SecurityUtils.getSubject();
        if(subject != null){
            subject.logout();
            modelAndView.addObject("message","登出成功");
        }
        return modelAndView;
    }

    /**
     * @Description 编辑密码页面
     */
    @RequestMapping("/editPassword")
    public ModelAndView editPassword(){
        return new ModelAndView("user/user-editor-password");
    }

    /**
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping("/saveNewPassword")
    @ResponseBody
    public Boolean saveNewPassword(String oldPassword,String newPassword) throws Exception {
        return userService.saveNewPassword(oldPassword,newPassword);
    }

}
