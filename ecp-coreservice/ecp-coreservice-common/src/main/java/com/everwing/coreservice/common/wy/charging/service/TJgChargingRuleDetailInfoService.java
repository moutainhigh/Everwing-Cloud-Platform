package com.everwing.coreservice.common.wy.charging.service;

import com.everwing.coreservice.common.wy.charging.entity.TJgChargingRuleDetailInfo;

import java.util.List;

/**
 * @Auther: QHC
 * @Date: 2018/11/15 11:37
 * @Description: 计费规则下的详细计费公式，基础信息这些实际用于计费的基础数据
 *                相关的基础数据维护业务接口
 */
public interface TJgChargingRuleDetailInfoService {

    /**
     * 根据主键删除计费规则详情基础信息
     * @param companyId 公司id
     * @param id  计费规则详情id
     * @return 执行结果
     */
    boolean deleteByPrimaryKey(String companyId, String id);

    /**
     * 单条插入新增计费规则详情信息
     * @param companyId 公司id
     * @param record  计费规则详情信息
     * @return 执行结果
     */
    boolean insert(String companyId, TJgChargingRuleDetailInfo record);

    /**
     * 根据主键id查询计费规则详情信息  TODO 有待扩展
     * @param companyId 公司id
     * @param id 计费规则详情主键id
     * @return 查询结果
     */
    TJgChargingRuleDetailInfo selectByPrimaryKey(String companyId, String id);

    /**
     * 查询所有计费规则详情信息,  TODO 后面根据参数的不同进行扩展(加入不同参数)
     * @param companyId 公司id
     * @return 查询集合信息
     */
    List<TJgChargingRuleDetailInfo> selectAll(String companyId);

    /**
     * 单条更新计费规则详情信息
     * @param companyId 公司id
     * @param record 计费规则详情信息
     * @return 执行结果
     */
    boolean updateByPrimaryKey(String companyId, TJgChargingRuleDetailInfo record);

}
