package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.mapper.UserRoleMapper;
import com.chaos.service.UserRoleService;
import org.springframework.stereotype.Service;
import com.chaos.domain.entity.UserRole;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-08-23 18:42:39
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

