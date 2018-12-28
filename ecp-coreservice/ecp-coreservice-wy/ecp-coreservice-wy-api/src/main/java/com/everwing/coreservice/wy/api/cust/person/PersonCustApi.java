package com.everwing.coreservice.wy.api.cust.person;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNewSearch;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.common.wy.service.cust.person.PersonCustImportService;
import com.everwing.coreservice.common.wy.service.cust.person.PersonCustService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("rawtypes")
@Component
public class PersonCustApi {

	@Autowired
	private PersonCustService personCustService;

	@Autowired
	private PersonCustImportService personCustImportService;


	/**
	 * 根据条件分页查询客户信息
	 * @param companyId
	 * @param personCustNew
	 * @return
	 */
	public RemoteModelResult<BaseDto> listPagePersonCustNewBySearch(String companyId, PersonCustNewSearch personCustNew) {
		return new RemoteModelResult<BaseDto>(this.personCustService.listPagePersonCustNewBySearch(companyId,personCustNew));
	}

	/**
	 * 根据id获取客户的资产绑定关系
	 * @param companyId
	 * @param custId
	 * @return
	 */
	public RemoteModelResult<BaseDto> getPersonBuildingByCustId(String companyId, String custId) {
		return new RemoteModelResult<BaseDto>(this.personCustService.getPersonBuildingByCustId(companyId,custId));
	}

	/**
	 * 修改客户基本信息
	 * @param companyId
	 * @param personCustNew
	 * @return
	 */
	public RemoteModelResult<BaseDto> updatePersonCustNewRestful(WyBusinessContext ctx, PersonCustNew personCustNew) {
		return new RemoteModelResult<BaseDto>(this.personCustService.updatePersonCustNewRestful(ctx,personCustNew));
	}

	/**
	 * 添加客户基本信息不关联建筑信息
	 * @param companyId
	 * @param personCustNew
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public RemoteModelResult<BaseDto> insertPersonCustNewRestful(WyBusinessContext ctx, PersonCustNew personCustNew) {
		return new RemoteModelResult<BaseDto>(this.personCustService.insertPersonCustNewRestful(ctx,personCustNew));
	}

	/**
	 * 根据姓名,身份证号,注册号码查询
	 * @param companyId
	 * @param personCustNew
	 * @return
	 */
	public RemoteModelResult<BaseDto> getPersonCustNewByName(String companyId, PersonCustNew personCustNew) {
		return new RemoteModelResult<BaseDto>(this.personCustService.getPersonCustNewByName(companyId,personCustNew));
	}

	/**
	 * 根据id删除客户基本信息
	 * @param ctx
	 * @param id
	 */
	public void deletePersonCust(WyBusinessContext ctx, String id) {
		this.personCustService.deletePersonCust(ctx,id);
	}


	public RemoteModelResult importPersonCust(WyBusinessContext ctx, TSysImportExportSearch tSysImportExportRequest) {
		personCustImportService.doImport(ctx,tSysImportExportRequest);
		RemoteModelResult result = new RemoteModelResult<>();
		return result;
	}

    public RemoteModelResult<BaseDto> queryCustAccountInfo(WyBusinessContext ctx,String id) {
		String companyId=ctx.getCompanyId();
		return new RemoteModelResult<BaseDto>(personCustService.queryCustAccountInfoById(companyId,id));
    }
}
