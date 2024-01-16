package com.ruoyi.srm.supplierBase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.srm.supplierBase.domain.entity.AuthenticationInfoEntity;
import com.ruoyi.srm.supplierBase.mapper.AuthenticationInfoMapper;
import com.ruoyi.srm.supplierBase.service.AuthenticationInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: dyw
 * @date: 2023/5/26 10:38
 */
@Service
@Slf4j
public class AuthenticationInfoServiceImpl extends ServiceImpl<AuthenticationInfoMapper, AuthenticationInfoEntity> implements AuthenticationInfoService {
}
