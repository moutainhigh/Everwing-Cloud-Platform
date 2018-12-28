package com.everwing.coreservice.wy.fee.core.service.charge.impl;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.charging.dto.AbnormalRuleBindingDto;
import com.everwing.coreservice.common.wy.charging.entity.TJgChargingRuleInfo;
import com.everwing.coreservice.common.wy.charging.service.TJgChargingRuleInfoService;
import com.everwing.coreservice.wy.charge.dao.mapper.TJfRuleBuildingRelationInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Auther: qhc
 * @Date: 2018/11/15 11:29
 * @Description: 计费规则汇总信息相关业务接口
 */
public class TJgChargingRuleInfoServiceImpl implements TJgChargingRuleInfoService {

    @Autowired
    private TJfRuleBuildingRelationInfoMapper tJfRuleBuildingRelationInfoMapper;


    /**
     * 根据主键删除计费规则汇总基础信息
     *
     * @param companyId 公司id
     * @param id        计费规则id
     * @return 执行结果
     */
    @Override
    public boolean deleteByPrimaryKey(String companyId, String id) {
        return false;
    }

    /**
     * 单条插入新增计费规则信息
     *
     * @param companyId 公司id
     * @param record    计费规则信息
     * @return 执行结果
     */
    @Override
    public boolean insert(String companyId, TJgChargingRuleInfo record) {
        return false;
    }

    /**
     * 根据主键id查询计费规则信息  TODO 有待扩展
     *
     * @param companyId 公司id
     * @param id        计费规则主键id
     * @return 查询结果
     */
    @Override
    public TJgChargingRuleInfo selectByPrimaryKey(String companyId, String id) {
        return null;
    }

    /**
     * 查询所有计费规则信息,  TODO 后面根据参数的不同进行扩展(加入不同参数)
     *
     * @param companyId 公司id
     * @return 查询集合信息
     */
    @Override
    public List<TJgChargingRuleInfo> selectAll(String companyId) {
        return null;
    }

    /**
     * 单条更新计费规则信息
     *
     * @param companyId 公司id
     * @param record    计费规则信息
     * @return 执行结果
     */
    @Override
    public boolean updateByPrimaryKey(String companyId, TJgChargingRuleInfo record) {
        return false;
    }

    /**
     * 计费规则绑定异常，计费项下各项为计费，建筑未关联计费规则
     * @param companyId
     * @param abnormalRuleBindingDto
     * @return
     */
    public BaseDto listPageAbnormalRuleBinding(String companyId, AbnormalRuleBindingDto abnormalRuleBindingDto){

        if(CommonUtils.isEmpty(abnormalRuleBindingDto)){

            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"传入参数为空"));
        }

        List<AbnormalRuleBindingDto> list = tJfRuleBuildingRelationInfoMapper.listPageAbnormalRuleBinding(abnormalRuleBindingDto);





        return null;
    };
}
