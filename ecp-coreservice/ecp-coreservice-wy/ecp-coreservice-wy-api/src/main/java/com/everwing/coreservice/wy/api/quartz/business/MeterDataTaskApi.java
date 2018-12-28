package com.everwing.coreservice.wy.api.quartz.business;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.service.quartz.business.MeterDataTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("meterDataTaskApi")
public class MeterDataTaskApi {

	@Autowired
	private MeterDataTaskService meterDataTaskService;
	
	public RemoteModelResult<BaseDto> scanSchedule(String companyId,String companyStr){
		return new RemoteModelResult<BaseDto>(this.meterDataTaskService.scanSchedule(companyId,companyStr));
	}
	
	public RemoteModelResult<BaseDto> autoCompleteTask(String companyId,String companyStr){
		return new RemoteModelResult<BaseDto>(this.meterDataTaskService.autoCompleteTask(companyId,companyStr));
	}
	
	public RemoteModelResult<BaseDto> productNextReadingTask(String companyId,TcReadingTask task){
		return new RemoteModelResult<BaseDto>(this.meterDataTaskService.productNextReadingTask(companyId, task));
	}
}
