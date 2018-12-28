package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AcCommonAccountDetail;
import org.apache.ibatis.annotations.Param;

public interface AcCommonAccountDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcCommonAccountDetail record);

    int insertSelective(AcCommonAccountDetail record);

    AcCommonAccountDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcCommonAccountDetail record);

    int updateByPrimaryKey(AcCommonAccountDetail record);
    AcCommonAccountDetail selectByOperaId( String operaId);


    double fingDeductionByDetail(@Param("code") int code, @Param("projectId") String projectId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    AcCommonAccountDetail selectHouseCodeAnd(String houseCode);
}