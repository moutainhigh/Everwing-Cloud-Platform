package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
@Repository
public interface TcBuildingMapper {

    List<TcBuildingList> findByCondition(TcBuildingSearch condition);

    List<TcBuilding> findHasBillsBuildings(@Param("projectId") String projectId, @Param("billingTime") Date billingTime, @Param("buildingCode") String buildingCode);

    List<TcBuilding> findChargeBuildingByProjectCode(String projectId);
    
    String getHouseCodeByBuildingCode(String buildingCode);
}
