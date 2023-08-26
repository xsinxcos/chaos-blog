package com.chaos.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddArticleDto {
    private String title;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 文章摘要
     */
    private String summary;
    /**
     * 所属分类id
     */
    private Long categoryId;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 是否置顶（0否，1是）
     */
    private String isTop;
    /**
     * 状态（0已发布，1草稿）
     */
    private String status;

    private Long[] tags;
}
