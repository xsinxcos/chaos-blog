package com.chaos.controller;

import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddArticleDto;
import com.chaos.domain.dto.ListArticleDto;
import com.chaos.domain.dto.UpdateArticleDto;
import com.chaos.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.keyvalue.repository.config.QueryCreatorType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto addArticleDto){
        return articleService.addArticle(addArticleDto);
    }
    @GetMapping("/list")
    public ResponseResult listArticle(ListArticleDto listArticleDto){
        return articleService.listArticle(listArticleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticle(@PathVariable Long id){
        return articleService.getArticle(id);
    }

    @PutMapping
    private ResponseResult updateArticle(@RequestBody UpdateArticleDto updateArticleDto){
        return articleService.updateArticle(updateArticleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id){
        return articleService.deleteArticle(id);
    }
}
