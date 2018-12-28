package com.everwing.coreservice.wy.dao.mapper.configuration.bc.project;

import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @TODO 托收项目
 * @author DELL
 *
 */
public interface TBcProjectMapper {
	
	int insert(TBcProject entity);

	List<TBcProject> listPage(TBcProject entity);
	
	List<String> getChargeTotalIds(String projectId);

	TBcProject findById(String id);

	int update(TBcProject entity) throws ECPBusinessException;

    TBcProject findByProjectId(String projectId);

	int updateUnionCountByProjectId(String projectId);

	int updateJrlCountByProjectId(String projectId);

	int deleteByCode(@Param("projectId") String code);
}
