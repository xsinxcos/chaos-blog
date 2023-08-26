package com.chaos.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Update;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.constants.SystemConstants;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddTagDto;
import com.chaos.domain.dto.TagListDto;
import com.chaos.domain.dto.TagUpdateDto;
import com.chaos.domain.entity.Tag;
import com.chaos.domain.vo.PageVo;
import com.chaos.domain.vo.TagListVo;
import com.chaos.enums.AppHttpCodeEnum;
import com.chaos.exception.SystemException;
import com.chaos.mapper.TagMapper;
import com.chaos.service.TagService;
import com.chaos.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-08-16 02:57:54
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //筛选相关数据
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());
        //封装成TagVo
        Page<Tag> page = new Page<>(pageNum ,pageSize);
        page(page ,queryWrapper);
        List<Tag> tagList = page.getRecords();
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(tagList ,TagListVo.class);

        return ResponseResult.okResult(new PageVo(tagListVos ,page.getTotal()));
    }

    @Override
    public ResponseResult addTag(AddTagDto addTagDto) {
        if(!StringUtils.hasText(addTagDto.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NOT_NULL);
        }else if(!StringUtils.hasText(addTagDto.getRemark())){
            throw new SystemException(AppHttpCodeEnum.TAG_REMARK_NOT_NULL);
        }

        Tag tag = BeanCopyUtils.copyBean(addTagDto ,Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {

        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(StringUtils.hasText(String.valueOf(id)),Tag::getId ,id)
                .set(Tag::getDelFlag ,Integer.parseInt(SystemConstants.TAG_DELETE));
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult showTag(Long id) {
        Tag tag = getById(id);
        return ResponseResult.okResult(BeanCopyUtils.copyBean(tag ,TagListVo.class));
    }

    @Override
    public ResponseResult updateTag(TagUpdateDto tagUpdateDto) {
        Tag tag = getById(tagUpdateDto.getId());
        if(Objects.isNull(tag)){
            throw new RuntimeException("id不存在");
        }else if(!StringUtils.hasText(tagUpdateDto.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NOT_NULL);
        }else if(!StringUtils.hasText(tagUpdateDto.getRemark())){
            throw new SystemException(AppHttpCodeEnum.TAG_REMARK_NOT_NULL);
        }
        tag.setName(tagUpdateDto.getName());
        tag.setRemark(tagUpdateDto.getRemark());
        getBaseMapper().updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getDelFlag ,SystemConstants.TAG_NORMAL);
        List<Tag> tagList = list(queryWrapper);
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(tagList, TagListVo.class);
        return ResponseResult.okResult(tagListVos);
    }
}

