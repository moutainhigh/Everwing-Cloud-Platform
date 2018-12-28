package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
@Repository
public interface TcWaterMeterMapper {

    Integer findMeterLevelBySomeParams(@Param("projectId") String projectId, @Param("buildingCode") String buildingCode, @Param("historyType")Integer historyType);

    TcWaterMeter findByBuildingCode(String projectId, String buildCode);

    List<TcWaterMeter> findByRelarionId(String projectId,String relationId);

    TcWaterMeter findMByBuildCodeAndProjectId(String projectId,String buildCode);

    TcWaterMeter getWaterMeterByCodeAndM(String meterCode,String projectId);

    TcWaterMeter findMByCCodeAndProjectId(String code,String projectId);

    TcWaterMeter getWaterMeterByCode(String meterCode, String projectId);

    List<String> findByparentCodeAndProjectId(String parentCode,String projectId);
}
