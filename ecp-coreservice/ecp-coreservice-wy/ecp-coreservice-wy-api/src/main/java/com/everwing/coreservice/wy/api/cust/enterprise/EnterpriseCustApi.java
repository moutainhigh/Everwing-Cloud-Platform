package com.everwing.coreservice.wy.api.cust.enterprise;

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNewSearch;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.cust.enterprise.EnterpriseCustImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.service.cust.enterprise.EnterpriseCustService;

@SuppressWarnings("rawtypes")
@Service("enterpriseCustApi")
public class EnterpriseCustApi {

	@Autowired
	private EnterpriseCustService enterpriseCustService;

	@Autowired
	private EnterpriseCustImportService enterpriseCustImportService;



	@NoExceptionProxy
	public RemoteModelResult<BaseDto> listAllEnterpriseCust(String companyId) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.listAllEnterpriseCust(companyId));
	}

	@NoExceptionProxy
	public RemoteModelResult<BaseDto> getEnterpriseById(String companyId,String enterpriseId) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.getEnterpiseById(companyId,enterpriseId));
	}


	public RemoteModelResult<BaseDto> listPageEnterpriseCust(String companyId,EnterpriseCustNew enterpriseCustNew) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.listPageEnterpriseCust(companyId,enterpriseCustNew));
	}

	public RemoteModelResult<BaseDto> listPageParams(String companyId,EnterpriseCustNew enterpriseCustNew) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.listPageParams(companyId,enterpriseCustNew));
	}

	public RemoteModelResult<BaseDto> findEnterpriseCust(String companyId,EnterpriseCustNew enterpriseCustNew) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.findEnterpriseCust(companyId,enterpriseCustNew));
	}

	public RemoteModelResult<BaseDto> importExcelFiles(String companyId,Annex annex) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.importExcelFiles(companyId,annex));
	}

	public RemoteModelResult<BaseDto> listPageEnterpriseCustNewHelp(String companyId, EnterpriseCustNew enterpriseCustNew) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.listPageEnterpriseCustNewHelp(companyId,enterpriseCustNew));
	}

	public RemoteModelResult<BaseDto> listPageEnterpriseByName(String companyId, EnterpriseCustNew enterpriseCustNew) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.listPageEnterpriseByName(companyId,enterpriseCustNew));
	}

	public RemoteModelResult<BaseDto> listPageEnterprise(String companyId,EnterpriseCustNew enterpriseCustNew) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.listPageEnterprise(companyId,enterpriseCustNew));
	}



	/**
	 *
	 * @param companyId
	 * @param enterpriseCustNew
	 * @return
	 */
	public RemoteModelResult<BaseDto> getEnterpriseCustNewByName(String companyId ,EnterpriseCustNew enterpriseCustNew) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.getEnterpriseCustNewByName(companyId,enterpriseCustNew));
	}



	/**  ------------------------------------------ 分割线 -----------------------------------------------------**/

	/**
	 * 通过企业名称 ，编号地址等条件查询业主的信息 分页
	 * @param enterpriseCustNewSearch
	 */
	public RemoteModelResult<BaseDto> listPageEnterpriseCustNewByCondition(String companyId, EnterpriseCustNewSearch enterpriseCustNewSearch) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.listPageEnterpriseCustNewByCondition(companyId,enterpriseCustNewSearch));
	}


	/**
	 * 根据id删除企业客户基本信息
	 * @param enterpriseId
	 */
	public RemoteModelResult<BaseDto> deleteEnterpriseCust(String companyId,String enterpriseId) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.deleteEnterpriseCust(companyId,enterpriseId));
	}

	/**
	 * 修改企业客户基本信息
	 * @param enterpriseCustNew
	 */
	public RemoteModelResult<BaseDto> updateEnterpriseCust(WyBusinessContext ctx, EnterpriseCustNew enterpriseCustNew) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.updateEnterpriseCust(ctx,enterpriseCustNew));
	}

	/**
	 * 添加企业客户基本信息
	 * @param enterpriseCustNew
	 */
	public RemoteModelResult<BaseDto> insertEnterprise(WyBusinessContext ctx, EnterpriseCustNew enterpriseCustNew) {
		return new RemoteModelResult<BaseDto>(this.enterpriseCustService.insertEnterprise(ctx,enterpriseCustNew));
	}


	public RemoteModelResult importEnpriseCustNew(WyBusinessContext ctx, TSysImportExportSearch tSysImportExportRequest) {
		this.enterpriseCustImportService.doImport(ctx,tSysImportExportRequest);
		RemoteModelResult result = new RemoteModelResult<>();
		return result;
	}
}
