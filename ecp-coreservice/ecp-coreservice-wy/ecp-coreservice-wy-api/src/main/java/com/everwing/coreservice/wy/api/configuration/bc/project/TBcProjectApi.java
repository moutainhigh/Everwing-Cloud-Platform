package com.everwing.coreservice.wy.api.configuration.bc.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.service.configuration.bc.project.TBcProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tBcProjectApi")
public class TBcProjectApi {

	@Autowired
	private TBcProjectService tBcProjectService;

	public RemoteModelResult<BaseDto> listPage(String companyId,TBcProject entity) {
		return new RemoteModelResult<BaseDto>(this.tBcProjectService.listPage(companyId,entity));
	}

	public RemoteModelResult<BaseDto> getById(String companyId, String id) {
		return new RemoteModelResult<BaseDto>(this.tBcProjectService.getById(companyId,id));
	}

	public RemoteModelResult<BaseDto> update(String companyId, TBcProject entity) {
		return new RemoteModelResult<BaseDto>(this.tBcProjectService.update(companyId,entity)); 
	}

	public RemoteModelResult<BaseDto> findDataPerYear(String companyId,TBcProject entity) {
		return new RemoteModelResult<BaseDto>(this.tBcProjectService.findDataPerYear(companyId,entity)); 
	}

	public RemoteModelResult<BaseDto> getByCode(String companyId, String projectId) {
		return new RemoteModelResult<BaseDto>(this.tBcProjectService.getByCode(companyId,projectId));
	}

}
