package com.chaos.controller;

import com.chaos.constants.SystemConstants;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.entity.Comment;
import com.chaos.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId , Integer pageNum , Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId ,pageNum ,pageSize);
    }
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum , Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null ,pageNum ,pageSize);
    }
    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
}
