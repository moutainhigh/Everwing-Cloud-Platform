package com.everwing.coreservice.admin.core.service.impl;

import com.everwing.coreservice.common.admin.service.CommonAdminService;
import com.everwing.coreservice.common.admin.util.PageBean;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.AppPkg;
import com.everwing.coreservice.common.platform.entity.generated.Permission;
import com.everwing.coreservice.platform.util.MapperResources;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CommonAdminServiceImpl extends MapperResources implements CommonAdminService {
	
	@Override
	public RemoteModelResult<Void> banAllPkgByType(int type) {
		appPkgExtraMapper.banAllPkg(type);
		return new RemoteModelResult<Void>();
	}
	
	@Override
	public RemoteModelResult<PageBean> listAdminLogByPage(PageBean pageBean) {
		pageBean.setItemList(commonAdminExtraMapper.listAdminLogByPage(pageBean));
		return new RemoteModelResult<PageBean>(pageBean);
	}
	
	@Override
	public RemoteModelResult<PageBean> listAnnouncementByPage(PageBean pageBean) {
		pageBean.setItemList(commonAdminExtraMapper.listAnnouncementByPage(pageBean));
		return new RemoteModelResult<PageBean>(pageBean);
	}
	
	@Override
	public RemoteModelResult<PageBean> listCompanyApprovalByPage(PageBean pageBean) {
		pageBean.setItemList(commonAdminExtraMapper.listCompanyApprovalByPage(pageBean));
		return new RemoteModelResult<PageBean>(pageBean);
	}

	@Override
	public RemoteModelResult<PageBean> listAppPkgByPage(PageBean pageBean) {
		pageBean.setItemList(commonAdminExtraMapper.listAppPkgByPage(pageBean));
		return new RemoteModelResult<PageBean>(pageBean);
	}

	@Override
	public RemoteModelResult<Void> deleteAppPkgByPrimaryKey(String id) {
		if (id == null) {
			throw new ECPBusinessException(ReturnCode.PF_PARAMS_MISSING);
		}
		appPkgMapper.deleteByPrimaryKey(id);
		return new RemoteModelResult<Void>();
	}

	@Override
	public RemoteModelResult<Void> addAppPkg(AppPkg appPkg) {
		if (appPkg == null) {
			throw new ECPBusinessException(ReturnCode.PF_PARAMS_MISSING);
		}
		appPkgMapper.insertSelective(appPkg);
		return new RemoteModelResult<Void>();
	}

	@Override
	public RemoteModelResult<Void> updateAppPkgByPrimaryKeySelective(AppPkg appPkg) {
		if (appPkg == null) {
			throw new ECPBusinessException(ReturnCode.PF_PARAMS_MISSING);
		}
		
		appPkgMapper.updateByPrimaryKeySelective(appPkg);
		return new RemoteModelResult<Void>();
	}

	@Override
	public RemoteModelResult<PageBean> listAdminAccountByPage(PageBean pageBean) {
		pageBean.setItemList(commonAdminExtraMapper.listAdminAccountByPage(pageBean));
		return new RemoteModelResult<PageBean>(pageBean);
	}
	
	@Override
	public RemoteModelResult<PageBean> listRoleByPage(PageBean pageBean) {
		pageBean.setItemList(commonAdminExtraMapper.listRoleByPage(pageBean));
		return new RemoteModelResult<PageBean>(pageBean);
	}
	
	@Override
	public RemoteModelResult<List<Permission>> listPermissionByRoleId(String roleId) {
		return new RemoteModelResult<List<Permission>>(commonAdminExtraMapper.listPermissionByRoleId(roleId));
	}

	@Override
	public RemoteModelResult<List<Permission>> listPermissionByAccountId(String accountId) {
		return new RemoteModelResult<List<Permission>>(commonAdminExtraMapper.listPermissionByAccountId(accountId));
	}
	
	@Override
	public RemoteModelResult<List<Map<String,Object>>> queryRoleByAccountIds(List<String> idList) {
		return new RemoteModelResult<List<Map<String,Object>>>(commonAdminExtraMapper.queryRoleByAccountIds(idList));
	}
	
}
