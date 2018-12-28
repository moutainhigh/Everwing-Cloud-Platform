package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import org.springframework.stereotype.Repository;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
@Repository
public interface PersonCustNewMapper {

    PersonCustNew findNamesByBuildingCode(String buildingCode);
}
