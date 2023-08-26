package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.constants.SystemConstants;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddUserDto;
import com.chaos.domain.dto.ListUserDto;
import com.chaos.domain.dto.UpdateUserDto;
import com.chaos.domain.entity.Role;
import com.chaos.domain.entity.UserRole;
import com.chaos.domain.vo.*;
import com.chaos.enums.AppHttpCodeEnum;
import com.chaos.exception.SystemException;
import com.chaos.mapper.UserMapper;
import com.chaos.service.RoleService;
import com.chaos.service.UserRoleService;
import com.chaos.service.UserService;
import com.chaos.utils.BeanCopyUtils;
import com.chaos.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.chaos.domain.entity.User;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-08-08 00:00:42
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleService roleService;
    @Override
    public ResponseResult userInfo() {
        //获取当前用户ID
        Long userId = SecurityUtils.getUserId();
        //获取ID对应的数据
        User user = getById(userId);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user ,UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }


    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName ,nickName);
        return count(queryWrapper) > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName ,userName);
        return count(queryWrapper) > 0;
    }

    private boolean phonenumberExist(String phonenumber){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber ,phonenumber);
        return count(queryWrapper) > 0;
    }

    private boolean emailExist(String email){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail ,email);
        return count(queryWrapper) > 0;
    }

    @Override
    public ResponseResult userList(ListUserDto listUserDto) {
        //对用户进行查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getDelFlag , SystemConstants.USER_NORMAL);
        //根据用户名模糊搜索
        if(!Objects.isNull(listUserDto.getUserName()) && StringUtils.hasText(listUserDto.getUserName())) {
            wrapper.like(User::getUserName, listUserDto.getUserName());
        }
        //根据手机号搜索
        if(!Objects.isNull(listUserDto.getPhonenumber()) && StringUtils.hasText(listUserDto.getPhonenumber())) {
            wrapper.eq(User::getPhonenumber, listUserDto.getPhonenumber());
        }
        //根据状态查询
        if(!Objects.isNull(listUserDto.getStatus()) && StringUtils.hasText(listUserDto.getStatus())) {
            wrapper.eq(User::getStatus, listUserDto.getStatus());
        }
        //分页
        Page<User> pages = new Page<>(listUserDto.getPageNum() ,listUserDto.getPageSize());
        page(pages ,wrapper);
        List<AdminUserListVo> vos = BeanCopyUtils.copyBeanList(pages.getRecords() ,AdminUserListVo.class);
        return ResponseResult.okResult(new PageVo(vos ,pages.getTotal()));
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        User user = BeanCopyUtils.copyBean(addUserDto ,User.class);
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }

        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }

        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(phonenumberExist(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        //将角色用户关联表进行保存
        List<UserRole> userRoleList = new ArrayList<>();
        for(Long roleId : addUserDto.getRoleIds()){
            userRoleList.add(new UserRole(user.getId() ,roleId));
        }
        userRoleService.saveBatch(userRoleList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(Long id) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId ,id)
                .set(User::getDelFlag ,SystemConstants.USER_DELETE);
        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult showUser(Long id) {
        AdminUserShowVo vo = new AdminUserShowVo();
        //查询User信息
        vo.setUser(BeanCopyUtils.copyBean(getById(id) , UserVo.class));
        //查询roleIds
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId ,id);
        List<UserRole> userRoles = userRoleService.list(wrapper);
        //将roleIds查询结果放入vo
        List<Long> roleIds = new ArrayList<>();
        for(UserRole userRole : userRoles){
            roleIds.add(userRole.getRoleId());
        }
        vo.setRoleIds(roleIds.toArray(new Long[0]));
        //查询roles信息封装
        LambdaQueryWrapper<Role> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Role::getStatus ,SystemConstants.ROLE_STATUS_NORMAL);
        wrapper1.eq(Role::getDelFlag ,SystemConstants.ROLE_NORMAL);
        List<Role> roles = roleService.list(wrapper1);
        vo.setRoles(BeanCopyUtils.copyBeanList(roles ,AdminRoleListVo.class));
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUser(UpdateUserDto updateUserDto) {
        //对用户表进行更新
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId ,updateUserDto.getId())
                .set(StringUtils.hasText(updateUserDto.getNickName()) ,User::getNickName ,updateUserDto.getNickName())
                .set(StringUtils.hasText(updateUserDto.getEmail()) ,User::getEmail ,updateUserDto.getEmail())
                .set(StringUtils.hasText(updateUserDto.getSex()) ,User::getSex ,updateUserDto.getSex())
                .set(StringUtils.hasText(updateUserDto.getStatus()) ,User::getStatus ,updateUserDto.getStatus());
        update(wrapper);
        //对用户角色表进行更新
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId ,updateUserDto.getId());
        userRoleService.getBaseMapper().delete(queryWrapper);
        List<UserRole> userRoleList = new ArrayList<>();
        for(Long roleId : updateUserDto.getRoleIds()){
            userRoleList.add(new UserRole(updateUserDto.getId() ,roleId));
        }
        userRoleService.saveBatch(userRoleList);
        return ResponseResult.okResult();
    }

}

