package com.everwing.coreservice.wy.dao.mapper.property;

import com.everwing.coreservice.common.wy.entity.property.publicbuilding.TcPublicBuilding;
import com.everwing.coreservice.common.wy.entity.property.publicbuilding.TcPublicBuildingList;
import com.everwing.coreservice.common.wy.entity.property.publicbuilding.TcPublicBuildingSearch;

import java.util.List;

public interface TcPublicBuildingMapper {
    List<TcPublicBuildingList> listPage(TcPublicBuildingSearch tcPublicBuildingSearch);

    List<TcPublicBuildingList> findByCondition(TcPublicBuildingSearch tcPublicBuildingSearch);

    int insert(TcPublicBuilding entity);

    int batchInsert(List<TcPublicBuilding> list);

    int modify(TcPublicBuilding entity);

    int batchModify(List<TcPublicBuilding> list);

    int batchDelete(List<String> list);
}