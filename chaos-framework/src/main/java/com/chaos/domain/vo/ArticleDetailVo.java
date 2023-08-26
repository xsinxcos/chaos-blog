package com.chaos.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVo {
    private Long categoryId;
    private String categoryName;
    private String content;
    private Date createTime;
    private Long id;
    private String isComment;
    private String title;
    private Long viewCount;
}
