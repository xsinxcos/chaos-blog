package com.chaos.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddCategoryDto;
import com.chaos.domain.dto.ListCategoryDto;
import com.chaos.domain.dto.UpdateCategoryDto;
import com.chaos.domain.vo.CategoryVo;
import com.chaos.domain.vo.ExcelCategoryVo;
import com.chaos.enums.AppHttpCodeEnum;
import com.chaos.service.CategoryService;
import com.chaos.utils.BeanCopyUtils;
import com.chaos.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> categoryVos = categoryService.listAllCategory();
        return ResponseResult.okResult(categoryVos);
    }
    @PreAuthorize("@ps.hasParmission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx" ,response);
            //获取需要导出的数据
            List<CategoryVo> categoryVos = categoryService.listAllCategory();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos , ExcelCategoryVo.class);
            //把数据写入excel
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @GetMapping("/list")
    public ResponseResult listCategory(ListCategoryDto listCategoryDto){
        return categoryService.listCategory(listCategoryDto);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.addCategory(addCategoryDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody UpdateCategoryDto dto){
        return categoryService.updateCategory(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable Long id){
        return categoryService.deleteById(id);
    }

}
