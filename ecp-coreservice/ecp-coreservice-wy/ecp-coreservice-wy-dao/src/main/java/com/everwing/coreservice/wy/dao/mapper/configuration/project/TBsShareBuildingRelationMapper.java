package com.everwing.coreservice.wy.dao.mapper.configuration.project;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBuildingRelation;

import java.util.List;
import java.util.Map;
/**
 * @describe （物业，本体，水费，电费）方案相关
 * @author QHC
 *
 */
public interface TBsShareBuildingRelationMapper {

	List<TBsShareBuildingRelation> selectAssetsByTaskId(String taskId);
	
	int batchInsert(List<TBsShareBuildingRelation> datas);
	
	/**
	 * 根据id批量修改
	 * @return
	 */
	int deleteRelationBuilding(String taskId);
	
	List<Map<String, String>> loadRelationTaskBuiling(String projectId,String taskId);
	
	int deleteByShareBasicId(String shareId);
	
}
