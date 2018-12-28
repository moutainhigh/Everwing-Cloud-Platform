package com.everwing.coreservice.wy.dao.mapper.sys;


import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganization;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganizationList;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganizationSearch;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;


public interface TSysOrganizationMapper {

    List<TSysOrganizationList> findOrganizationTree(TSysOrganizationSearch condition) throws DataAccessException;
    
    List<TSysOrganization> findByCondition(TSysOrganizationSearch condition) throws DataAccessException;

    List<TSysOrganizationList> findRolesByStaffNumber(String staffNumber) throws DataAccessException;

    int insert(TSysOrganization entity) throws DataAccessException;

    int modify(TSysOrganization entity) throws DataAccessException;

    int delete(TSysOrganization entity) throws DataAccessException;

    List<Map<String,Object>> selectByCAndP(@Param("companyId") String companyId, @Param("projectId") String project) throws DataAccessException;
}
