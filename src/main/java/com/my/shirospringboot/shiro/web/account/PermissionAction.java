package com.my.shirospringboot.shiro.web.account;

import com.my.shirospringboot.mapper.ShPermissionMapper;
import com.my.shirospringboot.pojo.ShPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 权限控制器
 */
@Controller
@RequestMapping("/permissionAction")
public class PermissionAction {
    @Autowired
    private ShPermissionMapper shPermissionMapper;

    //自定义格式转换器
    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder)throws Exception{
        //去掉空格
        binder.registerCustomEditor(String.class,new StringTrimmerEditor(false));
    }

    /**
     * @description: 初始化列表
     * @return
     */
    @RequestMapping("/listInitialize")
    public String listInitialize(){
        return "resource/resource-listInitialize";
    }

    @RequestMapping("/list")
    @ResponseBody
    public ModelMap list(ShPermission permission,Integer rows,Integer page){
//        List<ShPermission> list =
        //TODO
        return new ModelMap();
    }

}
