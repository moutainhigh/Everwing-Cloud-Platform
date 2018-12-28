package com.everwing.coreservice.wy.api.order;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplaint;
import com.everwing.coreservice.common.wy.service.order.TcOrderComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tcOrderCompaintApi")
public class TcOrderComplaintApi {

	
	@Autowired
	private TcOrderComplaintService tcOrderComplaintService;

	public RemoteModelResult<BaseDto> listPage(String companyId,TcOrderComplaint entity) {
		return new RemoteModelResult<BaseDto>(this.tcOrderComplaintService.listPage(companyId,entity));
	}

	public RemoteModelResult<BaseDto> insert(String companyId,TcOrderComplaint entity) {
		return new RemoteModelResult<BaseDto>(this.tcOrderComplaintService.insert(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> showDtail(WyBusinessContext ctx, String projectId,
												String orderCode, String buildCode, String type){
		return new RemoteModelResult<BaseDto>(this.tcOrderComplaintService.showDtail(ctx, projectId, orderCode, buildCode, type));
	}
}
