package com.everwing.coreservice.wy.dao.mapper.sys;


import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartment;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartmentList;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartmentSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;


public interface TSysDepartmentMapper {

    List<TSysDepartmentList> listPage(TSysDepartmentSearch condition) throws DataAccessException;
    
    List<TSysDepartment> findByCondition(TSysDepartmentSearch condition) throws DataAccessException;

    int insert(TSysDepartment entity) throws DataAccessException;

    int modify(TSysDepartment entity) throws DataAccessException;

    int delete(TSysDepartment entity) throws DataAccessException;
}
