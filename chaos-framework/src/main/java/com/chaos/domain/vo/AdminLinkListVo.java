package com.chaos.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLinkListVo {
    private String address;
    private String description;
    private String logo;
    private Long id;
    private String name;
    private String status;
}
