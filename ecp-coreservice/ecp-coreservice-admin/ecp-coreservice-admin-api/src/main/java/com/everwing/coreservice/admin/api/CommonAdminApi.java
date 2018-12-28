package com.everwing.coreservice.admin.api;

import com.everwing.coreservice.common.admin.service.CommonAdminService;
import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.AppPkg;
import com.everwing.coreservice.common.platform.entity.generated.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CommonAdminApi {

	@Autowired
	private CommonAdminService commonAdminService;
	
	
	//Account
	public RemoteModelResult<PageBean> listAdminAccountByPage(PageBean pageBean) {
		return commonAdminService.listAdminAccountByPage(pageBean);
	}
	
	//Permission
	public RemoteModelResult<List<Permission>> listPermissionByRoleId(String roleId) {
		return commonAdminService.listPermissionByRoleId(roleId);
	}
	
	public RemoteModelResult<List<Permission>> listPermissionByAccountId(String accountId) {
		return commonAdminService.listPermissionByAccountId(accountId);
	}
	
	//Role
	public RemoteModelResult<PageBean> listRoleByPage(PageBean pageBean) {
		return commonAdminService.listRoleByPage(pageBean);
	}
	
	public RemoteModelResult<List<Map<String,Object>>> queryRoleByAccountIds(String... ids) {
		return commonAdminService.queryRoleByAccountIds(Arrays.asList(ids));
	}
	
	//APP_PKG
	public RemoteModelResult<Void> banAllPkgByType(int type) {
		return commonAdminService.banAllPkgByType(type);
	}
	
	//AdminLog
	public RemoteModelResult<PageBean> listAdminLogByPage(PageBean pageBean) {
		return commonAdminService.listAdminLogByPage(pageBean);
	}
	
	//Announcement
	public RemoteModelResult<PageBean> listAnnouncementByPage(PageBean pageBean) {
		return commonAdminService.listAnnouncementByPage(pageBean);
	}
	
	//CompanyApproval
	public RemoteModelResult<PageBean> listCompanyApprovalByPage(PageBean pageBean) {
		return commonAdminService.listCompanyApprovalByPage(pageBean);
	}
	
	//AppPkg
	public RemoteModelResult<PageBean> listAppPkgByPage(PageBean pageBean) {
		return commonAdminService.listAppPkgByPage(pageBean);
	}
	
	public RemoteModelResult<Void> deleteAppPkgByPrimaryKey(String id){
		return commonAdminService.deleteAppPkgByPrimaryKey(id);
	}
	
	public RemoteModelResult<Void> updateAppPkgByPrimaryKeySelective(AppPkg appPkg){
		return commonAdminService.updateAppPkgByPrimaryKeySelective(appPkg);
	}
	
	public RemoteModelResult<Void> addAppPkg(AppPkg appPkg){
		return commonAdminService.addAppPkg(appPkg);
	}

}