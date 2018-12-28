package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.cust.TBcCollection;
import org.springframework.stereotype.Repository;

/**
 * Created by DELL on 2018/6/4.
 */
@Repository
public interface CollectionMapper {

    TBcCollection findByBuildingCode(String buildingCode);

}
