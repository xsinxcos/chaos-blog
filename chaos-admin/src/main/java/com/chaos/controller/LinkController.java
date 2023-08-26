package com.chaos.controller;

import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddLinkDto;
import com.chaos.domain.dto.ChangeLinkStatusDto;
import com.chaos.domain.dto.ListLinkDto;
import com.chaos.domain.dto.UpdateLinkDto;
import com.chaos.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    LinkService linkService;
    @GetMapping("/list")
    public ResponseResult listLink(ListLinkDto listLinkDto){
        return linkService.listLink(listLinkDto);
    }
    @PostMapping
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto){
        return linkService.addLink(addLinkDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable Long id){
        return linkService.getLinkById(id);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody UpdateLinkDto updateLinkDto){
        return linkService.updateLink(updateLinkDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody ChangeLinkStatusDto dto){
        return linkService.changeLinkStatus(dto);
    }

}
