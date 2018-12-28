package com.everwing.coreservice.common.wy.service.configuration.bill;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;

public interface TBsBillTotalService {

	BaseDto findDataPerYear(String companyId,TBsChargeBillTotal entity);

	BaseDto findById(String companyId, String id);

	BaseDto audit(String companyId, TBsChargeBillTotal entity);

	void koufei(String companyId, TBsChargeBillTotal total);

}
