package com.my.shirospringboot.shiro.web.account;

import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.shiro.utils.SecurityUtils;
import com.my.shirospringboot.shiro.vo.PermissionVo;
import com.my.shirospringboot.utils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * @Description: 主页面菜单列表
     * @return
     */
    @RequestMapping("/menuList.action")
    @ResponseBody
    public List<ShPermission> menuList(){
        List<ShPermission> loginUserPermissions = SecurityUtils.getLoginUserPermissions();

//        //做数据处理 -处理成 {object,children:[{object},{object,children[]...}]}格式 方便vue调用
//        List<Map<String,Object>> permissionList = new ArrayList<>();
//        for (ShPermission s:loginUserPermissions ) {
//            Map<String,Object> map = new HashMap<>();
//            map.put("id",s.getId());
//            map.put("parentId",s.getParentId());
//            map.put("permissionName",s.getPermissionName());
//            map.put("url",s.getPath());
//            map.put("icon",s.getIcon());
//            permissionList.add(map);
//        }
//
//        List<Map<String,Object>> permissionListFinal = new ArrayList<>();
//        for (Map<String,Object> p:permissionList) {
//            if(String.valueOf(p.get("id")).length() == 3){//取第一级菜单
//                p.put("children",getChildren(permissionList,p));
//                permissionListFinal.add(p);
//            }
//        }
        return loginUserPermissions;
    }

    /**
     * 递归设置 children
     * @param permissionList
     * @param  permission
     * @return
     */
    public Map<String,Object> getChildren(List<Map<String,Object>> permissionList,
                                          Map<String,Object> permission){
        for (Map<String,Object> p:permissionList) {
            if(String.valueOf(permission.get("id")).equals(String.valueOf(p.get("parentId")))){
                permission.put("children",getChildren(permissionList,p));
            }
        }
        return permission;
    }



}
