package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
@Repository
public interface TBsChargingSchemeMapper {

    List<Map<String, Object>> findCurrentRate(String projectId);

    TBsChargingScheme findUsingScheme(TBsChargingScheme paramscheme);

    void autoStopScheme();

    BigDecimal findCurrentRateByProjectIdAndType(String projectId, Integer type);
}
