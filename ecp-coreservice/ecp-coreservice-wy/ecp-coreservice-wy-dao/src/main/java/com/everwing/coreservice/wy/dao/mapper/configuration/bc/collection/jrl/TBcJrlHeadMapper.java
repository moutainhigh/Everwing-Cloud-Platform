package com.everwing.coreservice.wy.dao.mapper.configuration.bc.collection.jrl;

import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlHead;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;


public interface TBcJrlHeadMapper {

	List<TBcJrlHead> listPage(TBcJrlHead head);

	List<TBcJrlHead> findByObj(TBcJrlHead paramHead);

	int update(TBcJrlHead head) throws DataAccessException;

	int insert(TBcJrlHead paramHead) throws DataAccessException;

	String findBatchNo(@Param("projectId") String projectId);

	TBcJrlHead findByTotalIdAndType(@Param("totalId") String totalId, @Param("type") Integer type);
}
