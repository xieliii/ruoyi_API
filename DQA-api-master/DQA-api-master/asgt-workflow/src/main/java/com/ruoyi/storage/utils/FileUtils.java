package com.ruoyi.storage.utils;

import cn.hutool.core.lang.Assert;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import cn.xuyanwu.spring.file.storage.UploadPretreatment;
import com.ruoyi.common.utils.SnowFlakeUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Axel on 2023/6/2 17:47
 *
 * @author Axel
 */
public class FileUtils {
    /**
     * 上传单个文件
     */
    public static FileInfo uploadFile(MultipartFile multipartFile, String path) {
        Assert.notNull(multipartFile, "文件数据为空，无法进行文件上传操作！");
        String originalFilename = multipartFile.getOriginalFilename();
        FileStorageService fileStorageService = SpringUtils.getBean(FileStorageService.class);
        UploadPretreatment uploadPretreatment = fileStorageService.of(multipartFile)
                .setName(originalFilename)
                .setOriginalFilename(originalFilename)
                .setSaveFilename(StringUtils.join(SnowFlakeUtil.snowFlakeId(), "-", originalFilename));
        if (StringUtils.isNotBlank(path)) {
            // 路径需使用/隔开
            uploadPretreatment.setPath(path + "/");
        }
        return uploadPretreatment.upload();
    }

    /**
     * 上传图片文件
     * @param multipartFile 图片文件
     * @param path 文件路径
     * @return 上传结果
     */
    public static FileInfo uploadImageFile(MultipartFile multipartFile, String path){
        Assert.notNull(multipartFile, "文件数据为空，无法进行文件上传操作！");
        String originalFilename = multipartFile.getOriginalFilename();
        FileStorageService fileStorageService = SpringUtils.getBean(FileStorageService.class);
        String join = StringUtils.join(SnowFlakeUtil.snowFlakeId(), "-", originalFilename);
        UploadPretreatment uploadPretreatment = fileStorageService.of(multipartFile)
                .setName(originalFilename)
                .setOriginalFilename(originalFilename)
//                .setThumbnailSuffix(".jpg")
//                .setSaveThFilename(join + "mini")
                .thumbnail(th -> th.size(200,200))
                .setSaveFilename(join);
        if (StringUtils.isNotBlank(path)) {
            // 路径需使用/隔开
            uploadPretreatment.setPath(path + "/");
        }
        return uploadPretreatment.upload();
    }
}
