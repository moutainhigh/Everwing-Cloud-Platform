package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.fee.entity.AcLastBillFeeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AcLastBillFeeInfoMapper {

    List<AcLastBillFeeInfo>  selectByProjectAndAccountType(@Param("projectId") String projectId,@Param("accountType") int accountType);

}