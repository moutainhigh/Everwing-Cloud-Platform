package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.TBcCollectionTotal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
@Repository
public interface TBcCollectionMapper {

    TBcCollectionTotal findRecentTotal(@Param("projectId") String projectId, @Param("collectionType") Integer unionFlag);
}
