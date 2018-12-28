package com.everwing.coreservice.wy.dao.mapper.configuration.rebilling;

import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;

import java.util.List;

public interface TBsRebillingInfoMapper {

	int insert(TBsRebillingInfo entity);

	int batchInsert(List<TBsRebillingInfo> insertInfoList);

	List<TBsRebillingInfo> listPage(TBsRebillingInfo entity);
	
	
	
	
}
