package com.my.shirospringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.shirospringboot.pojo.ShRoles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description shiro角色类 Mapper
 */
@Mapper
@Component//作用是去掉idea的自动注入报红检查，无实际意义
public interface ShRolesMapper extends BaseMapper<ShRoles> {

    @Select("select * from sh_roles ")
    List<ShRoles> findAll();

    @Select("select a.* from sh_roles a left join sh_user_role b on a.id = b.roleId " +
            "where b.userId = #{userId} ")
    List<ShRoles> findRolesByUserId(String userId);


}
