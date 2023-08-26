package com.chaos.controller;

import com.chaos.domain.ResponseResult;
import com.chaos.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/getAllLink")
    private ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}
