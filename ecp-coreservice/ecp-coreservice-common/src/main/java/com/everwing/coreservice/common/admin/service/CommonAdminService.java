package com.everwing.coreservice.common.admin.service;

import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.AppPkg;
import com.everwing.coreservice.common.platform.entity.generated.Permission;

import java.util.List;
import java.util.Map;

public interface CommonAdminService {
	
	RemoteModelResult<List<Permission>> listPermissionByAccountId(String accountId);
	
	RemoteModelResult<PageBean> listRoleByPage(PageBean pageBean);
	
	RemoteModelResult<PageBean> listAdminLogByPage(PageBean pageBean);
	
	RemoteModelResult<PageBean> listAnnouncementByPage(PageBean pageBean);
	
	RemoteModelResult<PageBean> listCompanyApprovalByPage(PageBean pageBean);

	RemoteModelResult<PageBean> listAppPkgByPage(PageBean pageBean);

	RemoteModelResult<Void> deleteAppPkgByPrimaryKey(String id);

	RemoteModelResult<Void> updateAppPkgByPrimaryKeySelective(AppPkg appPkg);

	RemoteModelResult<Void> addAppPkg(AppPkg appPkg);

	RemoteModelResult<Void> banAllPkgByType(int type);

	RemoteModelResult<List<Permission>> listPermissionByRoleId(String roleId);

	RemoteModelResult<PageBean> listAdminAccountByPage(PageBean pageBean);

	RemoteModelResult<List<Map<String,Object>>> queryRoleByAccountIds(List<String> idList);

}
