package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.entity.User;

public interface BlogLoginService{
    ResponseResult login(User user);

    ResponseResult logout();
}
