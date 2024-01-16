package com.ruoyi.srm.supplierBase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.srm.supplierBase.domain.entity.BusinessInfoEntity;
import com.ruoyi.srm.supplierBase.mapper.BusinessInfoMapper;
import com.ruoyi.srm.supplierBase.service.BusinessInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: dyw
 * @date: 2023/5/26 10:44
 */
@Service
@Slf4j
public class BusinessInfoServiceImpl extends ServiceImpl<BusinessInfoMapper, BusinessInfoEntity> implements BusinessInfoService {
}
