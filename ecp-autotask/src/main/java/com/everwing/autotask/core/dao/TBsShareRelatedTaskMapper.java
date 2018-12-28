package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBasicsInfo;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBuildingRelation;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareRelatedTask;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by DELL on 2018/6/4.
 */
@Repository
public interface TBsShareRelatedTaskMapper {

    List<TBsShareRelatedTask> getShareTaskByIds(List<TBsShareBasicsInfo> list);

    List<TBsShareBuildingRelation> getRightBuilingInfos(String shareTaskId);

    Double getElectTotalUseAmountByTaskId(String shareTaskId,String meterType,String shareFrequency);

    List<TBsShareBuildingRelation> getUseAmountByBuildingForElect(String shareTaskId,String meterType);

    Double getTotalUseAmountByTaskId(String shareTaskId,String meterType,String shareFrequency);

    List<TBsShareBuildingRelation> getUseAmountByBuilding(String shareTaskId,String meterType);
}
