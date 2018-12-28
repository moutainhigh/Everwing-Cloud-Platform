package com.everwing.coreservice.wy.api.order;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.dto.OrderSearchDto;
import com.everwing.coreservice.common.wy.entity.order.TcOrder;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplete;
import com.everwing.coreservice.common.wy.service.order.TcOrderCompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tcOrderCompleteApi")
public class TcOrderCompleteApi {


	@Autowired
	private TcOrderCompleteService tcOrderCompleteService;

	public RemoteModelResult<BaseDto> insert(String companyId,TcOrderComplete entity) {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.insert(companyId,entity));
	}

	public RemoteModelResult<BaseDto> findById(String companyId, String id) {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.findById(companyId,id));
	}

	public RemoteModelResult<BaseDto> findDetailById(String companyId, String id) {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.findDetailById(companyId,id));
	}
	
	public RemoteModelResult<BaseDto> subType(String typeId) {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.subType(CommonUtils.getCompanyIdByCurrRequest(),typeId));
	}
	
	public RemoteModelResult<BaseDto> levelOneType() {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.levelOneType(CommonUtils.getCompanyIdByCurrRequest()));
	}
	
	public RemoteModelResult<BaseDto> listPageDatas(OrderSearchDto orderSearchDto) {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.listPageDatas(CommonUtils.getCompanyIdByCurrRequest(),orderSearchDto));
	}
	
	public RemoteModelResult<BaseDto> updateOrder(TcOrder order) {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.updateOrder(CommonUtils.getCompanyIdByCurrRequest(),order));
	}
	
	public RemoteModelResult<BaseDto> newOrders(List<TcOrder> newOrders) {
		String userId= WyBusinessContext.getContext().getUserId();
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.newOrders(CommonUtils.getCompanyIdByCurrRequest(),newOrders,userId));
	}
	
	public RemoteModelResult<BaseDto> deleteOrders(String ids) {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.deleteOrders(CommonUtils.getCompanyIdByCurrRequest(),ids));
	}
	
	public RemoteModelResult<BaseDto> finishOrders(String ids,String updateBy) {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.finishOrders(CommonUtils.getCompanyIdByCurrRequest(),ids,updateBy));
	}
	
	public RemoteModelResult<BaseDto> saveChangeCompleteOrder(WyBusinessContext ctx,TcOrderComplete entity,String type){
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.inserChangeAssetInsert(ctx,entity,type));
	}
}
