package com.chaos.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.javassist.compiler.ast.StringL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagListVo {
    private Long id;
    private String name;
    private String remark;
}
