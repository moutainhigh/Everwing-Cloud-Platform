package com.everwing.coreservice.common.wy.service.cust.enterprise;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNewSearch;

@SuppressWarnings("rawtypes")
public interface EnterpriseCustService {

	BaseDto listAllEnterpriseCust(String companyId);

	BaseDto getEnterpiseById(String companyId, String enterpriseId);

	BaseDto listPageEnterpriseCust(String companyId,EnterpriseCustNew enterpriseCustNew);

	BaseDto listPageParams(String companyId, EnterpriseCustNew enterpriseCustNew);

	BaseDto findEnterpriseCust(String companyId,EnterpriseCustNew enterpriseCustNew);

	BaseDto importExcelFiles(String companyId, Annex annex);

	BaseDto listPageEnterpriseCustNewHelp(String companyId,EnterpriseCustNew enterpriseCustNew);

	BaseDto listPageEnterpriseByName(String companyId,EnterpriseCustNew enterpriseCustNew);

	BaseDto listPageEnterprise(String companyId,EnterpriseCustNew enterpriseCustNew);

	BaseDto getEnterpriseCustNewByName(String companyId, EnterpriseCustNew enterpriseCustNew);


	/**  ------------------------------------------ 分割线 -----------------------------------------------------**/

	/**
	 * 通过企业名称 ，编号地址等条件查询业主的信息 分页
	 * @param enterpriseCustNewSearch
	 */
	BaseDto listPageEnterpriseCustNewByCondition(String companyId,EnterpriseCustNewSearch enterpriseCustNewSearch);


	/**
	 * 新的导入
	 * @param companyId
	 * @param excelPath
	 * @return
	 */
	MessageMap importEnterpriseCust(String companyId,String excelPath);

	/**
	 * 根据id删除企业客户基本信息
	 * @param enterpriseId
	 */
	BaseDto deleteEnterpriseCust(String companyId, String enterpriseId);

	/**
	 * 修改企业客户基本信息
	 * @param enterpriseCustNew
	 */
	BaseDto updateEnterpriseCust(WyBusinessContext ctx, EnterpriseCustNew enterpriseCustNew);

	/**
	 * 添加企业客户基本信息
	 * @param enterpriseCustNew
	 */
	BaseDto insertEnterprise(WyBusinessContext ctx, EnterpriseCustNew enterpriseCustNew);



}
