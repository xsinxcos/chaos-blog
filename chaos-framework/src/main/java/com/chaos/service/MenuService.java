package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.ListMenuDto;
import com.chaos.domain.entity.Menu;
import com.chaos.domain.vo.AdminTreeSelectMenuVo;
import com.chaos.domain.vo.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-08-18 21:09:16
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult menuList(ListMenuDto listMenuDto);

    ResponseResult addMenu(Menu menu);

    ResponseResult getMenu(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(Long menuId);

    List<AdminTreeSelectMenuVo> treeSelectMenu();

    ResponseResult roleMenuTreeSelect(Long id);
}

