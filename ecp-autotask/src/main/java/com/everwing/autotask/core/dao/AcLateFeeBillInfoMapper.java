package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.fee.entity.AcLateFeeBillInfo;
import org.apache.ibatis.annotations.Param;

public interface AcLateFeeBillInfoMapper {

    AcLateFeeBillInfo getAcLateFeeBillInfoByProject(@Param("projectId") String projectId,@Param("accountType") int accountType);

}