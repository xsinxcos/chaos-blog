package com.chaos.controller;

import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddRoleDto;
import com.chaos.domain.dto.ChangeRoleStatusDto;
import com.chaos.domain.dto.ListRoleDto;
import com.chaos.domain.dto.RoleUpdateDto;
import com.chaos.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("/list")
    public ResponseResult listRole(ListRoleDto listRoleDto){
        return roleService.listRole(listRoleDto);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeRoleStatus(@RequestBody ChangeRoleStatusDto changeRoleStatusDto){
        return roleService.changeRoleStatus(changeRoleStatusDto);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult showRole(@PathVariable Long id){
        return roleService.showRole(id);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleUpdateDto roleUpdateDto){
        return roleService.updateRole(roleUpdateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable Long id){
        return roleService.deleteRole(id);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
}
