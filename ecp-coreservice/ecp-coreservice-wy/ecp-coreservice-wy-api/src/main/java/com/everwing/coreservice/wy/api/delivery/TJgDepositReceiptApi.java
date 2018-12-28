package com.everwing.coreservice.wy.api.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.delivery.TJgDepositReceipt;
import com.everwing.coreservice.common.wy.service.delivery.TJgDepositReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/***
 * @describe 存单表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Service("tJgDepositReceiptApi")
public class TJgDepositReceiptApi{
	
	@Autowired
	private TJgDepositReceiptService tJgDepositReceiptService;
	
	
	public RemoteModelResult<MessageMap> addDepositReceipt(String companyId,TJgDepositReceipt entity) {
		return new RemoteModelResult<MessageMap>(this.tJgDepositReceiptService.addDepositReceipt(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> getDepositReceiptInfo(String companyId,String totalId) {
		return new RemoteModelResult<BaseDto>(this.tJgDepositReceiptService.getDepositReceiptInfo(companyId,totalId));
	}
	
	public RemoteModelResult<BaseDto> getDepositReceiptInfoForGive(String companyId,String projectId,String oprId) {
		return new RemoteModelResult<BaseDto>(this.tJgDepositReceiptService.getDepositReceiptInfoForGive(companyId,projectId,oprId));
	}
	
	public RemoteModelResult<MessageMap> delDepositReceiptInfo(String companyId,String id) {
		return new RemoteModelResult<MessageMap>(this.tJgDepositReceiptService.delDepositReceiptInfo(companyId,id));
	}
	
	public RemoteModelResult<BaseDto> listPageDepositsInfo(String companyId,TJgDepositReceipt entity,int type) {
		return new RemoteModelResult<BaseDto>(this.tJgDepositReceiptService.listPageDepositsInfo(companyId,entity,type));
	}
	
}
