package com.chaos.domain.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListArticleDto {
    private Integer pageNum;
    private Integer pageSize;
    private String title;
    private String summary;
}
