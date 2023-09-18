package com.chaos.utils;

import com.chaos.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        if(!Objects.isNull(getAuthentication()))
            return (LoginUser) getAuthentication().getPrincipal();
        return null;
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        if(!Objects.isNull(getLoginUser())) {
            Long id = getLoginUser().getUser().getId();
            return id != null && 1L == id;
        }
        return false;
    }

    public static Long getUserId() {
        if(!Objects.isNull(getLoginUser())) {
            return getLoginUser().getUser().getId();
        }
        return null;
    }
}