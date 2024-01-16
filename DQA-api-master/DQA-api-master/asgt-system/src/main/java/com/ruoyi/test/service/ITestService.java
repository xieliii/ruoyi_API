package com.ruoyi.test.service;

import com.ruoyi.test.domain.Test;

import java.util.List;
/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2022-12-12
 */
public interface ITestService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public Test selectTestById(String id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param test 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<Test> selectTestList(Test test);

    /**
     * 新增【请填写功能名称】
     * 
     * @param test 【请填写功能名称】
     * @return 结果
     */
    public int insertTest(Test test);

    /**
     * 修改【请填写功能名称】
     * 
     * @param test 【请填写功能名称】
     * @return 结果
     */
    public int updateTest(Test test);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteTestByIds(String[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTestById(String id);
}
