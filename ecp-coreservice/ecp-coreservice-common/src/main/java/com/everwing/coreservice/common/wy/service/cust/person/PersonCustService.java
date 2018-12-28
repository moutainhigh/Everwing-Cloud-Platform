package com.everwing.coreservice.common.wy.service.cust.person;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNewSearch;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;

@SuppressWarnings("rawtypes")
public interface PersonCustService {


	/**
	 * 根据条件分页查询客户信息
	 * @param companyId
	 * @param personCustNew
	 * @return
	 */
	public BaseDto listPagePersonCustNewBySearch(String companyId, PersonCustNewSearch personCustNew);

	/**
	 * 根据id获取客户的资产绑定关系
	 * @param companyId
	 * @param custId
	 * @return
	 */
	public BaseDto getPersonBuildingByCustId(String companyId, String custId);

	/**
	 * 修改客户基本信息
	 * @param ctx
	 * @param personCustNew
	 * @return
	 */
	public BaseDto updatePersonCustNewRestful(WyBusinessContext ctx, PersonCustNew personCustNew);

	/**
	 * 添加客户基本信息不关联建筑信息
	 * @param ctx
	 * @param personCustNew
	 * @return
	 */
	public BaseDto insertPersonCustNewRestful(WyBusinessContext ctx, PersonCustNew personCustNew);


	/**
	 * 根据id删除客户基本信息
	 * @param ctx
	 * @param id
	 */
	public void deletePersonCust(WyBusinessContext ctx, String id);

	/**
	 * 根据姓名,身份证号,注册号码查询
	 * @param companyId
	 * @param personCustNew
	 * @return
	 */
	BaseDto getPersonCustNewByName(String companyId, PersonCustNew personCustNew);



	void importPersonCust(WyBusinessContext ctx, TSysImportExportSearch tSysImportExportRequest);

	BaseDto queryCustAccountInfoById(String companyId, String id);
}
