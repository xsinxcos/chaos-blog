package com.chaos.controller;

import com.chaos.domain.ResponseResult;
import com.chaos.domain.entity.LoginUser;
import com.chaos.domain.entity.Menu;
import com.chaos.domain.entity.User;
import com.chaos.domain.vo.AdminUserInfoVo;
import com.chaos.domain.vo.MenuVo;
import com.chaos.domain.vo.RoutersVo;
import com.chaos.domain.vo.UserInfoVo;
import com.chaos.enums.AppHttpCodeEnum;
import com.chaos.exception.SystemException;
import com.chaos.service.LoginService;
import com.chaos.service.MenuService;
import com.chaos.service.RoleService;
import com.chaos.utils.BeanCopyUtils;
import com.chaos.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LoginController {
    @Autowired
    LoginService loginService;

    @Autowired
    MenuService menuService;

    @Autowired
    RoleService roleService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("getInfo")
    public ResponseResult getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户Id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user ,UserInfoVo.class);

        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms ,roleKeyList ,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("getRouters")
    public ResponseResult getRouters(){
        Long userId = SecurityUtils.getUserId();
        ////查询menu 结果是tree的形式
        List<MenuVo> menuList = menuService.selectRouterMenuTreeByUserId(userId);
        return ResponseResult.okResult(new RoutersVo(menuList));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
