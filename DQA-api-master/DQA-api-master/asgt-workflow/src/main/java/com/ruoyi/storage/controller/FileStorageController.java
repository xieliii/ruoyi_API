package com.ruoyi.storage.controller;

import cn.hutool.core.io.IoUtil;
import cn.xuyanwu.spring.file.storage.Downloader;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.storage.service.FileDetailService;
import com.ruoyi.storage.service.StorageService;
import com.ruoyi.workflow.domain.AgFileDetailEntity;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Axel on 2023/6/2 16:14
 *
 * @author Axel
 */
@Slf4j
@RestController
@RequestMapping("/fileStorage")
public class FileStorageController {
    @Resource
    private StorageService storageService;

    @Resource
    private FileDetailService fileDetailService;

    @Resource
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public R<Object> uploadFile(MultipartFile file, String path){
        FileInfo fileInfo = storageService.uploadFile(file, path);
        return R.ok(fileInfo);
    }

    @PostMapping("/uploadImageFile")
    public R<Object> uploadImageFile(MultipartFile file, String path){
        FileInfo fileInfo = storageService.uploadImageFile(file, path);
        return R.ok(fileInfo);
    }

    @PostMapping("/batchUploadFile")
    public R<Object> batchUploadFile(List<MultipartFile> file, String path){
        List<FileInfo> fileInfos = storageService.batchUploadFile(file, path);
        return R.ok(fileInfos);
    }

    @GetMapping("/downloadFileById")
    public void downloadFileById(String id, HttpServletResponse response){
        if(StringUtils.isBlank(id)){
            throw new RuntimeException("文件id为空，无法下载文件！");
        }
        AgFileDetailEntity byId = fileDetailService.getById(id);
        if(ObjectUtils.isNull(byId)){
            throw new RuntimeException("文件详情为空，无法下载文件！");
        }
        String filename = byId.getOriginalFilename();
        Downloader downloader = storageService.downloadFileById(id);
        try {
            FileUtils.setAttachmentResponseHeader(response, filename);
            ServletOutputStream stream = response.getOutputStream();
            downloader.inputStream(inputStream -> IoUtil.copy(inputStream, stream));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(filename + "文件下载失败");
        }
    }

    @GetMapping("/downloadFileByUrl")
    public void downloadFileByUrl(String url, HttpServletResponse response){
        if(StringUtils.isBlank(url)){
            throw new RuntimeException("文件url为空，无法下载文件！");
        }
        FileInfo fileInfoByUrl = fileStorageService.getFileInfoByUrl(url);
        if(ObjectUtils.isNull(fileInfoByUrl)){
            throw new RuntimeException("文件详情为空，无法下载文件！");
        }
        String filename = fileInfoByUrl.getOriginalFilename();
        Downloader downloader = storageService.downloadFileByUrl(url);
        try {
            FileUtils.setAttachmentResponseHeader(response, filename);
            @Cleanup ServletOutputStream stream = response.getOutputStream();
            downloader.inputStream(inputStream -> IoUtil.copy(inputStream, stream));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(filename + "文件下载失败");
        }
    }

    @GetMapping("/downloadThImgByUrl")
    public void downloadThImgByUrl(String url, HttpServletResponse response){
        if(StringUtils.isBlank(url)){
            throw new RuntimeException("文件url为空，无法下载文件！");
        }
        FileInfo fileInfoByUrl = fileStorageService.getFileInfoByUrl(url);
        if(ObjectUtils.isNull(fileInfoByUrl)){
            throw new RuntimeException("文件详情为空，无法下载文件！");
        }
        String filename = fileInfoByUrl.getThFilename();
        Downloader downloader = storageService.downloadThImgByUrl(url);
        try {
            FileUtils.setAttachmentResponseHeader(response, filename);
            @Cleanup ServletOutputStream stream = response.getOutputStream();
            downloader.inputStream(inputStream -> IoUtil.copy(inputStream, stream));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(filename + "文件下载失败");
        }
    }

    @GetMapping("/deleteFileById")
    public R<Object> deleteFileById(String id){
        storageService.deleteFileById(id);
        return R.ok();
    }

    @GetMapping("/deleteFileByUrl")
    public R<Object> deleteFileByUrl(String url){
        storageService.deleteFileByUrl(url);
        return R.ok();
    }

    @PostMapping("/getFileInfoByIdList")
    public R<Object> getFileInfoByIdList(@RequestBody List<String> list){
        if(CollectionUtils.isEmpty(list)){
            return R.fail("文件id列表为空，无法获取文件详细信息！");
        }
        List<AgFileDetailEntity> entities = fileDetailService.listByIds(list);
        return R.ok(entities);
    }
}
