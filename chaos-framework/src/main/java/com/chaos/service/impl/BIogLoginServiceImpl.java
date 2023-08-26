package com.chaos.service.impl;

import com.chaos.domain.ResponseResult;
import com.chaos.domain.entity.LoginUser;
import com.chaos.domain.entity.User;
import com.chaos.utils.BeanCopyUtils;
import com.chaos.utils.JwtUtil;
import com.chaos.utils.RedisCache;
import com.chaos.domain.vo.BlogUserLoginVo;
import com.chaos.domain.vo.UserInfoVo;
import com.chaos.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BIogLoginServiceImpl implements BlogLoginService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName() ,user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("bloglogin:" + userId ,loginUser);
        //把token和userinfo封装 返回
        //将User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser() ,UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt ,userInfoVo);
        return ResponseResult.okResult(vo);

    }

    @Override
    public ResponseResult logout() {
        //获取token 解析获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userId
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:" + userId);
        return ResponseResult.okResult();
    }
}
