package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddCategoryDto;
import com.chaos.domain.dto.ListCategoryDto;
import com.chaos.domain.dto.UpdateCategoryDto;
import com.chaos.domain.entity.Category;
import com.chaos.domain.vo.CategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-08-04 11:32:35
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    ResponseResult listCategory(ListCategoryDto listCategoryDto);

    ResponseResult addCategory(AddCategoryDto addCategoryDto);

    ResponseResult getCategoryById(Long id);

    ResponseResult updateCategory(UpdateCategoryDto dto);

    ResponseResult deleteById(Long id);
}

