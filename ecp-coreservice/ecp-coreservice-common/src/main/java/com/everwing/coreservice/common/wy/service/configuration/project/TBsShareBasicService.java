package com.everwing.coreservice.common.wy.service.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBasicsInfo;

public interface TBsShareBasicService {
	
	MessageMap addShareBasic(String companyId,TBsShareBasicsInfo entity);
	
	BaseDto listPageShareInfos(String companyId,TBsShareBasicsInfo entity);
	
	MessageMap deleteShareBsic(String companyId,String shareId);
	
	MessageMap innvalidShareBasic(String companyId,String shareId);
}
