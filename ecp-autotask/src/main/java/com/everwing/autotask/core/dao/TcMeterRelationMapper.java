package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.business.meterrelation.TcMeterRelation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/6/5.
 */
@Repository
public interface TcMeterRelationMapper {

    List<String> findPositionByDatas(Map<String,Object> paramMap);

    int batchAdd(List<TcMeterRelation> relations);

    List<TcMeterRelation> getTaskRelationListByTaskId(String relationId);
}
