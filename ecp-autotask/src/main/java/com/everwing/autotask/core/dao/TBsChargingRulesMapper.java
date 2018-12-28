package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingRules;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
@Repository
public interface TBsChargingRulesMapper {

    List<TBsChargingRules> getTBsChargingRulesBySchemeId(String chargingSchemeId);
}
