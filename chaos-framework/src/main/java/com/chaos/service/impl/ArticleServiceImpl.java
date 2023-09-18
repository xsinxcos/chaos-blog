package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.domain.ResponseResult;
import com.chaos.constants.SystemConstants;
import com.chaos.domain.dto.AddArticleDto;
import com.chaos.domain.dto.ListArticleDto;
import com.chaos.domain.dto.UpdateArticleDto;
import com.chaos.domain.entity.Article;
import com.chaos.domain.entity.ArticleTag;
import com.chaos.domain.entity.Category;
import com.chaos.domain.vo.*;
import com.chaos.service.ArticleTagService;
import com.chaos.utils.BeanCopyUtils;
import com.chaos.mapper.ArticleMapper;
import com.chaos.service.ArticleService;
import com.chaos.service.CategoryService;
import com.chaos.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper ,Article> implements ArticleService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
//          需要查询浏览量最高的前10篇文章的信息。要求展示文章标题和浏览量。把能让用户自己点击跳转到具体的文章详情进行浏览。
//
//      	注意：不能把草稿展示出来，不能把删除了的文章查询出来。要按照浏览量进行降序排序。
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus , SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多查询十条
        Page<Article> page = new Page(1,10);
        page(page ,queryWrapper);

        List<Article> articles = page.getRecords();
        for(Article article : articles){
            //从redis中获取viewCount
            Integer viewCount = redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
            article.setViewCount(viewCount.longValue());
        }
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(articles ,HotArticleVo.class));
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果 有categoryId 就要 查询时要和传入的相同
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0 ,Article::getCategoryId ,categoryId);
        //必须是正式文章
        queryWrapper.eq(Article::getStatus ,SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop进行降序
        queryWrapper.orderByAsc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum ,pageSize);
        page(page ,queryWrapper);
        //查询categoryName
        List<Article> articles = page.getRecords();
        for(Article article : articles){
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
            //从redis中获取viewCount
            Integer viewCount = redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
            article.setViewCount(viewCount.longValue());
        }

        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords() ,ArticleListVo.class);

        return ResponseResult.okResult(new PageVo(articleListVos ,page.getTotal()));
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询对应文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article , ArticleDetailVo.class);
        //根据文章id查询分类名称
        Category category = categoryService.getById(articleDetailVo.getCategoryId());
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }

        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount" ,id.toString() ,1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addArticle(AddArticleDto addArticleDto) {
        //将其转化成Article
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = new ArrayList<>();
        Long articleId = article.getId();
        //将其保存在于articleTags表
        for(Long TagId : addArticleDto.getTags()){
            articleTags.add(new ArticleTag(articleId ,TagId));
        }
        articleTagService.saveBatch(articleTags);
        //存储进redis
        Map<String, Integer> map = redisCache.getCacheMap("article:viewCount");
        map.put(articleId.toString() ,SystemConstants.ARTICLE_INITIAL_VIEWCOUNT);
        redisCache.setCacheMap( "article:viewCount",map);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listArticle(ListArticleDto listArticleDto) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        //对显示结果进行筛选
        wrapper.eq(Article::getDelFlag ,SystemConstants.ARTICLE_STATUS_NORMAL);
        if(!Objects.isNull(listArticleDto.getTitle())){
            wrapper.like(Article::getTitle ,listArticleDto.getTitle());
        }
        if(!Objects.isNull(listArticleDto.getSummary())){
            wrapper.like(Article::getSummary ,listArticleDto.getSummary());
        }
        Page<Article> page = new Page<>(listArticleDto.getPageNum() ,listArticleDto.getPageSize());
        page(page ,wrapper);
        List<Article> articles = page.getRecords();
        List<AdminArticleListVo> adminArticleListVos = BeanCopyUtils.copyBeanList(articles, AdminArticleListVo.class);
        return ResponseResult.okResult(new PageVo(adminArticleListVos ,page.getTotal()));
    }

    @Override
    public ResponseResult getArticle(Long id) {
        Article article = getById(id);
        //根据文章id查询对应tags
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper();
        wrapper.eq(ArticleTag::getArticleId ,id);
        //将查询得结果封装进vo
        AdminUpdateArticleVo articleVo = BeanCopyUtils.copyBean(article, AdminUpdateArticleVo.class);
        List<ArticleTag> list = articleTagService.list(wrapper);
        List<Long> Tags = new ArrayList<>();
        for(ArticleTag articleTag : list){
            Tags.add(articleTag.getTagId());
        }
        articleVo.setTags(Tags.toArray(new Long[Tags.size()]));
        return ResponseResult.okResult(articleVo);
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(StringUtils.hasText(String.valueOf(id)),Article::getId ,id)
                .set(Article::getDelFlag ,Integer.parseInt(SystemConstants.TAG_DELETE));
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateArticle(UpdateArticleDto updateArticleDto) {
        Article article = BeanCopyUtils.copyBean(updateArticleDto, Article.class);
        //更新文章
        updateById(article);
        //删除原有的文章标签关系
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId ,updateArticleDto.getId());
        articleTagService.getBaseMapper().delete(wrapper);
        //新增修改的文章标签关系
        for(Long tagId : updateArticleDto.getTags()){
            articleTagService.save(new ArticleTag(updateArticleDto.getId() ,tagId));
        }
        return ResponseResult.okResult();
    }
}
