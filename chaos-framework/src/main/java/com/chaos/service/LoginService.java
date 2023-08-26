package com.chaos.service;

import com.chaos.domain.ResponseResult;
import com.chaos.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
