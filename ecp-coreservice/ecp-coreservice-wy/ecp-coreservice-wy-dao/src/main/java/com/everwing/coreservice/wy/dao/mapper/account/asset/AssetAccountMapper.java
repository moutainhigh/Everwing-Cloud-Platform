package com.everwing.coreservice.wy.dao.mapper.account.asset;

import com.everwing.coreservice.common.wy.entity.account.asset.AssetAccount;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface AssetAccountMapper {
	/*查询所有的资产账户信息*/
	List<AssetAccount> listAllAssetAccount() throws DataAccessException;
	/*分页查询资产账户信息*/
//    List<AssetAccount> listPageAssetAccount(Search search) throws DataAccessException;
    /*根据id查询资产账户信息*/
    AssetAccount getAssetAccountById(String assetAccountId) throws DataAccessException;
    /*新增资产账户信息*/
    int insertAssetAccount(AssetAccount assetAccount) throws DataAccessException;
    /*修改资产账户信息*/
    int updateAssetAccount(AssetAccount assetAccount) throws DataAccessException;
    /*查询押金账户信息assetAccountType=1	（押金）*/
    /****获取后调用预支付明细表的汇总接口并设置相应字段*****/
    List<AssetAccount> getAssetAccountByCustId(String custId) throws DataAccessException;
    /*获取欠费账户总余额*/
    Double getTotalBalanceOfDelinquentAccounts(String assetAccountId) throws DataAccessException;    
    
    /*根据客户id查询资产账户信息*/
    AssetAccount seletctAssetAccountByCustId(String custId,String buildingId) throws DataAccessException;
    /*根据项目id获取管理户数*/
    int countAccountNumByProjectId(String projectId) throws DataAccessException;
    
    /*充值*/
    int topUP(AssetAccount assetAccount) throws DataAccessException;
    
    //根据建筑ID获取该建筑的资产类型
	AssetAccount getAssetAccountByBuildingStructureId(String buildingStructureId) throws DataAccessException;
	
	AssetAccount countAccountNumBybuildingId(String buildingId) throws DataAccessException;
	
	/**
	 * 批量插入资产账户数据
	 * 王洲
	 * 2016.04.19
	 * @param list
	 * @return
	 */
	int insertAssetAccountList(List<AssetAccount> list) throws DataAccessException;
	
	/**
	 * 根据建筑结构id查询对应的资产账户
	 * @author 王洲
	 * @param buildingId
	 * @return
	 */
	AssetAccount getAssetAccountByBuildingId(String buildingId) throws DataAccessException;
	
	
	/**
	 * 根据客户id获取关联的资产账户和押金账户
	 * @param custId
	 * @return list
	 * @author wangzhou
	 * @date 2016.04.27
	 */
	List<AssetAccount> listAssetAccountByCustId(String custId) throws DataAccessException;
	
	/**
	 * 根据建筑id获取资产账户和收支明细
	 * @return
	 * @author wangzhou
	 * @date 2016.04.27
	 */
	AssetAccount listAssetAccountByBuildingId(String buildingId) throws DataAccessException;
	
	/**
	 * 根据条件查询账号和账号明细表
	 * @param account
	 * @return
	 */
	List<AssetAccount> queryByBuild(AssetAccount account) throws DataAccessException;
	
	/**
	 * 根据cust_id和project_id查询账户信息
	 * @param custId
	 * @param projectId
	 * @return
	 */
	List<AssetAccount> queryByProAndCid(String custId, String projectId) throws DataAccessException;
	/**
	 * 根据custId查询资产信息
	 * @param custId
	 * @return
	 */
	List<AssetAccount> getAssetsAccountByCustId(String custId) throws DataAccessException;
	/**
	 * 给建筑一个资产
	 * @param assetAccount
	 */
	void insertAssetAccounts(AssetAccount assetAccount) throws DataAccessException;

	//查询账户信息
	AssetAccount getAstActByBCid(AssetAccount assetAccount) throws DataAccessException;
	
	//根据个人客户的custCode删除资产
	int deleteAssetAccountByCustId(String custId) throws DataAccessException;
	
}