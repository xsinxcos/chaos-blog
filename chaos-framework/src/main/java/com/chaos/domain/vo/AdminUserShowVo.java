package com.chaos.domain.vo;

import com.chaos.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserShowVo {
    private Long [] roleIds;
    private List<AdminRoleListVo> roles;
    private UserVo user;
}
