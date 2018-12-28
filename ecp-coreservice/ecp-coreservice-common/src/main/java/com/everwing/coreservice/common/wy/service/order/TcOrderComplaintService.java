package com.everwing.coreservice.common.wy.service.order;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.order.TcOrderChangeAssetComplaint;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplaint;

public interface TcOrderComplaintService {

	BaseDto listPage(String companyId,TcOrderComplaint entity);

	BaseDto insert(String companyId, TcOrderComplaint entity);
	
	/**
	 * 临时单个抄表任务
	 * @param ctx
	 * @param custName
	 * @param biuldCode
	 * @param projectId
	 * @param projectName
	 * @param type
	 * @param readingPersonId
	 * @return
	 */
	public BaseDto insertAssetsChange(WyBusinessContext ctx, TcOrderChangeAssetComplaint tcOrderChangeAssetComplaint);

	/**
	 * 展示明细
	 */
	BaseDto showDtail(WyBusinessContext ctx, String projectId,
					  String orderCode, String buildCode, String type);
}
