package com.everwing.coreservice.wy.fee.core.service.charge.impl;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.charging.entity.TJfChargingItemTotalInfo;
import com.everwing.coreservice.common.wy.charging.service.TJfChargingItemTotalInfoService;

import java.util.List;

/**
 * @Auther: qhc
 * @Date: 2018/11/15 16:24
 * @Description: 单个计费项每个周期的汇总信息。也就是计费的入口（包括自动计费和手动计费）
 */
public class TJfChargingItemTotalInfoServiceImpl implements TJfChargingItemTotalInfoService {
    /**
     * 根据公司进行每个周期，单个计费项的汇总信息生成
     * 每月放在定时任务去做，如果定时任务失败，需要手动生成走这里（这里会统计一下会进行计费的家数，如果家数都不对，没必要开始计费）
     * @param companyId 公司id
     * @return 执行结果
     */
    @Override
    public MessageMap insertTotalInfoByCompany(String companyId) {
        return null;
    }

    /**
     * @param companyId 公司id
     * @param entity    总单id
     * @return 执行结果
     */
    @Override
    public MessageMap updateTotalInfoById(String companyId, TJfChargingItemTotalInfo entity) {
        return null;
    }

    /**
     * 根据项目id查询需要展示的单个计费项的每个周期的信息
     *
     * @param companyId 公司id
     * @param projectId 需要查询项目id
     * @return
     */
    @Override
    public List<TJfChargingItemTotalInfo> selectTJfChargingItemTotalInfosByProjectId(String companyId, String projectId) {
        return null;
    }

    /**
     * 根据计费项总单id，进行对单个计费项的所有需要计费数据进行计费操作
     *
     * @param companyId 公司id
     * @param totalId   计费项本周期总单的id
     * @return 执行结果
     */
    @Override
    public MessageMap beginChargingByTotalId(String companyId, String totalId) {
        return null;
    }

    /**
     * 根据计费项总单id，进行对单个计费项的计费数据结果进行审核操作
     *
     * @param companyId 公司id
     * @param totalId   计费项本周期总单的id
     * @return 执行结果
     */
    @Override
    public MessageMap beginAudiByTotalId(String companyId, String totalId) {
        return null;
    }


}
