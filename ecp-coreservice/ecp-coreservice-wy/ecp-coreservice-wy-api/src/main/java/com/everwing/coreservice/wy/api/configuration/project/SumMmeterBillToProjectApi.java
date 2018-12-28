package com.everwing.coreservice.wy.api.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.service.configuration.project.SumMmeterBillToProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sumMmeterBillToProjectApi")
public class SumMmeterBillToProjectApi {

	@Autowired
	private  SumMmeterBillToProjectService sumMmeterBillToProjectService;
	
	public RemoteModelResult<BaseDto> findTotalBill(String companyId){
		return new RemoteModelResult<BaseDto>(this.sumMmeterBillToProjectService.findTotalBill(companyId));
	}
	
	public RemoteModelResult<BaseDto> sumTotalBill(String companyId,TBsChargeBillTotal total){
		
		return new RemoteModelResult<BaseDto>(this.sumMmeterBillToProjectService.sumTotalBill(companyId, total));
	}
}
