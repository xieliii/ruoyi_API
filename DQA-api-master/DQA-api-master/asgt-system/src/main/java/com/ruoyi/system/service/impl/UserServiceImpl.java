package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.UserEntity;
import com.ruoyi.system.mapper.CommonUserMapper;
import com.ruoyi.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: parus
 * @date: 2023/6/5 13:57
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<CommonUserMapper, UserEntity> implements UserService {

    @Resource
    private CommonUserMapper commonUserMapper;

    @Override
    public UserEntity getUserInfoById(Long id) {
        if (id == null){
            throw new RuntimeException("用户不存在");
        }
        return commonUserMapper.selectById(id);
    }

    @Override
    public Map<Long, UserEntity> getUserInfoByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)){
            throw new RuntimeException("数据有误");
        }
        List<UserEntity> userEntities = commonUserMapper.selectBatchIds(ids);
        if (CollectionUtils.isEmpty(userEntities)){
            return null;

        }        return userEntities.stream().collect(Collectors.toMap(UserEntity::getUserId, item -> item, (key1, key2) -> key2));
    }

    @Override
    public List<UserEntity> getUserListByDept(Long deptId) {
        if (deptId == null){
            throw new RuntimeException("部门不存在");
        }
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery(UserEntity.class);
        wrapper.eq(UserEntity::getDeptId,deptId);
        return commonUserMapper.selectList(wrapper);
    }
}
