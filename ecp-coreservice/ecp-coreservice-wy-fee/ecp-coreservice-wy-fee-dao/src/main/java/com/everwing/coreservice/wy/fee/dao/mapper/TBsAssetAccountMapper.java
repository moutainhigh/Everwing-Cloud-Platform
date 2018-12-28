package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TBsAssetAccountMapper {

	List<TBsAssetAccount> findByBuildingCodeAndItems(@Param("buildingCode") String buildingCode, @Param("items") List<String> items);

	int update(TBsAssetAccount account);

	List<TBsAssetAccount> getAccountsByBuildingCode(@Param("buildingCode") String buildingCode);

	int updateAccountAndTypeAndAmount(@Param("amount") double amount,
									  @Param("type") Integer accountType,@Param("houseCodeNew") String houseCode);
}
