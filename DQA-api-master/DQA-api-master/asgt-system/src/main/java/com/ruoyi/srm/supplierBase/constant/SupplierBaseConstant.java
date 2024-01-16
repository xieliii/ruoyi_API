package com.ruoyi.srm.supplierBase.constant;

import io.swagger.models.auth.In;

/**
 * @author: dyw
 * @date: 2023/5/24 14:40
 */
public class SupplierBaseConstant {
    /**
     * 当前页数
     */
    public static final Integer PAGE_NUM = 1;
    /**
     * 页面大小
     */
    public static final Integer PAGE_SIZE = 10;
    /**
     * 删除
     */
    public static final Integer IS_DELETE = 1;
    /**
     * 未删除
     */
    public static final Integer IS_NOT_DELETE = 0;

    /**
     * 已入册
     */
    public static final Integer IS_ABLE = 1;

    /**
     * 未入册
     */
    public static final Integer IS_NOT_ABLE = 0;
    /**
     * 货源供应商
     */
    public static final Integer SOURCE = 1;
    /**
     * 货源供应商全称
     */
    public static final String SOURCE_FULL_NAME = "货源供应商";
    /**
     * 潜在供应商
     */
    public static final Integer POTENTIAL = 2;
    /**
     * 潜在供应商全称
     */
    public static final String POTENTIAL_FULL_NAME = "潜在供应商";
    /**
     * 获批供应商
     */
    public static final Integer APPROVED = 3;
    /**
     * 获批供应商全称
     */
    public static final String APPROVED_FULL_NAME = "获批供应商";

    public static final Integer ZERO = 0;

}
