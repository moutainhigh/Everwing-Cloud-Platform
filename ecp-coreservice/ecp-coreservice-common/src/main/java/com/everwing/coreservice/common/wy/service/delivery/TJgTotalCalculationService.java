package com.everwing.coreservice.common.wy.service.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.delivery.TJgTotalCalculation;

/***
 * @describe 总结算表javaBean
 * @author qhc
 * @ date 2017-08-31 
 */
public interface TJgTotalCalculationService{
	
	MessageMap addTotalCalculation(String companyId,TJgTotalCalculation entity);
	
	@SuppressWarnings("rawtypes")
	BaseDto listPageTotalCaculation(String companyId,TJgTotalCalculation entity);
	
	MessageMap returnOrConfirmTotalInfo(String companyId,TJgTotalCalculation entity,String ids);
	
	@SuppressWarnings("rawtypes")
	BaseDto summaryAccountForInfo(String companyId,TJgTotalCalculation entity);
	
	@SuppressWarnings("rawtypes")
	BaseDto listPageSelfTotalInfo(String companyId,TJgTotalCalculation entity);
	
	MessageMap giveAnccountSummarForLeader(String companyId,TJgTotalCalculation entity,int type);
	
	@SuppressWarnings("rawtypes")
	BaseDto listPageSettlementDetails(String companyId,TJgTotalCalculation entity,String type);
	
	MessageMap returnOrConfirmTotalInfoCN(String companyId,TJgTotalCalculation entity,String ids); 
	
	MessageMap returnOrConfirmTotalInfoKJ(String companyId,TJgTotalCalculation entity,String ids); 
	
	
	
}
