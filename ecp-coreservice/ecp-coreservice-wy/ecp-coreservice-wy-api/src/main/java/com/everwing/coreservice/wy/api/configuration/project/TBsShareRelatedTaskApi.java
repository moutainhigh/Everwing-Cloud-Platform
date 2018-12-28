package com.everwing.coreservice.wy.api.configuration.project;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareRelatedTask;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsShareRelatedTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("tBsShareRelatedTaskApi")
public class TBsShareRelatedTaskApi {

	
	@Autowired
	private TBsShareRelatedTaskService tBsShareRelatedTaskService;

	public RemoteModelResult<MessageMap> addShareTask(String companyId,TBsShareRelatedTask entity) {
		return new RemoteModelResult<MessageMap>(this.tBsShareRelatedTaskService.addShareTask(companyId,entity));
	}

	public RemoteModelResult<BaseDto> listPageShareTask(String companyId,TBsShareRelatedTask entity) {
		return new RemoteModelResult<BaseDto>(this.tBsShareRelatedTaskService.listPageShareTask(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> loadRelationTaskBuiling(String companyId,String projectId,String taskId) {
		return new RemoteModelResult<BaseDto>(this.tBsShareRelatedTaskService.loadRelationTaskBuiling(companyId,projectId,taskId));
	}
	
	public RemoteModelResult<MessageMap> deleteTaskById(String companyId,String id) {
		return new RemoteModelResult<MessageMap>(this.tBsShareRelatedTaskService.deleteTaskById(companyId, id));
	}
	
	
}
