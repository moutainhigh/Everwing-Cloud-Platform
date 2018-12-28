package com.everwing.coreservice.wy.dao.mapper.configuration;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface TBsProjectMapper {

	List<TBsProject> listPageProject(TBsProject project);

	TBsProject findById(String id);

	int update(TBsProject project);

	Map<String,Object> countProject();
	
	int insert(TBsProject project);
	
	int batchInsert(List<TBsProject> projects);

	List<TBsProject> findUsingProject();
	
	TBsProject findByObj(TBsProject project);

	Integer updateWhenTSysProjectUpdate(TBsProject tBsProject);

	List<TBsProject> findNeedGenProjects();

	//获取可计费的项目集合
	List<TBsProject> findCanBillingProjects(Map<String,Object> paramObj);
	
	//获取可计费的项目集合（分摊用）
	List<TBsProject> findShareBillingProjects(Map<String,Object> paramObj);

	List<TBsProject> findCanBillingCmacProject();

	List<TBsProject> findAllByObj(TBsProject paramObj);

	//项目进行整体聚合
	void updateFee(TBsProject paramProject);

	List<TBsProject> findCanReGenBillingProject(Map<String,Object> paramMap);

	List<TBsProject> findCanGenBillProject();
	
	/**
	 * 根据水电费计费状态、项目编号和计费时间来查询
	 */
	
	TBsProject findByItemStatueAndBilltime(Map<String,Object> map);
	
	/**
	 * 根据计费状态和ID来查询
	 */
	TBsProject findByItemStatueAndId(TBsProject tBsProject);
	
	/**
	 * 查询能计费的项目
	 */
	List<TBsProject> findCanNewBillingProjects(Map<String,Object> paramObj);
	
	/**
	 * 查当月项目
	 */
	TBsProject findCurrentMonthAndBilltime (@Param("billingTime")String billingTime,@Param("projectId")String projectId);
	
	/**
	 * 查询所有项目
	 */
	List<TBsProject> findCurrentAllUseProjects(@Param("billingTime")String billingTime);

	int deleteByCode(@Param("projectId") String code);

	void flushItems(@Param("projectId") String projectId) throws DataAccessException;

	Integer findCommonStatus(@Param("userId") String userId);

	TBsProject findCurrentProjectByBuildingCode(@Param("buildingCode") String buildingCode);
}
