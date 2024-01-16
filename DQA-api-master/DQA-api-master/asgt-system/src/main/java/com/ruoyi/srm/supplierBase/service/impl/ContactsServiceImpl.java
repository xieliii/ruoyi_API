package com.ruoyi.srm.supplierBase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.srm.supplierBase.domain.entity.ContactsEntity;
import com.ruoyi.srm.supplierBase.mapper.ContactsMapper;
import com.ruoyi.srm.supplierBase.service.ContactsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: dyw
 * @date: 2023/5/26 10:42
 */
@Service
@Slf4j
public class ContactsServiceImpl extends ServiceImpl<ContactsMapper, ContactsEntity> implements ContactsService {
}
