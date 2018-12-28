package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.fee.entity.AcCommonAccountDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface AcCommonAccountDetailMapper {

    List<AcCommonAccountDetail> getPushDataForCommonAccount(Map<String, String> paramMap);

}