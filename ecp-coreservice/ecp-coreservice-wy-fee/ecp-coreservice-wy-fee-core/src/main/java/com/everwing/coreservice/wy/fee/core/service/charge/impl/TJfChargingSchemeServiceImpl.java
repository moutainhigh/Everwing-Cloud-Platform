package com.everwing.coreservice.wy.fee.core.service.charge.impl;


import com.everwing.coreservice.common.wy.charging.entity.TJfChargingScheme;
import com.everwing.coreservice.common.wy.charging.service.TJfChargingSchemeService;

import java.util.List;

/**
 * @Auther: QHC
 * @Date: 2018/11/15 11:15
 * @Description: 计费方案基础信息相关业务接口
 */
public class TJfChargingSchemeServiceImpl implements TJfChargingSchemeService {

    /**
     * 根据主键删除计费方案基础信息
     *
     * @param companyId 公司id
     * @param id        计费方案id
     * @return 执行u结果
     */
    @Override
    public boolean deleteByPrimaryKey(String companyId, String id) {
        return false;
    }

    /**
     * 单条插入新增计费方案信息
     *
     * @param companyId 公司id
     * @param record    计费方案信息
     * @return 执行结果
     */
    @Override
    public boolean insert(String companyId, TJfChargingScheme record) {
        return false;
    }

    /**
     * 根据主键id查询计费方案信息  TODO 有待扩展
     *
     * @param companyId 公司id
     * @param id        计费方案主键id
     * @return 查询结果
     */
    @Override
    public TJfChargingScheme selectByPrimaryKey(String companyId, String id) {
        return null;
    }

    /**
     * 查询所有计费方案信息,  TODO 后面根据参数的不同进行扩展(加入不同参数)
     *
     * @param companyId 公司id
     * @return 查询集合信息
     */
    @Override
    public List<TJfChargingScheme> selectAll(String companyId) {
        return null;
    }

    /**
     * 单条更新计费方案信息
     *
     * @param companyId 公司id
     * @param record    计费方案信息
     * @return 执行结果
     */
    @Override
    public boolean updateByPrimaryKey(String companyId, TJfChargingScheme record) {
        return false;
    }
}
