package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * @author: parus
 * @date: 2023/6/5 13:57
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 根据用户id取出用户信息
     * @param id
     * @return
     */
    UserEntity getUserInfoById(Long id);

    /**
     *  根据用户id 取出用户信息 返回Map
     * @param id
     * @return
     */
    Map<Long,UserEntity> getUserInfoByIds(List<Long> id);

    /**
     * 根据组织id取出所有用户信息list
     * @param deptId
     * @return
     */
    List<UserEntity> getUserListByDept(Long deptId);
}
