package com.chaos.controller;

import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddUserDto;
import com.chaos.domain.dto.ListUserDto;
import com.chaos.domain.dto.UpdateUserDto;
import com.chaos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/list")
    public ResponseResult userList(ListUserDto listUserDto){
        return userService.userList(listUserDto);
    }
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
    @GetMapping("/{id}")
    public ResponseResult showUser(@PathVariable Long id){
        return userService.showUser(id);
    }

    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }
}
