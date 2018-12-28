package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.common.platform.entity.generated.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonAdminExtraMapper {
	
	
	List<Map<String,Object>> queryRoleByAccountIds(@Param("idList")List<String> idList);
	
	List<Permission> listPermissionByRoleId(@Param("roleId")String roleId);
	
	List<Permission> listPermissionByAccountId(@Param("accountId")String accountId);
	
	List<Map<String,Object>> listAdminAccountByPage(@Param("pageBean") PageBean pageBean);
	
	List<Map<String,Object>> listRoleByPage(@Param("pageBean") PageBean pageBean);
	
	List<Map<String,Object>> listAdminLogByPage(@Param("pageBean") PageBean pageBean);
	
	List<Map<String,Object>> listAnnouncementByPage(@Param("pageBean") PageBean pageBean);

	List<Map<String,Object>> listAppPkgByPage(@Param("pageBean") PageBean pageBean);
	
	List<Map<String,Object>> listCompanyApprovalByPage(@Param("pageBean") PageBean pageBean);
}