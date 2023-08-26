package com.chaos.controller;

import com.chaos.domain.ResponseResult;
import com.chaos.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    UploadService uploadService;
    @RequestMapping
    public ResponseResult uploadImg(MultipartFile img){
            return uploadService.uploadImg(img);
    }
}
