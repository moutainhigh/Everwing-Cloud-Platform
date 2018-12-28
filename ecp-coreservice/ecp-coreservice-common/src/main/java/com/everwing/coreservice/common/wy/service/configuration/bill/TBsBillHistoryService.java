package com.everwing.coreservice.common.wy.service.configuration.bill;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;

import java.util.List;

public interface TBsBillHistoryService {

	BaseDto listPageData(String companyId, TBsChargeBillHistory entity);

	BaseDto updateZipCompleteByObj(String companyId, TBsChargeBillHistory paramObj);

	BaseDto findNotZipByObj(String companyId, TBsChargeBillHistory paramObj);

	BaseDto listPageInCustomerService(String companyId, TBsChargeBillHistory entity);

	BaseDto updateBilledBuilding(String companyId, TBsChargeBillHistory paramObj, List<String> buildingCodes);
}
