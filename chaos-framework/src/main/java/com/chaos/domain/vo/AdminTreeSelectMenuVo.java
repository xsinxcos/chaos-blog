package com.chaos.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminTreeSelectMenuVo {
    private Long id;
    private String label;
    private Long parentId;
    private List<AdminTreeSelectMenuVo> children;
}
