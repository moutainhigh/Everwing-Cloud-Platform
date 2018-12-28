package com.everwing.coreservice.wy.api.configuration.bill;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.service.configuration.bill.TBsBillTotalService;
import com.everwing.coreservice.common.wy.service.configuration.project.SumMmeterBillToProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tBsBillTotalApi")
public class TBsBillTotalApi {

	@Autowired
	private TBsBillTotalService tBsBillTotalService;
	@Autowired
	private SumMmeterBillToProjectService  sumMmeterBillService;
	public RemoteModelResult<BaseDto> findDataPerYear(String companyId,TBsChargeBillTotal entity) {
		return new RemoteModelResult<BaseDto>(this.tBsBillTotalService.findDataPerYear(companyId,entity));
	}

	public RemoteModelResult<BaseDto> findById(String companyId, String id) {
		return new RemoteModelResult<BaseDto>(this.tBsBillTotalService.findById(companyId,id));
	}

	public RemoteModelResult<BaseDto> audit(String companyId,TBsChargeBillTotal entity) {
		return new RemoteModelResult<BaseDto>(this.tBsBillTotalService.audit(companyId,entity));
	}
	
	/**
	 * 测试费用汇总
	 */
	public RemoteModelResult<BaseDto> TestSum(WyBusinessContext ctx){
		return new RemoteModelResult<BaseDto>(sumMmeterBillService.findTotalBill(ctx.getCompanyId()));
	}
	
	
}
