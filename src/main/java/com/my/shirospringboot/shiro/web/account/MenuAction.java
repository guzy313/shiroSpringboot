package com.my.shirospringboot.shiro.web.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Gzy
 * @version 1.0
 * @Description: 资源控制器
 */
@Controller
@RequestMapping("/menus")
public class MenuAction {
    private static final Logger log = LoggerFactory.getLogger(MenuAction.class);


    @RequestMapping("/system")
    public ModelAndView menu(){
        return new ModelAndView("home");
    }


}
