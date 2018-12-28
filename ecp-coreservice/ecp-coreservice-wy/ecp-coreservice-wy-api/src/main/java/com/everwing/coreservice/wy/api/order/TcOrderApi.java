package com.everwing.coreservice.wy.api.order;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.dto.MyWorkOrderDTO;
import com.everwing.coreservice.common.wy.dto.OrderSearchDto;
import com.everwing.coreservice.common.wy.entity.order.TcOrder;
import com.everwing.coreservice.common.wy.service.order.TcOrderCompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TcOrderApi {


	@Autowired
	private TcOrderCompleteService tcOrderCompleteService;
	
	
	public RemoteModelResult<BaseDto> listPageDatas(OrderSearchDto orderSearchDto) {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.listPageDatas(CommonUtils.getCompanyIdByCurrRequest(),orderSearchDto));
	}
	
	public RemoteModelResult<BaseDto> updateOrder(String companyId,TcOrder order) {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.updateOrder(companyId,order));
	}
	
	public RemoteModelResult<BaseDto> newOrders(List<TcOrder> newOrders) {
		String userId= WyBusinessContext.getContext().getUserId();
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.newOrders(CommonUtils.getCompanyIdByCurrRequest(),newOrders,userId));
	}
	
	public RemoteModelResult<BaseDto> deleteOrders(String ids) {
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.deleteOrders(CommonUtils.getCompanyIdByCurrRequest(),ids));
	}
	
	public RemoteModelResult<BaseDto> finishOrders(String ids) {
		String updateBy=WyBusinessContext.getContext().getUserId();
		return new RemoteModelResult<BaseDto>(this.tcOrderCompleteService.finishOrders(CommonUtils.getCompanyIdByCurrRequest(),ids,updateBy));
	}

	public RemoteModelResult<List> queryMyWorkOrder(String companyId,String accountId,String description,String date,int pageSize,int pageNo) {
        RemoteModelResult<List> remoteModelResult = new RemoteModelResult();
        List<MyWorkOrderDTO> list = tcOrderCompleteService.queryMyWorkOrder(companyId,accountId,description,date,pageSize,pageNo);
        remoteModelResult.setModel(list);
        return remoteModelResult;
    }

    public RemoteModelResult<Integer> updateProcessedWorkOrder(String companyId,String orderCode,String status,String processDate,String procesInstructions,String updateBy) {
        RemoteModelResult<Integer> remoteModelResult = new RemoteModelResult();
        remoteModelResult.setModel(tcOrderCompleteService.updateProcessedWorkOrder(companyId,orderCode,status,processDate,procesInstructions,updateBy));
        return remoteModelResult;
    }

	public RemoteModelResult<TcOrder> queryOrderStatusById(String companyId,String orderId) {
		RemoteModelResult<TcOrder> remoteModelResult = new RemoteModelResult();
		remoteModelResult.setModel(tcOrderCompleteService.selectById(companyId,orderId));
		return remoteModelResult;
	}
}
