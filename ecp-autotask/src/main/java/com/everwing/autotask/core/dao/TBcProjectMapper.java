package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import org.springframework.stereotype.Repository;

/**
 * 项目
 *
 * @author DELL shiny
 * @create 2018/6/1
 */
@Repository
public interface TBcProjectMapper {

    TBcProject findByProjectId(String projectId);

}
