package com.chaos.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddTagDto;
import com.chaos.domain.dto.TagListDto;
import com.chaos.domain.dto.TagUpdateDto;
import com.chaos.domain.entity.Tag;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-08-16 02:57:53
 */
public interface TagService extends IService<Tag> {
    ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(AddTagDto addTagDto);

    ResponseResult deleteTag(Long id);

    ResponseResult showTag(Long id);

    ResponseResult updateTag(TagUpdateDto tagUpdateDto);

    ResponseResult listAllTag();
}

