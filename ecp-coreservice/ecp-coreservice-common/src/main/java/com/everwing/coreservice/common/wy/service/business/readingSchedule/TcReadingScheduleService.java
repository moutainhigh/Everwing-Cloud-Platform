package com.everwing.coreservice.common.wy.service.business.readingSchedule;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;

public interface TcReadingScheduleService {

	BaseDto listPage(String companyId, TcReadingSchedule tcReadingSchedule);

	BaseDto insert(String companyId, TcReadingSchedule tcReadingSchedule,Integer flag);

	BaseDto modify(String companyId, TcReadingSchedule tcReadingSchedule);

	BaseDto del(String companyId, String ids);

	BaseDto getInfoById(String companyId, String id);

	BaseDto batchModify(String companyId, TcReadingSchedule tcReadingSchedule);

	BaseDto findUsingSchedule(String companyId,TcReadingSchedule tcReadingSchedule);

}
