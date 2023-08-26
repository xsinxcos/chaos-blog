package com.chaos.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLinkDto {
    private String address;
    private String description;
    private Long id;
    private String logo;
    private String name;
    private String status;
}
