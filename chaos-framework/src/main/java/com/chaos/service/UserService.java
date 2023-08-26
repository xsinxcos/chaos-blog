package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddUserDto;
import com.chaos.domain.dto.ListUserDto;
import com.chaos.domain.dto.UpdateUserDto;
import com.chaos.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-08-08 00:00:42
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult userList(ListUserDto listUserDto);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult deleteUser(Long id);

    ResponseResult showUser(Long id);

    ResponseResult updateUser(UpdateUserDto updateUserDto);
}

