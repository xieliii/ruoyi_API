package com.ruoyi.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.recorder.FileRecorder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.utils.SnowFlakeUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.storage.service.FileDetailService;
import com.ruoyi.workflow.domain.AgFileDetailEntity;
import com.ruoyi.workflow.mapper.AgFileDetailMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/**
 * Created by Axel on 2023/6/2 10:53
 *
 * @author Axel
 */
@Service
public class FileDetailServiceImpl extends ServiceImpl<AgFileDetailMapper, AgFileDetailEntity> implements FileDetailService, FileRecorder {

    @SneakyThrows
    @Override
    public boolean record(FileInfo fileInfo) {
        AgFileDetailEntity agFileDetailEntity = BeanUtil.copyProperties(fileInfo, AgFileDetailEntity.class, "attr", "createTime");
        //这是手动获 取附加属性字典 并转成 json 字符串，方便存储在数据库中
        if (fileInfo.getAttr() != null) {
            agFileDetailEntity.setAttr(new ObjectMapper().writeValueAsString(fileInfo.getAttr()));
        }
        agFileDetailEntity.setId(SnowFlakeUtil.snowFlakeId());
        boolean b = save(agFileDetailEntity);
        if (b) {
            fileInfo.setId(agFileDetailEntity.getId());
        }
        return b;
    }

    /**
     * 根据 url 查询文件信息
     */
    @SneakyThrows
    @Override
    public FileInfo getByUrl(String s) {
        Assert.notBlank(s, "文件url为空，无法获取文件数据！");
        AgFileDetailEntity detail = getOne(new LambdaQueryWrapper<AgFileDetailEntity>().eq(AgFileDetailEntity::getUrl,s));
        Assert.notNull(detail, "当前文件不存在,无法获取文件数据！");
        FileInfo info = BeanUtil.copyProperties(detail,FileInfo.class,"attr");

        //这是手动获取数据库中的 json 字符串 并转成 附加属性字典，方便使用
        if (StringUtils.isNotBlank(detail.getAttr())) {
            info.setAttr(new ObjectMapper().readValue(detail.getAttr(), Dict.class));
        }
        return info;
    }

    /**
     * 根据 url 删除文件信息
     */
    @Override
    public boolean delete(String s) {
        Assert.notBlank(s, "文件url为空，无法删除文件！");
        return remove(new LambdaQueryWrapper<AgFileDetailEntity>().eq(AgFileDetailEntity::getUrl,s));
    }

}
