package com.my.shirospringboot.shiro.service.impl;

import com.my.shirospringboot.mapper.ShUsersMapper;
import com.my.shirospringboot.pojo.ShUsers;
import com.my.shirospringboot.shiro.constant.SuperConstant;
import com.my.shirospringboot.shiro.service.UserService;
import com.my.shirospringboot.shiro.utils.SecurityUtils;
import com.my.shirospringboot.shiro.vo.UserVo;
import com.my.shirospringboot.utils.BeanUtils;
import com.my.shirospringboot.utils.DigestUtil;
import com.my.shirospringboot.utils.PageUtils;
import com.my.shirospringboot.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gzy
 * @version 1.0
 * @Description 用户接口实现
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private ShUsersMapper shUsersMapper;


    @Override
    public Boolean saveNewPassword(String oldPassword,String newPassword) throws Exception {
        ShUsers shUsers = SecurityUtils.getShUsers();
        //对用户输入的原密码进行散列加密（用以比对数据库原密码）
        Map<String, String> oldPwdMap = DigestUtil.entryptPassword(oldPassword);
        if(shUsers.getPassword().equals(oldPwdMap.get("password"))){//登录对象的密码与加密后的原密码进行比对
            log.error("原密码不正确");
            return false;
        }
        //构建新密码保存 用户对象
        UserVo userVo = new UserVo();
        userVo.setPassword(newPassword);
        //对新密码进行散列加密
        this.entryptPassword(userVo);
        try {
            shUsers.setPassword(userVo.getPassword());
            shUsers.setSalt(userVo.getSalt());
            shUsersMapper.updateById(userVo);
            return true;
        }catch (Exception e){
            log.error("密码更新失败:{}",e.getMessage());
            return false;
        }
    }

    @Override
    public List<Map<String,Object>> findUserList(UserVo userVo, Integer pageSize, Integer pageIndex,String keyword) throws Exception {
        List<Map<String,Object>> resultList = null;
        List<ShUsers> list = null;
        //通过用户查询对应角色
        if(StringUtils.hasLength(userVo.getId())){
            list = shUsersMapper.findUserById(userVo.getId());
            //转map集合
            resultList = BeanUtils.objectListToMapList(list);
            return resultList;
        }else{
            //直接查询全部角色列表
            if(StringUtils.hasLength(keyword)){
                list = shUsersMapper.findByName(keyword);
            }else{
                list = shUsersMapper.findAll();
            }
            if(list.size() > 0){
                //实现分页的序号功能
                resultList = PageUtils.paging(list,pageSize,pageIndex);
            }
            return resultList;
        }




    }

    @Override
    public Long countUserList(UserVo userVo) throws Exception {
        //通过用户查询对应角色
        if(StringUtils.hasLength(userVo.getId())){
            List<ShUsers> list = shUsersMapper.findUserById(userVo.getId());
            return Long.parseLong(String.valueOf(list.size()));
        }
        //直接查询全部角色列表
        List<ShUsers> list = shUsersMapper.findAll();
        return Long.parseLong(String.valueOf(list.size()));
    }


    @Override
    public Boolean saveOrUpdateUser(UserVo userVo)throws Exception{

        if(!StringUtils.hasLength(userVo.getId())){
            entryptPassword(userVo);//散列加密密码 用以获取散列之后的密文密码以及salt保存
            userVo.setEnableFlag(SuperConstant.YES);//启用用户
            ShUsers shUsers = (ShUsers) BeanUtils.toBean(userVo,ShUsers.class);
            //新增
            int insert = shUsersMapper.insert(shUsers);
            if(insert > 0){
                return true;
            }
            return false;
        }else{
            ShUsers shUsers = (ShUsers) BeanUtils.toBean(userVo,ShUsers.class);
            //修改
            int update = shUsersMapper.updateById(shUsers);
            if(update > 0){
                return true;
            }
            return false;
        }
    }


    /**
     * @Description 对用户视图对象的密码进行散列加密
     * @param userVo
     */
    private void entryptPassword(UserVo userVo){
        Map<String, String> newPwdMap = DigestUtil.entryptPassword(userVo.getPassword());
        userVo.setPassword(newPwdMap.get("password"));
        userVo.setSalt(newPwdMap.get("salt"));
    }


    @Override
    public Boolean deleteUser(UserVo userVo) throws Exception {
        //此处还未考虑哪些情况下用户无法删除
        if("admin".equals(userVo.getLoginName())){
            throw new RuntimeException("管理员无法进行删除操作");
        }
        ShUsers shUsers = (ShUsers)BeanUtils.toBean(userVo, ShUsers.class);
        int delete = shUsersMapper.deleteById(shUsers);
        if(delete > 0){
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkLoginName(UserVo userVo) throws Exception {
        Map<String,Object> columnMap = new HashMap<>();
        if(!StringUtils.hasLength(userVo.getLoginName())){
            throw new RuntimeException("用户名不能为空");
        }
        columnMap.put("loginName",userVo.getLoginName());
        List<ShUsers> shUsersList = shUsersMapper.selectByMap(columnMap);
        if(shUsersList.size() > 0){
            //用户名已经存在
            return true;
        }
        return false;
    }
}
