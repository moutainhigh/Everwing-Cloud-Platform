package com.everwing.coreservice.wy.dao.mapper.sys;


import com.everwing.coreservice.common.wy.entity.system.company.TSysCompany;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanyList;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanySearch;
import org.springframework.dao.DataAccessException;

import java.util.List;


public interface TSysCompanyMapper {

    List<TSysCompanyList> listPage(TSysCompanySearch condition) throws DataAccessException;

    List<TSysCompanyList> findByCondition(TSysCompanySearch condition) throws DataAccessException;

    int insert(TSysCompany entity) throws DataAccessException;

    int modify(TSysCompany entity) throws DataAccessException;

    int delete(TSysCompany entity) throws DataAccessException;
}
