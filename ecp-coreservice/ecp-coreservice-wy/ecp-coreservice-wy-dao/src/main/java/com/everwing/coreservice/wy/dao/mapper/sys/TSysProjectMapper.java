package com.everwing.coreservice.wy.dao.mapper.sys;


import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;


public interface TSysProjectMapper {

    List<TSysProjectList> listPage(TSysProjectSearch condition) throws DataAccessException;

    List<TSysProjectList> findByCondition(TSysProjectSearch condition) throws DataAccessException;

    Map getSummaryInformationByProjectId(String projectId) throws DataAccessException;

    int insert(TSysProject entity) throws DataAccessException;

    int modify(TSysProject entity) throws DataAccessException;

    int delete(TSysProject entity) throws DataAccessException;


    List<TSysProjectList> listPageAndCountMeters(TSysProjectSearch condition) throws DataAccessException;
    
    /**
     * 根据code查询 codeye 保证唯一
     */
    TSysProject findByCode(String code) throws DataAccessException;
}
