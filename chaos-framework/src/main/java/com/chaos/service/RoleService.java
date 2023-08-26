package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddRoleDto;
import com.chaos.domain.dto.ChangeRoleStatusDto;
import com.chaos.domain.dto.ListRoleDto;
import com.chaos.domain.dto.RoleUpdateDto;
import com.chaos.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-08-18 21:12:06
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listRole(ListRoleDto listRoleDto);

    ResponseResult changeRoleStatus(ChangeRoleStatusDto changeRoleStatusDto);

    ResponseResult showRole(Long id);

    ResponseResult addRole(AddRoleDto addRoleDto);


    ResponseResult updateRole(RoleUpdateDto roleUpdateDto);

    ResponseResult deleteRole(Long id);

    ResponseResult listAllRole();
}

