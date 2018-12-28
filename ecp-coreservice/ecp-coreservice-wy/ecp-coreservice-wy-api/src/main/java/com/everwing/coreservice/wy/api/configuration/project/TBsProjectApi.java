package com.everwing.coreservice.wy.api.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("tBsProjectApi")
public class TBsProjectApi {

	
	@Autowired
	private TBsProjectService tBsProjectService;

	public RemoteModelResult<BaseDto> listPageBsProject(String companyId,TBsProject project) {
		return new RemoteModelResult<BaseDto>(this.tBsProjectService.listPageProject(companyId,project));
	}

	public RemoteModelResult<BaseDto> findById(String companyId, String id) {
		return new RemoteModelResult<BaseDto>(this.tBsProjectService.findById(companyId,id));
	}

	public RemoteModelResult<BaseDto> update(String companyId,TBsProject project) {
		return new RemoteModelResult<BaseDto>(this.tBsProjectService.update(companyId,project));
	}

	public RemoteModelResult<BaseDto> countProject(String companyId) {
		return new RemoteModelResult<BaseDto>(this.tBsProjectService.countProject(companyId));
	}

	public RemoteModelResult<BaseDto> manualBilling(String companyId,List<TBsProject> projects) {
		return new RemoteModelResult<BaseDto>(this.tBsProjectService.manualBilling(companyId,projects));
	}

	public RemoteModelResult<BaseDto> findCommonStatus(String companyId,String userId) {
		return new RemoteModelResult<BaseDto>(this.tBsProjectService.findCommonStatus(companyId,userId));
	}
	
}
