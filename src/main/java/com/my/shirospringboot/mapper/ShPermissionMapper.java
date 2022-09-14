package com.my.shirospringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.shirospringboot.pojo.ShPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Descrition shiro 权限类 Mapper
 */
@Component
@Mapper
public interface ShPermissionMapper extends BaseMapper<ShPermission> {

    @Select("select a.* from sh_permission a left join sh_role_permission b on a.id = b.permissionId " +
            "left join sh_user_role c on b.roleId = c.roleId where c.userId = #{userId} ")
    List<ShPermission> findListByUserId(String userId);

    @Select("select * from sh_permission where parentId = #{parentId} ")
    List<ShPermission> findListByParentId(String parentId);

}
