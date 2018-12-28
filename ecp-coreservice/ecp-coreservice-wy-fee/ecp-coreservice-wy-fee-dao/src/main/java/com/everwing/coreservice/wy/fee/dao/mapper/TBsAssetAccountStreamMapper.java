package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TBsAssetAccountStreamMapper {

	int batchInsert(List<TBsAssetAccountStream> insertStreamList);

	int singleInsert(TBsAssetAccountStream tBsAssetAccountStream);


	int insertAndAmountByBuildingCode(@Param("houseCode") String houseCode,@Param("type") int i,@Param("changMoney") double v,
									  @Param("createId") String operaId,@Param("purpose") String s);
}
