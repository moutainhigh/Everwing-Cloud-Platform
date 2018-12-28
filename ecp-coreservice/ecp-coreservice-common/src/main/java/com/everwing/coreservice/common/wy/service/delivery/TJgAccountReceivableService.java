package com.everwing.coreservice.common.wy.service.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.delivery.TJgAccountReceivable;

/***
 * @describe 交账明细表接口
 * @author qhc
 * @ date 2017-08-31 
 */
public interface TJgAccountReceivableService{
	
	MessageMap addAccountReceivable(String companyId,TJgAccountReceivable entity);
	
	BaseDto listPageAcountReceiveInfo(String companyId,TJgAccountReceivable entity);
	
	BaseDto sumPaymentInfo(String companyId,TJgAccountReceivable entity);
	
	BaseDto sumNotGavenAmountInfo(String companyId,TJgAccountReceivable entity);
	
	BaseDto getNotGivenCashAmount(String companyId,String userId,String projectId);
	
	BaseDto listPageNotGivenInfos(String companyId,TJgAccountReceivable entity);
	
	BaseDto listPageReceiveByTotalId(String companyId,TJgAccountReceivable entity);
	
	BaseDto listPageAccountReceiveForKJ(String companyId,TJgAccountReceivable entity);
	
}
