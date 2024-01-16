package com.ruoyi.srm.contractMgt.utils;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.ruoyi.srm.contractMgt.domain.VO.ContractRsVO;
import com.ruoyi.srm.contractMgt.domain.entity.ContractEntity;
import com.ruoyi.srm.supplierBase.domain.VO.ClearRecordsVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Omelette
 */
public class ContractUtil {
    public static List<ContractRsVO> transformContractEntities(List<ContractEntity> entities){
        Assert.notNull(entities, "合同不存在");
        List<ContractRsVO> vos = new ArrayList<>();
        entities.forEach(entity -> {
            ContractRsVO baseVO = new ContractRsVO();
            BeanUtils.copyProperties(entity,baseVO);
            vos.add(baseVO);
        });
        return vos;
    }
}
