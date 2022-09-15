package com.my.shirospringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.shirospringboot.pojo.ShRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Gzy
 * @version 1.0
 * @Description
 */
@Component//作用是去掉idea的自动注入报红检查，无实际意义
@Mapper
public interface ShRolePermissionMapper extends BaseMapper<ShRolePermission> {

    @Select("select * from sh_role_permission where roleId = #{roleId} ")
    List<ShRolePermission> findListByRoleId(String roleId);

}
