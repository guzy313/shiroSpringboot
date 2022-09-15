package com.my.shirospringboot.shiro.web.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Controller
@RequestMapping("/userAction")
public class UserAction {

    /**
     * @description: 角色列表页面
     * @return
     */
    @RequestMapping("/listInitialize")
    public String listInitialize(){
        return "/user/user-listInitialize";
    }


}
