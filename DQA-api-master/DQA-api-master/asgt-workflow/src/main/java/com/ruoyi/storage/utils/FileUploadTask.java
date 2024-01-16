package com.ruoyi.storage.utils;

import cn.hutool.core.lang.Assert;
import cn.xuyanwu.spring.file.storage.FileInfo;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.RecursiveTask;

/**
 * Created by Axel on 2023/6/2 18:12
 * 批量上传文件多线程任务
 * @author Axel
 */
public class FileUploadTask extends RecursiveTask<FileInfo> {
    private MultipartFile multipartFile;
    private String path;
    /**
     * 安全认证上下文，切换线程会丢失，从主线程中获取并进行初始化
     */
    private SecurityContext context;

    public FileUploadTask(MultipartFile multipartFile, String path, SecurityContext context) {
        this.multipartFile = multipartFile;
        this.path = path;
        this.context = context;
    }

    @Override
    protected FileInfo compute() {
        SecurityContextHolder.setContext(context);
        Assert.notNull(multipartFile, "文件为空，无法进行文件上传！");
        return FileUtils.uploadFile(multipartFile, path);
    }
}
