package com.my.shirospringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.shirospringboot.pojo.ShUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Gzy
 * @version 1.0
 * @Description: 用户角色关系实体 数据操作
 */
@Component
@Mapper
public interface ShUserRoleMapper extends BaseMapper<ShUserRole> {

    @Select("select * from sh_user_role where userId = #{userId} ")
    List<ShUserRole> findRoleSByUserId(String userId);



}
