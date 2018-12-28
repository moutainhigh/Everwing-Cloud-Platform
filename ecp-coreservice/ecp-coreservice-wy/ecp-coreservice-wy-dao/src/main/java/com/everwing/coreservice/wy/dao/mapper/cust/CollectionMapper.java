package com.everwing.coreservice.wy.dao.mapper.cust;

import com.everwing.coreservice.common.wy.entity.cust.TBcCollection;
import com.everwing.coreservice.common.wy.entity.cust.TBcCollectionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectionMapper {
    long countByExample(TBcCollectionExample example);

    int deleteByExample(TBcCollectionExample example);

    int deleteByPrimaryKey(String id);

    int insert(TBcCollection record);

    int insertSelective(TBcCollection record);

    List<TBcCollection> selectByExample(TBcCollectionExample example);

    TBcCollection selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TBcCollection record);

    int updateByPrimaryKey(TBcCollection record);

    Integer findBankTypeById(String projectId);

	TBcCollection findByBuildingCodeAndType(@Param("buildingCode") String buildingCode, @Param("type") Integer type);

	List<TBcCollection> findAllByProjectIdAndType(@Param("projectId") String projectId, @Param("type") Integer type);

    int batchDelete(String[] ids);

    int batchEffective(String[] ids);

    int batchUnEffective(String[] ids);

	TBcCollection findByBuildingCode(String buildingCode);

	List<TBcCollection> findByBillTotalId(@Param("totalId") String totalId,@Param("projectId") String projectId);

	List<TBcCollection> findByIds(@Param("ids") List<String> ids);
}