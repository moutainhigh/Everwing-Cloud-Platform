package com.everwing.coreservice.wy.dao.mapper.configuration.tbs.assetsaccount;

import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccountDto;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccountInitialization;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TBsAssetAccountMapper {
	
	
	/**
	 * 批量新增
	 */
	int batchInsert(List<TBsAssetAccount> list);
	
	/**
	 * 单个新增
	 */
	int singleInsert(TBsAssetAccount tBsAssetAccount);
	
	/**
	 * 根据建筑编号和账户类型查找账户
	 */
	TBsAssetAccount lookupByBuildCodeAndType(String buildingCode,Integer type);

	int update(TBsAssetAccount account);
	
	/**
	 * 根据建筑编号查询物业管理、水费、电费、本体基金账户情况
	 */
	List<TBsAssetAccount> queryAccountSituationByBuildCode(String buildCoe);
	
	/**
	 * 批量更新账号
	 */
	int batchUpdate(List<TBsAssetAccount> list);

	TBsAssetAccount findByBillId(String billId);
	
	/**
	 * 查询单个建筑总欠费
	 * @param projectId
	 * @param buildCode
	 * @return
	 */
	Double queryTotalArrears(String projectId,String buildCode);
	
	/**
	 * 查询多个建筑总欠费
	 * @param buildingCodes
	 * @param type
	 * @return
	 */
	Double queryTotalBalances(@Param("buildings") List<TcBuilding> buildingCodes);

	int getAccountsByBuildingCodesAndType(@Param("buildingCodes") List<String> buildingCodes, @Param("type") Integer type);

	int getAccountsByBuildingCodesAndTypeNew(@Param("buildingCodes") List<String> buildingCodes, @Param("type") Integer type);

	List<TBsAssetAccount> getAccountsByBuildingCode(@Param("buildingCode") String buildingCode);

	//添加违约金进入账户余额
	int addLateFee(@Param("accountId") String accountId, @Param("lateFee") double lateFee);

	List<TBsAssetAccount> findByBuildingCodeAndItems(@Param("buildingCode") String buildingCode, @Param("items") List<String> items);


	List<Map<String,Object>> queryBalances(@Param("buildings") List<TcBuilding> buildingList);

	/**
	 * 获取账户的余额和违约金
	 * @param buildingCode
	 * @return
	 */
	Map<String,Object> findByAccountBalance(String buildingCode);

	/**
	 * 等到欠费的账户信息
	 * @param projectId
	 * @return
	 */
	List<TBsAssetAccountDto> findListAccountAndArrears(String projectId);

    List<TBsAssetAccountInitialization> findByInitializationAndProjectId(String projectCode);

    List<TBsAssetAccountDto> findListThirdAccountAndArrears(String projectCode);

	/**
	 * 第三方资产
	 * @param projectCode
	 * @return
	 */
	List<TBsAssetAccountInitialization> findByThirdInitializationAndProjectId(String projectCode);

	Map<String,Double> getAccountInfoByCode(String buildingCode);

}
