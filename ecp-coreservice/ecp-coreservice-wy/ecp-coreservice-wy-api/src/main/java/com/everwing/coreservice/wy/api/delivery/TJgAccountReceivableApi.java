package com.everwing.coreservice.wy.api.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.delivery.TJgAccountReceivable;
import com.everwing.coreservice.common.wy.service.delivery.TJgAccountReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/***
 * @describe 交账明细表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Service("tJgAccountReceivableApi")
public class TJgAccountReceivableApi{
	
	@Autowired
	private TJgAccountReceivableService tJgAccountReceivableService;
	
	
	public RemoteModelResult<MessageMap> addAccountReceivable(String companyId,TJgAccountReceivable entity) {
		return new RemoteModelResult<MessageMap>(this.tJgAccountReceivableService.addAccountReceivable(companyId,entity));
	}
	
	
	public RemoteModelResult<BaseDto> listPageAcountReceiveInfo(String companyId,TJgAccountReceivable entity) {
		return new RemoteModelResult<BaseDto>(this.tJgAccountReceivableService.listPageAcountReceiveInfo(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> sumPaymentInfo(String companyId,TJgAccountReceivable entity) {
		return new RemoteModelResult<BaseDto>(this.tJgAccountReceivableService.sumPaymentInfo(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> sumNotGavenAmountInfo(String companyId,TJgAccountReceivable entity) {
		return new RemoteModelResult<BaseDto>(this.tJgAccountReceivableService.sumNotGavenAmountInfo(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> getNotGivenCashAmount(String companyId,String userId,String projectId) {
		return new RemoteModelResult<BaseDto>(this.tJgAccountReceivableService.getNotGivenCashAmount(companyId,userId,projectId));
	}
	
	
	public RemoteModelResult<BaseDto> listPageNotGivenInfos(String companyId,TJgAccountReceivable entity) {
		return new RemoteModelResult<BaseDto>(this.tJgAccountReceivableService.listPageNotGivenInfos(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> listPageReceiveByTotalId(String companyId,TJgAccountReceivable entity) {
		return new RemoteModelResult<BaseDto>(this.tJgAccountReceivableService.listPageReceiveByTotalId(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> listPageAccountReceiveForKJ(String companyId,TJgAccountReceivable entity) {
		return new RemoteModelResult<BaseDto>(this.tJgAccountReceivableService.listPageAccountReceiveForKJ(companyId,entity));
	}
	
}
