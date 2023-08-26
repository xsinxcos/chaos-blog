package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.domain.ResponseResult;
import com.chaos.constants.SystemConstants;
import com.chaos.domain.dto.AddCategoryDto;
import com.chaos.domain.dto.ListCategoryDto;
import com.chaos.domain.dto.UpdateCategoryDto;
import com.chaos.domain.entity.Article;
import com.chaos.domain.entity.Category;
import com.chaos.domain.entity.User;
import com.chaos.domain.vo.AdminCategoryListVo;
import com.chaos.domain.vo.PageVo;
import com.chaos.utils.BeanCopyUtils;
import com.chaos.domain.vo.CategoryVo;
import com.chaos.mapper.CategoryMapper;
import com.chaos.service.ArticleService;
import com.chaos.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-08-04 11:32:36
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表，状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus , SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id。并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categories = listByIds(categoryIds);

        categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus ,SystemConstants.NORMAL);
        List<Category> categoryList = list(queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return categoryVos;
    }

    @Override
    public ResponseResult listCategory(ListCategoryDto listCategoryDto) {
        //查询未删除目录
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getDelFlag ,SystemConstants.CATEGORY_NORMAL);
        //根据分类名称模糊查询
        if(!Objects.isNull(listCategoryDto.getName()) && StringUtils.hasText(listCategoryDto.getName())) {
            wrapper.like(Category::getName, listCategoryDto.getName());
        }
        if(!Objects.isNull(listCategoryDto.getStatus()) && StringUtils.hasText(listCategoryDto.getStatus())){
            wrapper.eq(Category::getStatus ,listCategoryDto.getStatus());
        }
        //分页查询
        Page<Category> page = new Page<>(listCategoryDto.getPageNum() ,listCategoryDto.getPageSize());
        page(page ,wrapper);
        List<Category> records = page.getRecords();
        List<AdminCategoryListVo> vos = BeanCopyUtils.copyBeanList(records, AdminCategoryListVo.class);
        return ResponseResult.okResult(new PageVo(vos ,page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(AddCategoryDto addCategoryDto) {
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = getById(id);
        AdminCategoryListVo vo = BeanCopyUtils.copyBean(category, AdminCategoryListVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateCategory(UpdateCategoryDto dto) {
        LambdaUpdateWrapper<Category> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Category::getId ,dto.getId())
                .set(!Objects.isNull(dto.getDescription())&& StringUtils.hasText(dto.getDescription()) ,
                        Category::getDescription ,dto.getDescription())
                .set(!Objects.isNull(dto.getName())&& StringUtils.hasText(dto.getName()) ,
                        Category::getName ,dto.getName())
                .set(Category::getStatus ,dto.getStatus());
        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteById(Long id) {
        LambdaUpdateWrapper<Category> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Category::getId ,id)
                .set(Category::getDelFlag ,SystemConstants.CATEGORY_DELETE);
        update(wrapper);
        return ResponseResult.okResult();
    }


}

