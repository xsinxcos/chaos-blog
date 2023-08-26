package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.mapper.ArticleTagMapper;
import com.chaos.service.ArticleTagService;
import org.springframework.stereotype.Service;
import com.chaos.domain.entity.ArticleTag;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-08-21 03:06:45
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}

