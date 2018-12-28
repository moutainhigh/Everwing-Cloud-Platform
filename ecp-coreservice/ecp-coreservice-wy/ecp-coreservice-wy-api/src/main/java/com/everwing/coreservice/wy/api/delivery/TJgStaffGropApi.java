package com.everwing.coreservice.wy.api.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.delivery.TJgStaffGrop;
import com.everwing.coreservice.common.wy.service.delivery.TJgStaffGropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 * @describe  银账交割的员工组表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Service("tJgStaffGropApi")
public class TJgStaffGropApi{
	
	@Autowired
	private TJgStaffGropService tJgStaffGropService;
	
	public RemoteModelResult<BaseDto> addStaffGrop(String companyId,TJgStaffGrop entity) {
		return new RemoteModelResult<BaseDto>(this.tJgStaffGropService.addStaffGrop(companyId,entity));
	}
	
	
	public RemoteModelResult<BaseDto> getPidInfoByUserId(String companyId,String userId,String projectId) {
		return new RemoteModelResult<BaseDto>(this.tJgStaffGropService.getPidInfoByUserId(companyId,userId,projectId));
	}


	public RemoteModelResult<BaseDto> delById(String companyId, String id) {
		return new RemoteModelResult<BaseDto>(this.tJgStaffGropService.delById(companyId,id));
	}


	public RemoteModelResult<BaseDto> findInfos(String companyId,TJgStaffGrop entity) {
		return new RemoteModelResult<BaseDto>(this.tJgStaffGropService.findInfos(companyId,entity));
	}


	public RemoteModelResult<BaseDto> loadInfosToTree(String companyId,String projectId) {
		return new RemoteModelResult<BaseDto>(this.tJgStaffGropService.loadInfosToTree(companyId,projectId));
	}


	public RemoteModelResult<BaseDto> delStaffGropAndChildren(String companyId,String id) {
		return new RemoteModelResult<BaseDto>(this.tJgStaffGropService.delStaffGropAndChildren(companyId,id));
	} 
	
	public RemoteModelResult<BaseDto> getMyselfInfoByUserId(String companyId,String userId,String projectId) {
		return new RemoteModelResult<BaseDto>(this.tJgStaffGropService.getMyselfInfoByUserId(companyId,userId,projectId));
	}
	
	public RemoteModelResult<BaseDto> getStaffGroupInfo(String companyId,String userId,String projectId) {
		return new RemoteModelResult<BaseDto>(this.tJgStaffGropService.getStaffGroupInfo(companyId,userId,projectId));
	}
	
	public RemoteModelResult<BaseDto> getProjectListForRole(String companyId,String userId) {
		return new RemoteModelResult<BaseDto>(this.tJgStaffGropService.getProjectListForRole(companyId,userId));
	}
	
	
	
	
}
