package com.chaos.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListLinkDto {
    private Integer pageNum;
    private Integer pageSize;
    private String name;
    private String status;
}
