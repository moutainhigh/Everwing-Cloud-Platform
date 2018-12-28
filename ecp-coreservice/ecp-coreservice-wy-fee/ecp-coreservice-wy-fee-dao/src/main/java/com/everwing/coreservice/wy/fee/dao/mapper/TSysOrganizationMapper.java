package com.everwing.coreservice.wy.fee.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface TSysOrganizationMapper {


    List<String> selectProjectId(@Param("staffCode") String staffCode);
}
