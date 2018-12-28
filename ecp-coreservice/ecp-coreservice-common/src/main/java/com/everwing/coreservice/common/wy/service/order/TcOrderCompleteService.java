package com.everwing.coreservice.common.wy.service.order;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.dto.MyWorkOrderDTO;
import com.everwing.coreservice.common.wy.dto.OrderSearchDto;
import com.everwing.coreservice.common.wy.entity.order.TcOrder;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplete;

import java.util.List;

public interface TcOrderCompleteService {

	BaseDto insert(String companyId, TcOrderComplete entity);

	BaseDto findById(String companyId, String id);

	BaseDto findDetailById(String companyId, String id);

	BaseDto subType(String companyId,String typeId);

	BaseDto levelOneType(String companyId);

	BaseDto deleteOrders(String companyId,String ids);

	BaseDto newOrders(String companyId,List<TcOrder> newOrders,String userId);

	BaseDto listPageDatas(String companyId, OrderSearchDto orderSearchDto);

	BaseDto finishOrders(String companyId, String ids, String updateBy);

	BaseDto updateOrder(String companyId, TcOrder order);

	/**
	 * 产权变更完成工单新增保存
	 * @param ctx
	 * @param entity
	 * @param type
	 * @return
	 */
	BaseDto inserChangeAssetInsert(WyBusinessContext ctx, TcOrderComplete entity, String type);

	List<MyWorkOrderDTO> queryMyWorkOrder(String companyId,String accountId,String description,String date,int pageSize,int pageNo);

	int updateProcessedWorkOrder(String companyId,String orderCode,String status,String processDate,String procesInstructions,String updateBy);

    TcOrder selectById(String companyId,String orderId);
}
