package com.everwing.coreservice.wy.fee.dao.mapper;


import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;


public interface TSysProjectMapper {

    
    /**
     * 根据code查询 codeye 保证唯一
     */
    TSysProject findByCode(String code);
    TSysProject findByProjectId(String projectId);
}
