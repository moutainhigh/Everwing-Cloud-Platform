package com.everwing.coreservice.common.wy.service.configuration.rebilling;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;

public interface TBsRebillingInfoService {

	BaseDto listPageInfos(String companyId, TBsRebillingInfo entity);

	BaseDto rebilling(String companyId, TBsRebillingInfo entity,Integer type);

}
