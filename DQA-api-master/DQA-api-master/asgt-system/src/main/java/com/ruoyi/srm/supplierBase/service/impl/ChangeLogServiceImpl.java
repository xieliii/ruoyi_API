package com.ruoyi.srm.supplierBase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.snowflake.SnowFlakeUtil;
import com.ruoyi.srm.supplierBase.constant.SupplierBaseConstant;
import com.ruoyi.srm.supplierBase.domain.DTO.SearchChangeLogDTO;
import com.ruoyi.srm.supplierBase.domain.VO.ChangeRecordsVO;
import com.ruoyi.srm.supplierBase.domain.entity.ChangeRecordsEntity;
import com.ruoyi.srm.supplierBase.mapper.ChangeRecordsMapper;
import com.ruoyi.srm.supplierBase.service.ChangeLogService;
import com.ruoyi.srm.supplierBase.utils.SupplierUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: dyw
 * @date: 2023/5/25 15:44
 */
@Service
@Slf4j
public class ChangeLogServiceImpl extends ServiceImpl<ChangeRecordsMapper, ChangeRecordsEntity> implements ChangeLogService {
    @Resource
    private ChangeRecordsMapper changeRecordsMapper;

    @Override
    public void addChangeLog(ChangeRecordsEntity entity) {
        if (StringUtils.isEmpty(entity.getId())){
            throw new RuntimeException("该供应商不存在！");
        }
        entity.setId(SnowFlakeUtil.snowFlakeId());
        changeRecordsMapper.insert(entity);
    }

    @Override
    public ChangeRecordsVO getChangeLog(String id) {
        if (StringUtils.isEmpty(id)){
            throw new RuntimeException("该变更记录不存在，请刷新后再试");
        }
        ChangeRecordsEntity changeRecordsEntity = changeRecordsMapper.selectById(id);
        ChangeRecordsVO recordsVO = new ChangeRecordsVO();
        BeanUtils.copyBeanProp(changeRecordsEntity,recordsVO);
        return recordsVO;
    }

    /**
     * 根据流程实例id
     *
     * @param processInstanceId 流程实例id
     * @return 变更记录
     */
    @Override
    public ChangeRecordsEntity getChangeByProcessInstanceId(String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)){
            throw new RuntimeException("流程不存在");
        }
        LambdaQueryWrapper<ChangeRecordsEntity> wrapper = Wrappers.lambdaQuery(ChangeRecordsEntity.class);
        wrapper.eq(ChangeRecordsEntity::getDeleteFlag, SupplierBaseConstant.IS_NOT_DELETE)
                .eq(ChangeRecordsEntity::getProcessInstanceId, processInstanceId);
        return changeRecordsMapper.selectOne(wrapper);
    }

    @Override
    public List<ChangeRecordsVO> getChangeLogByPage(SearchChangeLogDTO dto) {
//        LambdaQueryWrapper<ChangeRecordsEntity> wrapper = Wrappers.lambdaQuery(ChangeRecordsEntity.class);
//        wrapper.eq(ChangeRecordsEntity::getDeleteFlag, SupplierBaseConstant.IS_NOT_DELETE);
//        List<ChangeRecordsEntity> changeRecordsEntities = changeRecordsMapper.selectList(wrapper);
//        List<ChangeRecordsVO> changeRecordsVOS = SupplierUtil.transformChangeRecordEntities(changeRecordsEntities);
//        return changeRecordsVOS;
        List<ChangeRecordsEntity> changeLogByCondition = changeRecordsMapper.getChangeLogByCondition(dto);
        return SupplierUtil.transformChangeRecordEntities(changeLogByCondition);
    }
}
