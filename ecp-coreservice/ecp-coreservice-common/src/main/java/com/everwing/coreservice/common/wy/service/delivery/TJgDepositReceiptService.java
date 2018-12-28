package com.everwing.coreservice.common.wy.service.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.delivery.TJgDepositReceipt;

/***
 * @describe 存单表接口
 * @author qhc
 * @ date 2017-08-31 
 */
public interface TJgDepositReceiptService{
	
	MessageMap addDepositReceipt(String companyId,TJgDepositReceipt entity);
	
	BaseDto getDepositReceiptInfo(String companyId,String totalId);
	
	BaseDto getDepositReceiptInfoForGive(String companyId,String projectId,String oprId);
	
	MessageMap delDepositReceiptInfo(String companyId,String id);
	
	BaseDto listPageDepositsInfo(String companyId,TJgDepositReceipt entity,int type);
}
