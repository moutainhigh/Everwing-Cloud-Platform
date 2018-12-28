package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/6/1.
 */
@Repository
public interface TBsOwedHistoryMapper {

    List<TBsOwedHistory> findAllByAccountId(String id);

    int batchInsert(List<TBsOwedHistory> entities);

    int batchUpdate(List<TBsOwedHistory> list);

    List<TBsOwedHistory> findUsingDatas(TBsOwedHistory paramObj);

    List<TBsOwedHistory> findNotOnJrlCollingDatas(@Param("projectId") String projectId, @Param("totalId") String totalId);

    List<TBsOwedHistory> findNotOnUnionCollingDatas(@Param("projectId") String projectId,@Param("totalId") String totalId);

    Double findSumLateFeeByBuildingCode(String buildingCode);

    Double findSumByBuildingCodeAndType(@Param("buildingCode") String buildingCode,@Param("type") Integer type);

    List<Map<String,Object>> findSomeInfoByAccountId(String id);
}
