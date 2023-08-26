package com.chaos.controller;

import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddTagDto;
import com.chaos.domain.dto.TagListDto;
import com.chaos.domain.dto.TagUpdateDto;
import com.chaos.domain.vo.TagListVo;
import com.chaos.service.TagService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    TagService tagService ;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum , Integer pageSize , TagListDto tagListDto){
        return tagService.list(pageNum ,pageSize ,tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto){
        return tagService.addTag(addTagDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult showTag(@PathVariable("id") Long id){
        return tagService.showTag(id);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody TagUpdateDto tagUpdateDto){
        return tagService.updateTag(tagUpdateDto);
    }
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
