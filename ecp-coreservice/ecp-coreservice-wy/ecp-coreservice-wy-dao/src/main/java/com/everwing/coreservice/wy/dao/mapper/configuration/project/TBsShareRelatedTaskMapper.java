package com.everwing.coreservice.wy.dao.mapper.configuration.project;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBasicsInfo;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBuildingRelation;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareRelatedTask;

import java.util.List;
/**
 * @describe （物业，本体，水费，电费）方案相关
 * @author QHC
 *
 */
public interface TBsShareRelatedTaskMapper {

	List<TBsShareRelatedTask> listPageShareTask(TBsShareRelatedTask entity);
	
	int insertShareTask(TBsShareRelatedTask entity);
	
	TBsShareRelatedTask selectTaskByBasicId(String basicId,String taskName);
	
	int updateShareTask(TBsShareRelatedTask entity);
	
	List<TBsShareRelatedTask> getShareTaskByIds(List<TBsShareBasicsInfo> list);
	
	Double getTotalUsedAmount(String codeS,String codeE,String meterType);
	
	List<TBsShareBuildingRelation> getRightBuilingInfos(String shareTaskId);
	
	List<TBsShareBuildingRelation> getUseAmountByBuilding(String shareTaskId,String meterType);
	
	List<TBsShareBuildingRelation> getUseAmountByBuildingForElect(String shareTaskId,String meterType);
	
	Double getTotalUseAmountByTaskId(String shareTaskId,String meterType,String shareFrequency);
	
	Double getElectTotalUseAmountByTaskId(String shareTaskId,String meterType,String shareFrequency);
	
	int deleteTaskByShareId(String shareId);
	
	int deleteTaskById(String id);
	
	/**
	 * 根据id批量修改
	 * @return
	 */
//	int batchDelShares(List<String> ids);
	
}
