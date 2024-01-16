package com.ruoyi.storage.service;

import cn.xuyanwu.spring.file.storage.Downloader;
import cn.xuyanwu.spring.file.storage.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Axel on 2023/6/2 15:07
 *
 * @author Axel
 */
public interface StorageService {
    /**
     * 上传文件
     * @param multipartFile 文件
     * @param path 存储路径
     * @return 文件详细信息
     */
    FileInfo uploadFile(MultipartFile multipartFile, String path);

    /**
     * 上传图片
     * @param multipartFile 图片文件
     * @param path 文件路径
     * @return 上传结果
     */
    FileInfo uploadImageFile(MultipartFile multipartFile, String path);

    /**
     * 批量上传文件
     * @param fileList 文件列表
     * @param path 存储路径
     * @return 上传文件的详情信息
     */
    List<FileInfo> batchUploadFile(List<MultipartFile> fileList, String path);

    /**
     * 根据文件路径删除文件
     * @param url 文件路径
     * @return 删除结果
     */
    boolean deleteFileByUrl(String url);

    /**
     * 根据存储id删除文件
     * @param id 文件id
     * @return 删除结果
     */
    boolean deleteFileById(String id);

    /**
     * 根据文件id下载文件
     * @param id 文件id
     * @return 下载器
     */
    Downloader downloadFileById(String id);

    /**
     * 根据文件url下载文件
     * @param url 文件路径
     * @return 下载器
     */
    Downloader downloadFileByUrl(String url);

    /**
     * 根据url下载缩略图
     * @param url 文件路径
     * @return 下载器
     */
    Downloader downloadThImgByUrl(String url);
}
