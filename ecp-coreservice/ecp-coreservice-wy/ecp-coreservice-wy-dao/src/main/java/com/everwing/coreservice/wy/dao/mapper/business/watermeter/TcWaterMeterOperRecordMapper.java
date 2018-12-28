package com.everwing.coreservice.wy.dao.mapper.business.watermeter;

import com.everwing.coreservice.common.wy.entity.business.watermeter.TcHydroMeterOperationRecord;

import java.util.Map;

public interface TcWaterMeterOperRecordMapper {

	// 当前业务只有更换或启用，禁用的时候会使用到它插入一条数据信息 
	int addWaerMeterOperRecord(TcHydroMeterOperationRecord tcHydroMeterOperationRecord);
	
	
	//根据三个参数, 表编号, 周期的起始时间 , 来查找该表在该时间段内是否出现过更换水表事件
	TcHydroMeterOperationRecord getInfoByCodeAndTime(Map<String,Object> paramMap);
	
	
	
}
