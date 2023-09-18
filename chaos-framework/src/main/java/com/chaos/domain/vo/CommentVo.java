package com.chaos.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    private Long id;

    //评论类型（0代表文章评论，1代表友链评论）
    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    private String toCommentNickname;
    //回复目标评论id
    private Long toCommentId;

    private Long createBy;

    private Date createTime;

    private String nickname;

    private List<CommentVo> children;

    private String avatar;

}
