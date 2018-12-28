package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsRuleBuildingRelation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/6/4.
 */
@Repository
public interface TBsRuleBuildingRelationMapper {

    List<TBsRuleBuildingRelation> selectByBuildingCode(String buildingCode);

    List<String> getBuildingCodeByRuleId(Map<String, Object> paramMap);
}
