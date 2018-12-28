package com.everwing.coreservice.wy.api.configuration.project;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBasicsInfo;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsShareBasicService;
import com.everwing.coreservice.common.wy.service.configuration.task.WaterElectShareTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("tBsShareBasicApi")
public class TBsShareBasicApi {

	
	@Autowired
	private TBsShareBasicService tBsShareBasicService;
	@Autowired
	private WaterElectShareTaskService waterElectShareTaskService;

	public RemoteModelResult<MessageMap> addShareBasic(String companyId,TBsShareBasicsInfo entity) {
		return new RemoteModelResult<MessageMap>(this.tBsShareBasicService.addShareBasic(companyId,entity));
	}

	public RemoteModelResult<BaseDto> listPageShareInfos(String companyId,TBsShareBasicsInfo entity) {
		return new RemoteModelResult<BaseDto>(this.tBsShareBasicService.listPageShareInfos(companyId,entity));
	}
	
	public RemoteModelResult<BaseDto> doshare(String companyId,int meterType) {
		return new RemoteModelResult<BaseDto>(this.waterElectShareTaskService.doWaterElectShareBillingByCompnay(companyId,  meterType));
	}

	public RemoteModelResult<MessageMap> deleteShareBsic(String companyId,String shareId) {
		return new RemoteModelResult<MessageMap>(this.tBsShareBasicService.deleteShareBsic(companyId,  shareId));
	}
	
	public RemoteModelResult<MessageMap> innvalidShareBasic(String companyId,String shareId) {
		return new RemoteModelResult<MessageMap>(this.tBsShareBasicService.innvalidShareBasic(companyId,  shareId));
	}
	
}
