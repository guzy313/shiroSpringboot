package com.my.shirospringboot.shiro.web.account;

import com.alibaba.fastjson.JSON;
import com.my.shirospringboot.mapper.ShPermissionMapper;
import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.service.impl.PermissionServiceImpl;
import com.my.shirospringboot.shiro.vo.PermissionVo;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Gzy
 * @version 1.0
 * @Description 权限控制器
 */
@Controller
@RequestMapping("/permission")
public class PermissionAction {
    private static final Logger log = LoggerFactory.getLogger(PermissionAction.class);
    @Autowired
    private PermissionServiceImpl permissionService;

    //自定义格式转换器
    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder)throws Exception{
        //去掉空格
        binder.registerCustomEditor(String.class,new StringTrimmerEditor(false));
    }

    /**
     * @description: 初始化列表页面
     * @return
     */
    @RequestMapping("/listInitialize")
    public ModelAndView listInitialize(){
        return new ModelAndView("permissionManager");
    }




    /**
     * @Description: 获取权限 树状
     * @param id
     * @return map
     */
    @RequestMapping("/getPermissionListTree.action")
    @ResponseBody
    public ModelMap getPermissionListTree(){
        try {
            Map<String,Object> map =  permissionService.findPermissionsForCascade();
            ModelMap modelMap = new ModelMap();
            modelMap.addAttribute("data",map.get("data"));
            modelMap.addAttribute("success",true);
            return modelMap;
        }catch (Exception e){
            log.error("查询权限错误",e.getMessage());
            throw new RuntimeException("查询权限错误");
        }
    }


    /**
     * @Description: 获取权限列表
     * @param permissionVo
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/list.action")
    @ResponseBody
    public ModelMap list(PermissionVo permissionVo, Integer rows, Integer page){
        //暂无分页需求，所以不做分页功能
       try {
           List<ShPermission> list = permissionService.findPermissionList(permissionVo,rows,page);
           Long total = permissionService.countPermissionList(permissionVo);
           ModelMap modelMap = new ModelMap();
           modelMap.addAttribute("data",list);
           modelMap.addAttribute("success",true);
           return modelMap;
       }catch (Exception e){
           log.error("获取权限列表失败",e.getMessage());
           throw new RuntimeException("获取权限列表失败");
       }
    }

    /**
     * @Description: 新增或者修改权限
     * @param shPermission
     * @return
     */
    @RequestMapping("/save.action")
    @ResponseBody
    public boolean save(HttpServletRequest request){
        String permissionJsonStr = request.getParameter("permission");
        ShPermission shPermission = JSON.parseObject(permissionJsonStr,ShPermission.class);
        try {
            return permissionService.saveOrUpdatePermission(shPermission);
        }catch (Exception e){
            log.error("保存失败",e.getMessage());
            return false;
        }
    }

    /**
     * @Description: 删除权限
     * @param shPermission
     * @return
     */
    @RequestMapping("/delete.action")
    @ResponseBody
    public boolean delete(HttpServletRequest request){
        String id = request.getParameter("id");
        if(!com.my.shirospringboot.utils.StringUtils.hasLength(id)){
            throw new RuntimeException("id不能为空");
        }
        ShPermission permission = new ShPermission();
        permission.setId(id);
        try {
            if(permissionService.findPermissionListByParentId(permission.getId()).size() > 0){
                //如果被删除的节点有子节点，则不允许删除
                return false;
            }
            return permissionService.deletePermission(permission);
        }catch (Exception e){
            log.error("删除失败",e.getMessage());
            return false;
        }
    }


}
