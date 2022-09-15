package com.my.shirospringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.shirospringboot.pojo.ShUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Gzy
 * @version 1.0
 * @Description: 用户角色关系实体 数据操作
 */
@Component
@Mapper
public interface ShUserRoleMapper extends BaseMapper<ShUserRole> {
}
