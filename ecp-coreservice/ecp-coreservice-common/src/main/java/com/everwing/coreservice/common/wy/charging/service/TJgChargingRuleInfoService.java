package com.everwing.coreservice.common.wy.charging.service;


import com.everwing.coreservice.common.wy.charging.entity.TJgChargingRuleInfo;

import java.util.List;

/**
 * @Auther: qhc
 * @Date: 2018/11/15 11:29
 * @Description: 计费规则汇总信息相关业务接口
 */
public interface TJgChargingRuleInfoService {

    /**
     * 根据主键删除计费规则汇总基础信息
     * @param companyId 公司id
     * @param id  计费规则id
     * @return 执行结果
     */
    boolean deleteByPrimaryKey(String companyId, String id);

    /**
     * 单条插入新增计费规则信息
     * @param companyId 公司id
     * @param record  计费规则信息
     * @return 执行结果
     */
    boolean insert(String companyId, TJgChargingRuleInfo record);

    /**
     * 根据主键id查询计费规则信息  TODO 有待扩展
     * @param companyId 公司id
     * @param id 计费规则主键id
     * @return 查询结果
     */
    TJgChargingRuleInfo selectByPrimaryKey(String companyId, String id);

    /**
     * 查询所有计费规则信息,  TODO 后面根据参数的不同进行扩展(加入不同参数)
     * @param companyId 公司id
     * @return 查询集合信息
     */
    List<TJgChargingRuleInfo> selectAll(String companyId);

    /**
     * 单条更新计费规则信息
     * @param companyId 公司id
     * @param record 计费规则信息
     * @return 执行结果
     */
    boolean updateByPrimaryKey(String companyId, TJgChargingRuleInfo record);

}
