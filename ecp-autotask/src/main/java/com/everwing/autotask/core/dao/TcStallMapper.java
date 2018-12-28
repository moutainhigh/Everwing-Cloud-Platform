package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.property.stall.TcStall;
import org.springframework.stereotype.Repository;

/**
 * @author DELL shiny
 * @create 2018/6/5
 */
@Repository
public interface TcStallMapper {

    TcStall findByBuildingCode(String buildingCode);

}
