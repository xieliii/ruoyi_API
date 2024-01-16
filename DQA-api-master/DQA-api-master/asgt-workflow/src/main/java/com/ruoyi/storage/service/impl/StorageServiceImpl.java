package com.ruoyi.storage.service.impl;

import cn.hutool.core.lang.Assert;
import cn.xuyanwu.spring.file.storage.Downloader;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import com.ruoyi.common.utils.Threads;
import com.ruoyi.storage.service.FileDetailService;
import com.ruoyi.storage.service.StorageService;
import com.ruoyi.storage.utils.FileUploadTask;
import com.ruoyi.storage.utils.FileUtils;
import com.ruoyi.workflow.domain.AgFileDetailEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * Created by Axel on 2023/6/2 15:08
 *
 * @author Axel
 */
@Slf4j
@Service
public class StorageServiceImpl implements StorageService {
    @Resource
    private FileStorageService fileStorageService;

    @Resource
    private FileDetailService fileDetailService;

    /**
     * 业务接口
     */
    @Override
    public FileInfo uploadFile(MultipartFile multipartFile, String path) {
        return FileUtils.uploadFile(multipartFile, path);
    }

    @Override
    public FileInfo uploadImageFile(MultipartFile multipartFile, String path) {
        return FileUtils.uploadImageFile(multipartFile, path);
    }

    @Override
    public List<FileInfo> batchUploadFile(List<MultipartFile> fileList, String path) {
        ArrayList<FileInfo> list = new ArrayList<>(fileList.size());
        ForkJoinPool forkJoinPool = new ForkJoinPool(fileList.size());
        SecurityContext context = SecurityContextHolder.getContext();
        fileList.forEach(multipartFile -> {
            FileUploadTask task = new FileUploadTask(multipartFile, path, context);
            ForkJoinTask<FileInfo> submit = forkJoinPool.submit(task);
            try {
                FileInfo fileInfo = submit.get();
                list.add(fileInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("线程中断异常！");
                throw new RuntimeException("线程执行中断！");
            } catch (ExecutionException e) {
                e.printStackTrace();
                log.error("线程执行异常!");
                throw new RuntimeException("线程执行异常！");
            }
        });
        return list;
    }

    @Override
    public boolean deleteFileByUrl(String url) {
        Assert.notBlank(url, "文件url为空，无法删除文件！");
        return fileStorageService.delete(url);
    }

    @Override
    public boolean deleteFileById(String id) {
        Assert.notBlank(id, "文件id为空，无法删除文件！");
        AgFileDetailEntity byId = fileDetailService.getById(id);
        Assert.notNull(byId, "文件详情为空，无法删除文件！");
        String url = byId.getUrl();
        Assert.notBlank(url, "文件url为空，无法删除文件！");
        return fileStorageService.delete(url);
    }

    @Override
    public Downloader downloadFileById(String id) {
        Assert.notBlank(id, "文件id为空，无法下载文件!");
        AgFileDetailEntity byId = fileDetailService.getById(id);
        Assert.notNull(byId, "文件详情不存在，无法进行文件下载！");
        String url = byId.getUrl();
        Assert.notBlank(url, "文件url为空，无法下载文件！");
        return fileStorageService.download(url);
    }

    @Override
    public Downloader downloadFileByUrl(String url) {
        Assert.notBlank(url, "文件url为空，无法下载文件！");
        return fileStorageService.download(url);
    }

    @Override
    public Downloader downloadThImgByUrl(String url) {
        Assert.notBlank(url, "文件url为空，无法下载文件！");
        return fileStorageService.downloadTh(url);
    }
}
