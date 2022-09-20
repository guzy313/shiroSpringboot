package com.my.shirospringboot.shiro.web.account;

import com.my.shirospringboot.mapper.ShPermissionMapper;
import com.my.shirospringboot.pojo.ShPermission;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.service.impl.PermissionServiceImpl;
import com.my.shirospringboot.shiro.vo.PermissionVo;
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

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description 权限控制器
 */
@Controller
@RequestMapping("/permission")
public class PermissionAction {
    private static final Logger log = LoggerFactory.getLogger(PermissionAction.class);
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
    public String listInitialize(){
        return "/permission/permission-listInitialize";
    }

    /**
     * @Description: 获取权限列表
     * @param permissionVo
     * @param rows
     * @param page
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ModelMap list(PermissionVo permissionVo, Integer rows, Integer page){
       try {
           List<ShPermission> list = permissionService.findPermissionList(permissionVo,rows,page);
           Long total = permissionService.countPermissionList(permissionVo);
           ModelMap modelMap = new ModelMap();
           modelMap.addAttribute("data",list);
           modelMap.addAttribute("total",total);
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
    @RequestMapping("/save")
    @ResponseBody
    public boolean save(@ModelAttribute("permission")ShPermission shPermission){
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
    @RequestMapping("/delete")
    @ResponseBody
    public boolean delete(@ModelAttribute("permission")ShPermission shPermission){
        try {
            if(permissionService.findPermissionListByParentId(shPermission.getId()).size() > 0){
                //如果被删除的节点有子节点，则不允许删除
                return false;
            }
            return permissionService.deletePermission(shPermission);
        }catch (Exception e){
            log.error("删除失败",e.getMessage());
            return false;
        }
    }


}
