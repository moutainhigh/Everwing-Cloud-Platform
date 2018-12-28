package com.everwing.coreservice.common.wy.service.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.delivery.TJgGiveAccountDetail;

/***
 * @describe 银账交割交账明细表接口
 * @author qhc
 * @ date 2017-08-31 
 */
public interface TJgGiveAccountDetailService{
	
	MessageMap addGiveAccountDetail(String companyId,TJgGiveAccountDetail entity);
	
	@SuppressWarnings("rawtypes")
	BaseDto listPageGiveCashInfo(String companyId ,TJgGiveAccountDetail entity);
	
	@SuppressWarnings("rawtypes")
	BaseDto listPageReceiveCashInfo(String companyId ,TJgGiveAccountDetail entity);
	
	MessageMap returnOrConfirmGiveInfo(String companyId ,TJgGiveAccountDetail entity,String ids);
	
	BaseDto listPageGiveAccountByTotalId(String companyId ,TJgGiveAccountDetail entity);
	
	BaseDto listPageAccountReceivables(String companyId ,TJgGiveAccountDetail entity,String type);
	
	BaseDto listPageGiveAccountForCN(String companyId,TJgGiveAccountDetail entity);
	
}
