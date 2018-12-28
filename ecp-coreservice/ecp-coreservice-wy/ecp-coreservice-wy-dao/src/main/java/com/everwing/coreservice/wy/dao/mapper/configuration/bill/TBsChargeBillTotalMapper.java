package com.everwing.coreservice.wy.dao.mapper.configuration.bill;

import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface TBsChargeBillTotalMapper {

	Map<String,Object> selectCurrentFeePerYear(TBsChargeBillTotal entity);
	Map<String,Object> selectIdPerYear(TBsChargeBillTotal entity);
	Map<String,Object> selectBillingTimePerYear(TBsChargeBillTotal entity);
	Map<String,Object> selectLastOwedFeePerYear(TBsChargeBillTotal entity);
	Map<String,Object> selectTotalFeePerYear(TBsChargeBillTotal entity);
	Map<String,Object> selectChargeTypePerYear(TBsChargeBillTotal entity);
	Map<String,Object> selectIsRebillingPerYear(TBsChargeBillTotal entity);
	Map<String,Object> selectAuditStatusPerYear(TBsChargeBillTotal entity);
	
	//查询当前等待扣费的   总计费信息
	TBsChargeBillTotal selectChargeBillByType(String projectId,String type);
	
	//查询所有的待审核的费用，总计费信息
	List<TBsChargeBillTotal> getNoAuditByProjectIdAndType(String projectId,String type);
	
	//新增一条  总计费信息
	int insertChargeBillTotal(TBsChargeBillTotal tBsChargeBillTotal);
	//获取某项目下某账户的最新一月的总账单
	List<TBsChargeBillTotal> findCurrentBillTotal(TBsChargeBillTotal paramTotal);
	
	//获取某项目下某账户的最新一月的总账单--分摊用，这里和上面有所不同。有了TBsChargeBillTotal信息才能进行分摊操作
	List<TBsChargeBillTotal> findCurrentBillTotalForShare(TBsChargeBillTotal paramTotal);
	
	//根据id获取总账单
	TBsChargeBillTotal selectById(String id);
	
	//修改
	Integer update(TBsChargeBillTotal currentTotalBill);
	
	TBsChargeBillTotal findNextBillTotal(String id);
	
	/*
	 * TODO 获取项目下未对通用账户扣费的本期总账单.
	 */
	List<TBsChargeBillTotal> findCmacCanbilling(TBsChargeBillTotal paramTotal);
	
	TBsChargeBillTotal findBilledTotal(TBsChargeBillTotal paramTotal);
	
	List<TBsChargeBillTotal> selectRebillingTotalByIds(List<String> ids);
	
	Map<String, Object> selectAllFee(TBsChargeBillTotal paramTotal);
	
	/**
	 * 根据id获取总账单
	 */
	TBsChargeBillTotal findTbsTotalbyId(String id);
	
	/**
	 * 根据lastTotalId应该也只能找到一个
	 */
	TBsChargeBillTotal findTbsTotalBylastTotalId(String lastTotalId);
	
	int sumLastPayed(String totalId) throws DataAccessException;
	
	/**
	 * 根据项目编号查找没有审核完成的总单
	 */
	List<TBsChargeBillTotal> findNoAduitByProjectId(String projectId);
	Integer getAuditedCountByProjectIdAndTypes(@Param("projectId") String projectId, @Param("types") List<String> types);
	
	List<TBsChargeBillTotal> findByObj(TBsChargeBillTotal paramTotal);
	
	
	
}
