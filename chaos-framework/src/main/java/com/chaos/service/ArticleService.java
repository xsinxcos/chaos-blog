package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddArticleDto;
import com.chaos.domain.dto.ListArticleDto;
import com.chaos.domain.dto.UpdateArticleDto;
import com.chaos.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(AddArticleDto addArticleDto);

    ResponseResult listArticle(ListArticleDto listArticleDto);

    ResponseResult getArticle(Long id);

    ResponseResult deleteArticle(Long id);

    ResponseResult updateArticle(UpdateArticleDto updateArticleDto);
}
