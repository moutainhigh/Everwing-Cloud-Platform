package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.entity.delivery.TJgTotalCalculation;

import java.util.List;
import java.util.Map;

/***
 * @describe 总结算表javaBean
 * @author qhc
 * @ date 2017-08-31 
 */
public interface TJgTotalCalculationMapper {
	
	int addTotalCalculation(TJgTotalCalculation entity);
	
	List<TJgTotalCalculation> listPageTotalCaculation(TJgTotalCalculation entity);
	
	int returnOrConfirmTotalInfo(Map<String, Object> paramMap);
	
	Map<String, Double> summaryAccountForInfo(TJgTotalCalculation entity);
	
	List<TJgTotalCalculation> listPageTotalCaculationForSelf(TJgTotalCalculation entity);
	
	int updateTotalCaculation(String totalId, String oprId, String projectId);
	
	List<TJgTotalCalculation> listPageSettlementDetails(TJgTotalCalculation entity);
	
	List<TJgTotalCalculation> listPageSettlementDetailsByOpr(TJgTotalCalculation entity);
	
	int changeStatusByTotalId(List<String> list);
	
	double getSysCashInfo(TJgTotalCalculation entity);
	
}
