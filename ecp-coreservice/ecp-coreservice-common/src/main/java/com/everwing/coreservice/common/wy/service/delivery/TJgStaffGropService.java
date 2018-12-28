package com.everwing.coreservice.common.wy.service.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.delivery.TJgStaffGrop;

/***
 * @describe  银账交割的员工组表接口
 * @author qhc
 * @ date 2017-08-31 
 */
@SuppressWarnings("rawtypes")
public interface TJgStaffGropService{
	
	
	BaseDto addStaffGrop(String companyId,TJgStaffGrop entity);
	
	BaseDto getPidInfoByUserId(String companyId,String userId ,String projectId);

	BaseDto delById(String companyId, String id);

	BaseDto findInfos(String companyId, TJgStaffGrop entity);

	BaseDto loadInfosToTree(String companyId, String projectId);

	BaseDto delStaffGropAndChildren(String companyId, String id);

	BaseDto getMyselfInfoByUserId(String companyId,String userId ,String projectId);
	
	BaseDto getStaffGroupInfo(String companyId,String userId ,String projectId);
	
	BaseDto getProjectListForRole(String companyId,String userId);
	
}
