package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.constants.SystemConstants;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.*;
import com.chaos.domain.entity.RoleMenu;
import com.chaos.domain.vo.*;
import com.chaos.service.MenuService;
import com.chaos.service.RoleMenuService;
import com.chaos.service.RoleService;
import com.chaos.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chaos.domain.entity.Role;
import com.chaos.mapper.RoleMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-08-18 21:12:06
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    RoleMenuService roleMenuService;
    @Autowired
    MenuService menuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否为管理员，如果是则返回admin
        if(id == 1L){
            List<String> roleList = new ArrayList<>();
            roleList.add("admin");
            return roleList;
        }
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult listRole(ListRoleDto listRoleDto) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        //模糊搜索
        if(!Objects.isNull(listRoleDto.getRoleName())&& StringUtils.hasText(listRoleDto.getRoleName())) {
            wrapper.like(Role::getRoleName, listRoleDto.getRoleName());
        }
        //针对状态查询
        if (!Objects.isNull(listRoleDto.getStatus()) && StringUtils.hasText(listRoleDto.getStatus())){
            wrapper.eq(Role::getStatus ,listRoleDto.getStatus());
        }
        wrapper.eq(Role::getDelFlag ,SystemConstants.ROLE_NORMAL);
        //根据role_sort排序
        wrapper.orderByAsc(Role::getRoleSort);
        //分页查询
        Page<Role> pages = new Page(listRoleDto.getPageNum() ,listRoleDto.getPageSize());
        page(pages ,wrapper);

        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(pages.getRecords(), RoleVo.class);
        return ResponseResult.okResult(new PageVo(roleVos ,pages.getTotal()));
    }

    @Override
    public ResponseResult changeRoleStatus(ChangeRoleStatusDto changeRoleStatusDto) {
        Role role = getById(changeRoleStatusDto.getRoleId());
        role.setStatus(changeRoleStatusDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult showRole(Long id) {
        AdminRoleShowVo vo = BeanCopyUtils.copyBean(getById(id), AdminRoleShowVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        //将新增角色加入数据库
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        //将角色权限关系表加入数据库
        List<RoleMenu> roleMenus = new ArrayList<>();
        for(Long MenuId : addRoleDto.getMenuIds()){
            roleMenus.add(new RoleMenu(role.getId() ,MenuId));
        }
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateRole(RoleUpdateDto roleUpdateDto) {
        updateById(BeanCopyUtils.copyBean(roleUpdateDto ,Role.class));
        //进行角色菜单权限更新
        //批量删除原有的权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId ,roleUpdateDto.getId());
        roleMenuService.getBaseMapper().delete(wrapper);
        //批量新增修改的权限
        List<RoleMenu> roleMenus = new ArrayList<>();
        for(Long menuId : roleUpdateDto.getMenuIds()){
            roleMenus.add(new RoleMenu(roleUpdateDto.getId() ,menuId));
        }
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long id) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(StringUtils.hasText(String.valueOf(id)),Role::getId ,id)
                .set(Role::getDelFlag ,Integer.parseInt(SystemConstants.ROLE_DELETE));
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus ,SystemConstants.ROLE_STATUS_NORMAL);
        wrapper.eq(Role::getDelFlag ,SystemConstants.ROLE_NORMAL);
        List<Role> roles = list(wrapper);
        List<AdminRoleListVo> vos = BeanCopyUtils.copyBeanList(roles, AdminRoleListVo.class);
        return ResponseResult.okResult(vos);
    }


}

