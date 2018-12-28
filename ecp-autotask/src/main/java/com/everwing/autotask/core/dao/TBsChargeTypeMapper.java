package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargeType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
@Repository
public interface TBsChargeTypeMapper {

    List<TBsChargeType> selectChargeType(String ruleId);
}
