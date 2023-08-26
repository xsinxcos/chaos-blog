package com.chaos.controller;

import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.ListMenuDto;
import com.chaos.domain.entity.Menu;
import com.chaos.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult menuList(ListMenuDto listMenuDto){
        return menuService.menuList(listMenuDto);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @GetMapping("/{id}")
    public ResponseResult getMenu(@PathVariable Long id){
        return menuService.getMenu(id);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @DeleteMapping("{menuId}")
    public ResponseResult deleteMenu(@PathVariable Long menuId){
        return menuService.deleteMenu(menuId);
    }

    @GetMapping("/treeselect")
    public ResponseResult treeSelectMenu(){
        return ResponseResult.okResult(menuService.treeSelectMenu());
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeSelect(@PathVariable Long id){
        return menuService.roleMenuTreeSelect(id);
    }
}
