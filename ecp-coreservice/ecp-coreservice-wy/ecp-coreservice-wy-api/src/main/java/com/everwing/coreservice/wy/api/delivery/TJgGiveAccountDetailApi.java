package com.everwing.coreservice.wy.api.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.delivery.TJgGiveAccountDetail;
import com.everwing.coreservice.common.wy.service.delivery.TJgGiveAccountDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/***
 * @describe 银账交割交账明细表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Service("tJgGiveAccountDetailApi")
public class TJgGiveAccountDetailApi{
	
	@Autowired
	private TJgGiveAccountDetailService tJgGiveAccountDetailService;
	
	
	public RemoteModelResult<MessageMap> addGiveAccountDetail(String companyId,TJgGiveAccountDetail entity) {
		return new RemoteModelResult<MessageMap>(this.tJgGiveAccountDetailService.addGiveAccountDetail(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> listPageGiveCashInfo(String companyId,TJgGiveAccountDetail entity) {
		return new RemoteModelResult<BaseDto>(this.tJgGiveAccountDetailService.listPageGiveCashInfo(companyId,entity));
	}
	
	
	public RemoteModelResult<BaseDto> listPageReceiveCashInfo(String companyId,TJgGiveAccountDetail entity) {
		return new RemoteModelResult<BaseDto>(this.tJgGiveAccountDetailService.listPageReceiveCashInfo(companyId,entity));
	}
	
	public RemoteModelResult<MessageMap> returnOrConfirmGiveInfo(String companyId,TJgGiveAccountDetail entity,String ids) {
		return new RemoteModelResult<MessageMap>(this.tJgGiveAccountDetailService.returnOrConfirmGiveInfo(companyId,entity,ids));
	}
	
	public RemoteModelResult<BaseDto> listPageGiveAccountByTotalId(String companyId,TJgGiveAccountDetail entity) {
		return new RemoteModelResult<BaseDto>(this.tJgGiveAccountDetailService.listPageGiveAccountByTotalId(companyId,entity));
	}
	
	
	public RemoteModelResult<BaseDto> listPageAccountReceivables(String companyId,TJgGiveAccountDetail entity,String type) {
		return new RemoteModelResult<BaseDto>(this.tJgGiveAccountDetailService.listPageAccountReceivables(companyId,entity,type));
	}
	
	public RemoteModelResult<BaseDto> listPageGiveAccountForCN(String companyId,TJgGiveAccountDetail entity) {
		return new RemoteModelResult<BaseDto>(this.tJgGiveAccountDetailService.listPageGiveAccountForCN(companyId,entity));
	}
	
	
}
