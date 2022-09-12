package com.my.shirospringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.shirospringboot.pojo.ShUsers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Description shiro用户类Mapper
 */
@Mapper
public interface ShUsersMapper extends BaseMapper<ShUsers> {

    @Select("select * from sh_users where loginName = #{loginName} ")
    List<ShUsers> findShUserByName(String loginName);

    @Update("update sh_users set password = #{password} where id = #{id} ")
    Boolean updatePassword(String id,String password);

}
