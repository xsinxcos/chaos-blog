package com.chaos.service.impl;

import com.chaos.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    /**
     * 判断当前用户是否有permission
     * @parm permission 要判断得权限
     * @return
     */
    public boolean hasPermission(String permission){
        //如果是超级管理员
        if(SecurityUtils.isAdmin()){
            return true;
        }
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
