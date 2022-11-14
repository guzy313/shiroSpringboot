package com.my.shirospringboot.shiro.web.account;

import com.alibaba.fastjson.JSONObject;
import com.my.shirospringboot.shiro.constant.CacheConstant;
import com.my.shirospringboot.shiro.core.base.BaseResponse;
import com.my.shirospringboot.shiro.service.UserService;
import com.my.shirospringboot.shiro.service.impl.LoginServiceImpl;
import com.my.shirospringboot.shiro.service.impl.UserServiceImpl;
import com.my.shirospringboot.shiro.utils.SecurityUtils;
import com.my.shirospringboot.shiro.vo.LoginVo;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.annotation.Resource;
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
     * @Descrition: jwt登录
     * @param loginVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/jwtLogin")
    public String jwtLogin(LoginVo loginVo){
        BaseResponse baseResponse = loginService.jwtRoute(loginVo);
        return JSONObject.toJSONString(baseResponse);
    }


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
        System.out.println("是否认证:"+SecurityUtils.getSubject().isAuthenticated());
        modelAndView.setViewName("redirect:/menus/system");
        long timeout = SecurityUtils.getSubject().getSession().getTimeout();
        System.out.println("会话失效时间:"+timeout);
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
            String queueKey = CacheConstant.ACTIVE_SESSIONID_QUEUE_PREFIX
                    + SecurityUtils.getShiroUser().getLoginName();
            //从redis缓存的当前用户同时在线队列中删除当前用户的sessionID
            String sessionId = SecurityUtils.getShiroSessionId();
            subject.logout();
            userService.deleteUserRedisQueue(queueKey,sessionId);
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
