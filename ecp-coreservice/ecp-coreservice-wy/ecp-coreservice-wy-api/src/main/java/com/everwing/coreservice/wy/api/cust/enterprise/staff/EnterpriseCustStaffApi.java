package com.everwing.coreservice.wy.api.cust.enterprise.staff;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.staff.EnterpriseCustStaffNew;
import com.everwing.coreservice.common.wy.service.cust.enterprise.staff.EnterpriseCustStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnterpriseCustStaffApi {

	
	@Autowired
	private EnterpriseCustStaffService enterpreiseCustStaffService;
	
	
	public RemoteModelResult<BaseDto> listEnterpriseCustStaffPage(String companyId,EnterpriseCustStaffNew enterpreiseCustStaff){
		return new RemoteModelResult<BaseDto>(this.enterpreiseCustStaffService.listEnterpriseCustStaffPage(companyId,enterpreiseCustStaff));
	}
	
	
	public RemoteModelResult<MessageMap> addEnterpriseCustStaff(String companyId,EnterpriseCustStaffNew enterpreiseCustStaff){
		return new RemoteModelResult<MessageMap>(this.enterpreiseCustStaffService.addEnterpriseCustStaff(companyId,enterpreiseCustStaff));
	}
	
	public RemoteModelResult<MessageMap> updateEnterpriseCustStaff(String companyId,EnterpriseCustStaffNew enterpreiseCustStaff){
		return new RemoteModelResult<MessageMap>(this.enterpreiseCustStaffService.updateEnterpriseCustStaff(companyId,enterpreiseCustStaff));
	}
	
	
	public RemoteModelResult<MessageMap> deleteEnterpriseCustStaff(String companyId,String ids){
		return new RemoteModelResult<MessageMap>(this.enterpreiseCustStaffService.deleteEnterpriseCustStaff(companyId,ids));
	}
	
	
	
}
