package com.chaos.service;

import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
