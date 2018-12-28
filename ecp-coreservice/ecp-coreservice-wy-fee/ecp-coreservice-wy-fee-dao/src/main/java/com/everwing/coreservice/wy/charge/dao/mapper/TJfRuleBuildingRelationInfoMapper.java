package com.everwing.coreservice.wy.charge.dao.mapper;


import com.everwing.coreservice.common.wy.charging.dto.AbnormalRuleBindingDto;
import com.everwing.coreservice.common.wy.charging.entity.TJfRuleBuildingRelationInfo;

import java.util.List;

/*
 *
 * 功能描述:
 *
 * @param:
 * @return:
 * @auther:
 * @date: 2018/11/14 10:43
 * @Description:
 **/
public interface TJfRuleBuildingRelationInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TJfRuleBuildingRelationInfo record);

    TJfRuleBuildingRelationInfo selectByPrimaryKey(String id);

    List<TJfRuleBuildingRelationInfo> selectAll();

    int updateByPrimaryKey(TJfRuleBuildingRelationInfo record);

    List<AbnormalRuleBindingDto> listPageAbnormalRuleBinding(AbnormalRuleBindingDto abnormalRuleBindingDto);
}