package com.everwing.coreservice.common.wy.service.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareRelatedTask;

public interface TBsShareRelatedTaskService {
	
	MessageMap addShareTask(String companyId,TBsShareRelatedTask entity);
	
	BaseDto listPageShareTask(String companyId,TBsShareRelatedTask entity);

	BaseDto loadRelationTaskBuiling(String companyId,String projectId,String taskId);
	
	MessageMap deleteTaskById(String companyId,String id);
	
}
