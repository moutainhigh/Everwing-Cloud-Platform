package com.everwing.coreservice.wy.dao.mapper.cust.enterprisecust.staff;

import com.everwing.coreservice.common.wy.entity.cust.enterprise.staff.EnterpriseCustStaffNew;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface EnterpriseCustStaffMapper {

	List<EnterpriseCustStaffNew> listPageEnterpriseCustStaff(EnterpriseCustStaffNew eStaff) throws DataAccessException;

	EnterpriseCustStaffNew getEnterpriseCustStaffInfo(EnterpriseCustStaffNew eStaff) throws DataAccessException;

	int addEnterpriseCustStaff(EnterpriseCustStaffNew eStaff) throws DataAccessException;

	int updateEnterpriseCustStaff(EnterpriseCustStaffNew eStaff) throws DataAccessException;

	int deleteEnterpriseCustStaff(List<String> ids) throws DataAccessException;
	

	
	
	
}
