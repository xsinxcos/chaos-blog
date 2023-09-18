package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.constants.SystemConstants;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.entity.Comment;
import com.chaos.domain.entity.User;
import com.chaos.domain.vo.CommentVo;
import com.chaos.domain.vo.PageVo;
import com.chaos.enums.AppHttpCodeEnum;
import com.chaos.exception.SystemException;
import com.chaos.mapper.CommentMapper;
import com.chaos.service.CommentService;
import com.chaos.service.UserService;
import com.chaos.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-08-07 22:33:49
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        //根评论Id为-1
        queryWrapper.eq(Comment::getRootId ,-1);
        //评论类型
        queryWrapper.eq(Comment::getType,commentType);
        //分页查询
        Page<Comment> page = new Page<>(pageNum ,pageSize);
        page(page ,queryWrapper);

        List<CommentVo> commentVos = toCommentVoList(page.getRecords());

        for (CommentVo commentVo : commentVos) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVos ,page.getTotal()));
    }

    private List<CommentVo> getChildren(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);

        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }
    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历Vo集合
        for(CommentVo commentVo : commentVos) {
            User user = userService.getById(commentVo.getCreateBy());
            if(Objects.isNull(user)) continue;
            String nickName = user.getNickName();
            commentVo.setNickname(nickName);
            commentVo.setAvatar(user.getAvatar());
            //如果toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentUserId() != -1) {
                User toCommentUser = userService.getById(commentVo.getToCommentUserId());
                if(Objects.isNull(toCommentUser)) continue;
                String toCommentNickName = toCommentUser.getNickName();
                commentVo.setToCommentNickname(toCommentNickName);
            }
        }
        //通过create
        return commentVos;
    }
    @Override
    public ResponseResult addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

}

