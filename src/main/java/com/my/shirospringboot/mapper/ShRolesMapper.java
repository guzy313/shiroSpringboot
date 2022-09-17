package com.my.shirospringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.shirospringboot.pojo.ShRoles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description shiro角色类 Mapper
 */
@Mapper
@Component//作用是去掉idea的自动注入报红检查，无实际意义
public interface ShRolesMapper extends BaseMapper<ShRoles> {

    /**
     * @Description: 查询所有角色
     * @return
     */
    @Select("select * from sh_roles ")
    List<ShRoles> findAll();

    /**
     * @Description: 查询某个用户拥有的所有角色
     * @return
     */
    @Select("select a.* from sh_roles a left join sh_user_role b on a.id = b.roleId " +
            "where b.userId = #{userId} ")
    List<ShRoles> findRolesByUserId(String userId);

    /**
     * @Description: 通过角色id集合更新多个角色的启用状态
     * @param ids
     * @param enableFlag
     * @return int 操作的数据行数
     */
    @Update("<script>" +
            "update sh_roles set enableFlag=#{enableFlag} where id in " +
            "<foreach item='id' index='index' collection='ids' open='(' separator=',' close=')' >" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int updateEnableFlagByIds(List<String> ids,String enableFlag);

    /**
     * @Description: 通过角色id集合查询对应的角色
     * @param ids
     * @return List<ShRoles>
     */
    @Select("<script>" +
            "select * from sh_roles where id in " +
            "<foreach item='id' index='index' collection='ids' open='(' separator=',' close=')' >" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<ShRoles> getShRolesByIds(List<String> ids);

}
