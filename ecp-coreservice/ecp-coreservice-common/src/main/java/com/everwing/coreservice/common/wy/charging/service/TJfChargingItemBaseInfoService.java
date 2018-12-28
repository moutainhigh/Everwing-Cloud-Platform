package com.everwing.coreservice.common.wy.charging.service;


import com.everwing.coreservice.common.wy.charging.entity.TJfChargingItemBaseInfo;

import java.util.List;

/**
 * @Auther: qhc
 * @Date: 2018/11/15 10:41
 * @Description: 计费项相关的业务操作接口
 */
public interface TJfChargingItemBaseInfoService {

    /**
     * 根据主键删除计费项信息
     * @param companyId 公司id
     * @param id  计费项id
     * @return 执行结果
     */
    boolean deleteByPrimaryKey(String companyId, String id);

    /**
     * 单条插入新增计费项信息
     * @param companyId 公司id
     * @param record 计费项信息
     * @return 执行结果
     */
    boolean insert(String companyId, TJfChargingItemBaseInfo record);

    /**
     * 根据主键id查询计费项信息  TODO 有待扩展
     * @param companyId 公司id
     * @param id 计费项主键id
     * @return 查询结果
     */
    TJfChargingItemBaseInfo selectByPrimaryKey(String companyId, String id);

    /**
     * 查询所有计费项信息,  TODO 后面根据参数的不同进行扩展
     * @param companyId 公司id
     * @return 查询集合
     */
    List<TJfChargingItemBaseInfo> selectAll(String companyId);

    /**
     * 单条更新计费项信息
     * @param companyId 公司id
     * @param record 计费项信息
     * @return 执行结果
     */
    boolean updateByPrimaryKey(String companyId, TJfChargingItemBaseInfo record);

}
