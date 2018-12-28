package com.everwing.coreservice.common.wy.service.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;

public interface SumMmeterBillToProjectService {

	/**
	 * 查找项目下要汇总的总单编号发送至MQ消息队列
	 */
	BaseDto findTotalBill(String companyId);
	
	/**
	 * 根据总单汇总该总单下M级虚拟表的计费费用
	 */
	BaseDto sumTotalBill(String companyId,TBsChargeBillTotal total);
}
