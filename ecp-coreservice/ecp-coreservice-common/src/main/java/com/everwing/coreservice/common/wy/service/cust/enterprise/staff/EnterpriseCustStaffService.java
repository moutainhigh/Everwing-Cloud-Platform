package com.everwing.coreservice.common.wy.service.cust.enterprise.staff;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.staff.EnterpriseCustStaffNew;

public interface EnterpriseCustStaffService {

	
	public BaseDto listEnterpriseCustStaffPage(String companyId,EnterpriseCustStaffNew enterpreiseCustStaff);
	
	public MessageMap addEnterpriseCustStaff(String companyId,EnterpriseCustStaffNew enterpreiseCustStaff);
	
	public MessageMap updateEnterpriseCustStaff(String companyId,EnterpriseCustStaffNew enterpreiseCustStaff);
	
	public MessageMap deleteEnterpriseCustStaff(String companyId,String ids);
	
	
	
}
