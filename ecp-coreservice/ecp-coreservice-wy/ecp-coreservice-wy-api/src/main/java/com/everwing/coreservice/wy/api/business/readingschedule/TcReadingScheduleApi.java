package com.everwing.coreservice.wy.api.business.readingschedule;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;
import com.everwing.coreservice.common.wy.service.business.readingSchedule.TcReadingScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TcReadingScheduleApi {

	@Autowired
	private TcReadingScheduleService tcReadingScheduleService;

	public RemoteModelResult<BaseDto> listPage(String companyId,TcReadingSchedule tcReadingSchedule) {
		return new RemoteModelResult<BaseDto>(this.tcReadingScheduleService.listPage(companyId,tcReadingSchedule));
	}

	public RemoteModelResult<BaseDto> insert(String companyId,TcReadingSchedule tcReadingSchedule,Integer flag) {
		return new RemoteModelResult<BaseDto>(this.tcReadingScheduleService.insert(companyId,tcReadingSchedule,flag));
	}

	public RemoteModelResult<BaseDto> modify(String companyId,TcReadingSchedule tcReadingSchedule) {
		return new RemoteModelResult<BaseDto>(this.tcReadingScheduleService.modify(companyId,tcReadingSchedule));
	}

	public RemoteModelResult<BaseDto> del(String companyId, String ids) {
		return new RemoteModelResult<BaseDto>(this.tcReadingScheduleService.del(companyId,ids));
	}

	public RemoteModelResult<BaseDto> getInfoById(String companyId, String id) {
		return new RemoteModelResult<BaseDto>(this.tcReadingScheduleService.getInfoById(companyId,id));
	}

	public RemoteModelResult<BaseDto> batchModify(String companyId,TcReadingSchedule tcReadingSchedule) {
		return new RemoteModelResult<BaseDto>(this.tcReadingScheduleService.batchModify(companyId,tcReadingSchedule));
	}

	public RemoteModelResult<BaseDto> findUsingSchedule(String companyId,TcReadingSchedule tcReadingSchedule) {
		return new RemoteModelResult<BaseDto>(this.tcReadingScheduleService.findUsingSchedule(companyId,tcReadingSchedule));
	}
	
}
