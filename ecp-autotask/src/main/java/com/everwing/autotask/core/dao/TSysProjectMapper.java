package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by DELL on 2018/6/4.
 */
@Repository
public interface TSysProjectMapper {

    TSysProject findByCode(String code);
    List<TSysProjectList> findByCondition(TSysProjectSearch condition);
}
