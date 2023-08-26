package com.chaos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-18 21:12:06
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}
