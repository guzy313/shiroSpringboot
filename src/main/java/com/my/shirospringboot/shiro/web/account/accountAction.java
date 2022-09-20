package com.my.shirospringboot.shiro.web.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Gzy
 * @version 1.0
 * @Description: 账号控制器
 */
@RequestMapping("/account")
@Controller
public class accountAction {
    private static final Logger log = LoggerFactory.getLogger(accountAction.class);
    @RequestMapping("/login")
    public String loginPage(){
        log.info("登录页面");
        return "login";
    }

}
