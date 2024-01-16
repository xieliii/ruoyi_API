package com.ruoyi.test.service.impl;

import java.util.List;

import com.ruoyi.test.domain.Test;
import com.ruoyi.test.mapper.TestMapper;
import com.ruoyi.test.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
@Service
public class TestServiceImpl implements ITestService
{
    @Autowired
    private TestMapper testMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Test selectTestById(String id)
    {
        return testMapper.selectTestById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param test 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Test> selectTestList(Test test)
    {
        return testMapper.selectTestList(test);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param test 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTest(Test test)
    {
        return testMapper.insertTest(test);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param test 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTest(Test test)
    {
        return testMapper.updateTest(test);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTestByIds(String[] ids)
    {
        return testMapper.deleteTestByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteTestById(String id)
    {
        return testMapper.deleteTestById(id);
    }
}
