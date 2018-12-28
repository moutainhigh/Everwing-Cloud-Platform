package com.everwing.coreservice.wy.fee.core.service.charge.impl;

import com.everwing.coreservice.common.wy.charging.entity.TJgChargingRuleDetailInfo;
import com.everwing.coreservice.common.wy.charging.service.TJgChargingRuleDetailInfoService;

import java.util.List;

/**
 * @Auther: QHC
 * @Date: 2018/11/15 11:37
 * @Description: 计费规则下的详细计费公式，基础信息这些实际用于计费的基础数据
 *                相关的基础数据维护业务接口
 */
public class TJgChargingRuleDetailInfoServiceImpl implements TJgChargingRuleDetailInfoService {

    /**
     * 根据主键删除计费规则详情基础信息
     *
     * @param companyId 公司id
     * @param id        计费规则详情id
     * @return 执行结果
     */
    @Override
    public boolean deleteByPrimaryKey(String companyId, String id) {
        return false;
    }

    /**
     * 单条插入新增计费规则详情信息
     *
     * @param companyId 公司id
     * @param record    计费规则详情信息
     * @return 执行结果
     */
    @Override
    public boolean insert(String companyId, TJgChargingRuleDetailInfo record) {
        return false;
    }

    /**
     * 根据主键id查询计费规则详情信息  TODO 有待扩展
     *
     * @param companyId 公司id
     * @param id        计费规则详情主键id
     * @return 查询结果
     */
    @Override
    public TJgChargingRuleDetailInfo selectByPrimaryKey(String companyId, String id) {
        return null;
    }

    /**
     * 查询所有计费规则详情信息,  TODO 后面根据参数的不同进行扩展(加入不同参数)
     *
     * @param companyId 公司id
     * @return 查询集合信息
     */
    @Override
    public List<TJgChargingRuleDetailInfo> selectAll(String companyId) {
        return null;
    }

    /**
     * 单条更新计费规则详情信息
     *
             * @param companyId 公司id
     * @param record    计费规则详情信息
     * @return 执行结果
     */
    @Override
    public boolean updateByPrimaryKey(String companyId, TJgChargingRuleDetailInfo record) {
        return false;
    }
}
