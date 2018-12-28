package com.everwing.coreservice.wy.api.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.delivery.TJgTotalCalculation;
import com.everwing.coreservice.common.wy.service.delivery.TJgTotalCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 * @describe 总结算表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Service("tJgTotalCalculationApi")
public class TJgTotalCalculationApi{
	
	@Autowired
	private TJgTotalCalculationService tJgTotalCalculationService;
	
	public RemoteModelResult<MessageMap> addTotalCalculation(String companyId,TJgTotalCalculation entity) {
		return new RemoteModelResult<MessageMap>(this.tJgTotalCalculationService.addTotalCalculation(companyId,entity));
	}
	
	
	public RemoteModelResult<BaseDto> listPageTotalCaculation(String companyId,TJgTotalCalculation entity) {
		return new RemoteModelResult<BaseDto>(this.tJgTotalCalculationService.listPageTotalCaculation(companyId,entity));
	}
	
	public RemoteModelResult<MessageMap> returnOrConfirmTotalInfo(String companyId,TJgTotalCalculation entity,String ids) {
		return new RemoteModelResult<MessageMap>(this.tJgTotalCalculationService.returnOrConfirmTotalInfo(companyId,entity,ids));
	}
	
	public RemoteModelResult<BaseDto> summaryAccountForInfo(String companyId,TJgTotalCalculation entity) {
		return new RemoteModelResult<BaseDto>(this.tJgTotalCalculationService.summaryAccountForInfo(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> listPageSelfTotalInfo(String companyId,TJgTotalCalculation entity) {
		return new RemoteModelResult<BaseDto>(this.tJgTotalCalculationService.listPageSelfTotalInfo(companyId,entity));
	}
	
	public RemoteModelResult<MessageMap> giveAnccountSummarForLeader(String companyId,TJgTotalCalculation entity,int type) {
		return new RemoteModelResult<MessageMap>(this.tJgTotalCalculationService.giveAnccountSummarForLeader(companyId,entity,type));
	}
	
	public RemoteModelResult<BaseDto> listPageSettlementDetails(String companyId,TJgTotalCalculation entity,String type) {
		return new RemoteModelResult<BaseDto>(this.tJgTotalCalculationService.listPageSettlementDetails(companyId,entity,type));
	}
	
	
	public RemoteModelResult<MessageMap> returnOrConfirmTotalInfoCN(String companyId,TJgTotalCalculation entity,String ids) {
		return new RemoteModelResult<MessageMap>(this.tJgTotalCalculationService.returnOrConfirmTotalInfoCN(companyId,entity,ids));
	}
	
	
	public RemoteModelResult<MessageMap> returnOrConfirmTotalInfoKJ(String companyId,TJgTotalCalculation entity,String ids) {
		return new RemoteModelResult<MessageMap>(this.tJgTotalCalculationService.returnOrConfirmTotalInfoCN(companyId,entity,ids));
	}
	
	
	
	
}
