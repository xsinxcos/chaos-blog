package com.chaos.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.injector.methods.Update;
import com.chaos.domain.ResponseResult;
import com.chaos.enums.AppHttpCodeEnum;
import com.chaos.exception.SystemException;
import com.chaos.service.UploadService;
import com.chaos.utils.PathUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.ImageFilter;
import java.io.*;

@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class OssUploadService implements UploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //判断文件类型
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        //对原始文件名进行判断
        assert originalFilename != null;
        if(!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //如果成功则上传
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = uploadOss(img ,filePath);
        return ResponseResult.okResult(url);
    }
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String bucketName;
    private String uploadOss(MultipartFile img ,String filePath) {
        // RAM用户的访问密钥（AccessKey ID和AccessKey Secret）。

        // 使用代码嵌入的RAM用户的访问密钥配置访问凭证。
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = filePath;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

        try {
            InputStream inputStream = img.getInputStream();
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            return "https://chaos-myblog.oss-cn-guangzhou.aliyuncs.com/" + filePath;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }
}
