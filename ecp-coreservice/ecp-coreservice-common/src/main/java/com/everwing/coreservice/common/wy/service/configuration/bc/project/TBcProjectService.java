package com.everwing.coreservice.common.wy.service.configuration.bc.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;

public interface TBcProjectService {

	BaseDto listPage(String companyId, TBcProject entity);

	BaseDto getById(String companyId, String id);

	BaseDto update(String companyId, TBcProject entity);

	BaseDto findDataPerYear(String companyId, TBcProject entity);

	BaseDto getByCode(String companyId, String projectId);

}
