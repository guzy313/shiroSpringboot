package com.my.shirospringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.shirospringboot.pojo.ShUsers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description shiro用户类Mapper
 */
@Component
@Mapper
public interface ShUsersMapper extends BaseMapper<ShUsers> {

    @Select("select * from sh_users ")
    List<ShUsers> findAll();

    @Select("select * from sh_users where id = #{id} ")
    List<ShUsers> findUserById(String id);

    @Select("select * from sh_users where loginName = #{loginName} ")
    List<ShUsers> findShUserByName(String loginName);

    @Update("update sh_users set password = #{password} where id = #{id} ")
    Boolean updatePassword(String id,String password);

}
