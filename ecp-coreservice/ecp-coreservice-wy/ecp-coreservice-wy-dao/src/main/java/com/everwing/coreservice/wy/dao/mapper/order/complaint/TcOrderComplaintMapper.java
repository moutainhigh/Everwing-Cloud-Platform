package com.everwing.coreservice.wy.dao.mapper.order.complaint;

import com.everwing.coreservice.common.wy.entity.order.TcOrderComplaint;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface TcOrderComplaintMapper {

	List<TcOrderComplaint> listPage(TcOrderComplaint entity) throws DataAccessException;

	void insert(TcOrderComplaint entity) throws DataAccessException;

	int update(TcOrderComplaint complaint) throws DataAccessException;

	TcOrderComplaint findById(String complaintOrderId) throws DataAccessException;

	List<TcOrderComplaint> findByObj(TcOrderComplaint obj) throws DataAccessException;

	/**
	 * 根据类型(水电表)，buildCode、create_time和项目来查询
	 */
	
	TcOrderComplaint findByTypeAndBuildCode(String type,String buildCode,String createTime,String projectId);
	
	/**
	 * 根据工单号查询工单
	 */
	Map<String,Object>  findByOrderCode(String projectId,String orderCode);
	
	/**
	 * 根据buildCode和meter_type查找最新的两条数据；因为可能有重抄表的情况，所以找最新两期数据
	 */
	List<Map<String,Object>> findByBuildAndMeterType(String projectId,String buildCode,String meterType);
	
	/**
	 * 根据buildCode projcetid查找 主要是查找没有完成的重抄单
	 */
	TcOrderComplaint findbyBuildCodeAndProjectId(String projectId,String buildCode);
	
	/**
	 * 根据orderCode和projectId查询
	 */
	TcOrderComplaint findTcOrderByOrderCode(String projectId,String orderCode);
}
