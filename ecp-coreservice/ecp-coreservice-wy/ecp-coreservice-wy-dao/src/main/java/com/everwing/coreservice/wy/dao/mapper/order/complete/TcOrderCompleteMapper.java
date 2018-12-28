package com.everwing.coreservice.wy.dao.mapper.order.complete;

import com.everwing.coreservice.common.wy.entity.order.TcOrderComplete;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface TcOrderCompleteMapper {

	int insert(TcOrderComplete entity) throws DataAccessException;

	TcOrderComplete findById(String id) throws DataAccessException;

	Map<String, Object> findDetailById(String id);
	
	/**
	 * 查找未计费的产权变更单
	 */
	List<Map<String,Object>> getNoBill(String buildCode,String projectId);
	
	/**
	 * 单个更新
	 */
	int singleUpdate(TcOrderComplete entity);
	
	/**
	 * 通过主键查询得到Map对象
	 * @param id
	 * @return
	 */
	Map<String,Object> findCompById(String id);
	
	/**
	 * 根据项目编号查找所有资产变更的未计费的工单
	 */
	List<Map<String,Object>> getAllNoBill(String projectId);
	
	/**
	 * 根据建筑编号、项目编号查询产权变更完成工单
	 */
	TcOrderComplete findByBuildCodeAndProjectId(@Param("buildCode") String buildCode,@Param("projectId") String projectId,@Param("meterType")Integer meterType);
}
