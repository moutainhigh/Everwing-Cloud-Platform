package com.everwing.coreservice.common.wy.service.quartz.business;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;

public interface MeterDataTaskService {

	BaseDto scanSchedule(String companyId, String companyStr);

	BaseDto autoCompleteTask(String companyId, String companyStr);
	
	BaseDto productNextReadingTask(String companyId,TcReadingTask task);

}
