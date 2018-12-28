package com.everwing.coreservice.wy.dao.mapper.business.readingschedule;

import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface TcReadingScheduleMapper {

	List<TcReadingSchedule> listPage(TcReadingSchedule entity) throws DataAccessException;
	
	int add(TcReadingSchedule entity)  throws DataAccessException;
	
	int modify(TcReadingSchedule entity)  throws DataAccessException;
	
	int del(List<String> ids)  throws DataAccessException;

	TcReadingSchedule getScheduleById(String id) throws DataAccessException;

	void batchModify(Map<String, Object> params) throws DataAccessException;

	Map<String, Object> getMaxScheduleCode() throws DataAccessException;
	
	//根据dataid获取计划
	TcReadingSchedule findScheduleByDataId(String id) throws DataAccessException;
	
	//获取计划
	List<TcReadingSchedule> findUsingSchedule(TcReadingSchedule entity) throws DataAccessException;

	Integer getMeterCountByScheduleId(Map<String, Object> paramMap);

	void stopFailedSchedule() throws DataAccessException;
	
	String findScheduleByProjectIdAndMterType(TcReadingSchedule t) throws DataAccessException;
}
