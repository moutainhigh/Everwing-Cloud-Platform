package com.everwing.coreservice.common.wy.service.configuration.payinfo;

import com.everwing.coreservice.common.BaseDto;

/**
 * @TODO 推送业主缴费信息到财务服务接口
 * @author qhc
 *
 */
@SuppressWarnings("rawtypes")
public interface PushPayInfoToFinanceService {
	
	
	
	BaseDto doPushPayInfoToFinance(String companyId,String projectId,String projectName,String status);
	
	BaseDto doPushPayInfo(String companyId);
	
	BaseDto tellWCPushEnd(String companyId);
	
	BaseDto doPushPayInfoByType(String companyId,String projectId,String type,String projectName,String status);
	
	void pushDataToWC(String encryptedData,String companyId,String projectId);
}
