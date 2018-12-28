package com.everwing.coreservice.wy.dao.mapper.configuration.bill;

import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistoryDto;
import com.everwing.coreservice.common.wy.entity.configuration.payinfo.PushPayInfoToFinance;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TBsChargeBillHistoryMapper {

	List<TBsChargeBillHistory> listPage(TBsChargeBillHistory entity);
	
	TBsChargeBillHistory selectChargeHistoryOne(TBsChargeBillHistory entity);
	
	int insertBillHistory(TBsChargeBillHistory entity) throws DataAccessException;
	
	TBsChargeBillHistory selectLastChargeHistory(TBsChargeBillHistory entity);
	
	int updateBillHistory(TBsChargeBillHistory entity) throws DataAccessException;

	List<TBsChargeBillHistory> findCurrentDetailBill(TBsChargeBillHistory paramBill);

	//根据本次bill的last_bill_id获取上次的Bill账单详情
	TBsChargeBillHistory selectLastBillById(String lastBillId);

	Integer batchInsert(List<TBsChargeBillHistory> insertList) throws DataAccessException;
	
	Integer batchUpdate(List<TBsChargeBillHistory> updateList) throws DataAccessException;
	
	int updateChargeHistoryForShare(TBsChargeBillHistory entity) throws DataAccessException;
	

	List<TBsChargeBillHistory> findAllByTotalId(String id);

	TBsChargeBillHistory findNextHistory(TBsChargeBillHistory history);

	Double findAllOwedFeeByTotalId(String totalId);

	TBsChargeBillHistory selectByAccountId(@Param("accountId") String accountId,@Param("type")Integer type);

	int updateLateFeeByProjectId(String projectId) throws DataAccessException;

	Double findAllPayedByTotalId(String id);		//统计下期账单的总上期支付

	String findNewLastId(String id, String buildingCode);

	Double findCurrPayedByTotalId(String id);

	List<TBsChargeBillHistory> findAllByObjAfterTime(Map<String,Object> paramObj);

	List<TBsChargeBillHistory> findCurrentBillByBuildingCode(String buildingCode);

	void updateZipCompleteByObj(TBsChargeBillHistory paramObj) throws DataAccessException;

	int findNotZipByObj(TBsChargeBillHistory paramObj);
	
	//根据总单id、buildCode以及未审核状态查询
	TBsChargeBillHistory getBytotalIdAndBuildCode(String totalId,String buildCode);
	
	TBsChargeBillHistory findByTotalIdAndBuildCode(String totalId,String buildCode);
	
	TBsChargeBillHistory getBytotalIdBuildCode(String totalId,String buildCode);
	
	//根据lastbillId和buildCode查询
	TBsChargeBillHistory getBylastBilllIdAndBuildCode(String lastbillId,String buildCode);

	TBsChargeBillHistory findByObj(TBsChargeBillHistory param);
	
	//根据buildCode和projectId查询
	Map<String,Object> getBillByBuildCode(String type,String projectId,String buildCode);

	TBsChargeBillHistory findById(String billId);

	TBsChargeBillHistory selectCurrentBillByCodeAndType(@Param("buildingCode") String buildingCode,@Param("type") Integer type);
	
	List<PushPayInfoToFinance> getBillingDataForFinece(Map<String, String> paramMap);
	

	TBsChargeBillHistory selectNotBillingByObj(@Param("buildingCode") String buildingCode,@Param("type") Integer type);

	List<TBsChargeBillHistory> listPageInCustomerService(TBsChargeBillHistory entity);
	
	//根据总单Id查找是M级别虚拟表的分单
	List<TBsChargeBillHistory> selectMByTotalId(@Param("projectId") String projectId,@Param("totalId")String totalId,@Param("meterType") Integer meterType);
	
	/**
	 * 根据buildCode和totalID查询
	 */
	List<TBsChargeBillHistory> selectCByBuildCodeAndTotalId(@Param("buildCodes") List<String> buildCodes,@Param("totalId") String totalId);

	List<TBsChargeBillHistory> findByBuildingCodeAndItems(@Param("buildingCode") String buildingCode, @Param("items") List<String> items);

    Date findLastBillTime(String id);

    void updateBilledBuilding(@Param("chargeBillHistory") TBsChargeBillHistory chargeBillHistory,@Param("buildingCodes") List<String> buildingCodes) throws DataAccessException;

	/**
	 * 查找账户的计费金额
	 * @param projectId
	 * @return
	 */
	List<TBsChargeBillHistoryDto> findListTBsChargeBillHistory(String projectId);

    List<TBsChargeBillHistoryDto> findDateCharge(String buildingCode);
}
