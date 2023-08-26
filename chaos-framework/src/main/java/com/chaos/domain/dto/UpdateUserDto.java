package com.chaos.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private String email;
    private Long id;
    private String nickName;
    private String sex;
    private String status;
    private String userName;
    private List<Long> roleIds;
}
