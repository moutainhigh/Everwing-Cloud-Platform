package com.everwing.coreservice.wy.dao.mapper.account;

import com.everwing.coreservice.common.wy.fee.entity.AcCommonAccountDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AcCommonAccountDetailMapper {

    List<AcCommonAccountDetail> getPushDataForCommonAccount(Map<String, String> paramMap);

}