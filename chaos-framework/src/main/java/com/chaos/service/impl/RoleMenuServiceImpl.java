package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;
import com.chaos.domain.entity.RoleMenu;
import com.chaos.service.RoleMenuService;
/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-08-23 03:59:22
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

