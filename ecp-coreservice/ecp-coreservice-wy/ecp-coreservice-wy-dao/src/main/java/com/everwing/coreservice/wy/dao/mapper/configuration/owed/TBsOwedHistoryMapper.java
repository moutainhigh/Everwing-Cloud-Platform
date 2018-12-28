package com.everwing.coreservice.wy.dao.mapper.configuration.owed;

import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistoryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TBsOwedHistoryMapper {


	int insert(TBsOwedHistory entity);
	
	int update(TBsOwedHistory entity);
	
	int batchInsert(List<TBsOwedHistory> entities);

	List<TBsOwedHistory> findAllByAccountId(String accountId);

	List<TBsOwedHistory> findUsingDatas(TBsOwedHistory paramObj);

	List<Map<String, Object>> findSomeInfoByAccountId(String id);
	
	/**
	 * 批量修改
	 */
	int batchUpdate(List<TBsOwedHistory> list);

	TBsOwedHistory findUsingDataById(@Param("id") String id, @Param("isUsed") Integer isUsed);

	List<TBsOwedHistory> findNotOnJrlCollingDatas(@Param("projectId") String projectId,@Param("totalId") String totalId);

	List<TBsOwedHistory> findNotOnUnionCollingDatas(@Param("projectId") String projectId,@Param("totalId") String totalId);

	Double findSumByBuildingCodeAndType(@Param("buildingCode") String buildingCode,@Param("type") Integer type);

	Double findSumLateFeeByBuildingCode(String buildingCode);

	Double findSumLateFeeByAccountId(String accountId);

    TBsOwedHistory findUpDateByAccountId(String accountId);

	List<TBsOwedHistoryDto> fingListTBsOwedHistory(String projectId);

    TBsOwedHistoryDto findOweAndBuildingCodeAndTime(String buildingCode);

	List<TBsOwedHistoryDto> fingListThirdTBsOwedHistory(String projectCode);
}
