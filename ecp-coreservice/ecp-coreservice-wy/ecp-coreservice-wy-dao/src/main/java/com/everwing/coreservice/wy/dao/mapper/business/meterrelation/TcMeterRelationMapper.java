package com.everwing.coreservice.wy.dao.mapper.business.meterrelation;

import com.everwing.coreservice.common.wy.entity.business.meterrelation.TcMeterRelation;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface TcMeterRelationMapper {

	List<TcMeterRelation> getRelationByMeterCode(TcMeterRelation entity) throws DataAccessException;
	
	int batchAdd(List<TcMeterRelation> relations) throws DataAccessException;
	
	int add(TcMeterRelation entity) throws DataAccessException;
	
	int del(TcMeterRelation entity) throws DataAccessException;
	
	int batchDelByObjs(List<TcMeterRelation> entities) throws DataAccessException;
	
	int batchDelByIds(List<String> ids) throws DataAccessException;
	
	Map<String,Long> countMetersByPositionList(Map<String,Object> paramMap) throws DataAccessException;

	void batchDelByscheduleIds(List<String> ids) throws DataAccessException;

	
	int batchDelByElectMeterCodes(List<String> ids) throws DataAccessException;
	
	int updateByrelationId(Map<String,String> map) throws DataAccessException;
	
	//获取任务对应的任务/建筑关系
	List<TcMeterRelation> getTaskRelationListByTaskId(String relationId) throws DataAccessException;
	
	//根据水电表id删除关联信息
	int bachDelByRelationIds(List<String> ids) throws DataAccessException;

	void delByRelationId(String id);

	List<String> findPositionByDatas(Map<String,Object> paramMap);
	
}
