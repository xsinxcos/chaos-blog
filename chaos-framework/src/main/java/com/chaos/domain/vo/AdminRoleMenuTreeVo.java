package com.chaos.domain.vo;

import com.chaos.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoleMenuTreeVo {
    private List<AdminTreeSelectMenuVo> menus;
    private Long[] checkedKeys;
}
