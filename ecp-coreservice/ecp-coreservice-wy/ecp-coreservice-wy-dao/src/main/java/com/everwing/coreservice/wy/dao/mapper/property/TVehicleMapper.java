package com.everwing.coreservice.wy.dao.mapper.property;

import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicle;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicleList;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicleSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface TVehicleMapper {
    List<TVehicleList> listPage(TVehicleSearch tBsVehicleSearch) throws DataAccessException;

    List<TVehicleList> findByCondition(TVehicleSearch tBsVehicleSearch) throws DataAccessException;

    int insert(TVehicle tBsVehicle) throws DataAccessException;

    int update(TVehicle tBsVehicle) throws DataAccessException;

    int delete(TVehicle tBsVehicle) throws DataAccessException;
}
