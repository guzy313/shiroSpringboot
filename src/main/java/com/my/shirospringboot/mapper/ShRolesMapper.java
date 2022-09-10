package com.my.shirospringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.shirospringboot.pojo.ShRoles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description shiro角色类 Mapper
 */
@Mapper
public interface ShRolesMapper extends BaseMapper<ShRoles> {

    @Select("select a.* from sh_roles a left join sh_user_role b on a.id = b.roleId " +
            "where b.userId = #{userId} ")
    List<ShRoles> findRolesByUserId(String userId);


}
