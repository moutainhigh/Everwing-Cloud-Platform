package com.everwing.coreservice.wy.fee.core.service.charge.impl;

import com.everwing.coreservice.common.wy.charging.entity.TJfChargingItemBaseInfo;
import com.everwing.coreservice.common.wy.charging.service.TJfChargingItemBaseInfoService;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author ：qhc
 * @date: 2018/11/15 11:01
 * @Description: 计费项基础信息业务实现类
 */
public class TJfChargingItemBaseInfoServiceImpl implements TJfChargingItemBaseInfoService {

    private static final Logger logger = Logger.getLogger(TJfChargingItemBaseInfoServiceImpl.class);

    /**
     * 根据主键删除计费项信息
     *
     * @param companyId 公司id
     * @param id        计费项id
     * @return 执行结果
     */
    @Override
    public boolean deleteByPrimaryKey(String companyId, String id) {
        return false;
    }

    /**
     * 单条插入新增计费项信息
     *
     * @param companyId 公司id
     * @param record    计费项信息
     * @return 执行结果
     */
    @Override
    public boolean insert(String companyId, TJfChargingItemBaseInfo record) {
        return false;
    }

    /**
     * 根据主键id查询计费项信息  TODO 有待扩展
     *
     * @param companyId 公司id
     * @param id        计费项主键id
     * @return 查询结果
     */
    @Override
    public TJfChargingItemBaseInfo selectByPrimaryKey(String companyId, String id) {
        return null;
    }

    /**
     * 查询所有计费项信息,  TODO 后面根据参数的不同进行扩展
     *
     * @param companyId 公司id
     * @return 查询集合
     */
    @Override
    public List<TJfChargingItemBaseInfo> selectAll(String companyId) {
        return null;
    }

    /**
     * 单条更新计费项信息
     *
     * @param companyId 公司id
     * @param record    计费项信息
     * @return 执行结果
     */
    @Override
    public boolean updateByPrimaryKey(String companyId, TJfChargingItemBaseInfo record) {
        return false;
    }
}
