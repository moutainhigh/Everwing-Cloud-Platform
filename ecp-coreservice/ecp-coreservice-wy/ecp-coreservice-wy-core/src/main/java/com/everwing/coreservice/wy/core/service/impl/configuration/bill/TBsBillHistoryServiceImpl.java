package com.everwing.coreservice.wy.core.service.impl.configuration.bill;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.service.configuration.bill.TBsBillHistoryService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("tBsBillHistoryService")
public class TBsBillHistoryServiceImpl extends Resources implements TBsBillHistoryService{

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageData(String companyId, TBsChargeBillHistory entity) {
		return new BaseDto(this.tBsChargeBillHistoryMapper.listPage(entity), entity.getPage());
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto updateZipCompleteByObj(String companyId,TBsChargeBillHistory paramObj) {
		this.tBsChargeBillHistoryMapper.updateZipCompleteByObj(paramObj);
		return new BaseDto(new MessageMap(null,"修改完成"));
	}

	@Override
	public BaseDto findNotZipByObj(String companyId,TBsChargeBillHistory paramObj) {
		int count = this.tBsChargeBillHistoryMapper.findNotZipByObj(paramObj);
		BaseDto dto = new BaseDto();
		dto.setObj(count);
		return dto;
	}

	@Override
	public BaseDto listPageInCustomerService(String companyId,TBsChargeBillHistory entity) {
		return new BaseDto(this.tBsChargeBillHistoryMapper.listPageInCustomerService(entity), entity.getPage());
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto updateBilledBuilding(String companyId, TBsChargeBillHistory paramObj, List<String> buildingCodes) {
		this.tBsChargeBillHistoryMapper.updateBilledBuilding(paramObj,buildingCodes);
		return new BaseDto(new MessageMap(null,"修改完成"));
	}
}
