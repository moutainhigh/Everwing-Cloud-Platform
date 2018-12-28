package com.everwing.coreservice.wy.api.business.pay;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import com.everwing.coreservice.common.wy.service.business.pay.TBsPayInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tBsPayInfoApi")
public class TBsPayInfoApi {

	@Autowired
	private TBsPayInfoService tBsPayInfoService;



	public RemoteModelResult<BaseDto> listPage(String companyId, TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.listPage(companyId,info));
	}

	public RemoteModelResult<BaseDto> findByObj(String companyId,TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.findByObj(companyId,info));
	}

	public RemoteModelResult<BaseDto> pay2Account(String companyId,TBsPayInfo info,String singleStr,String isNotSkipLateFee) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.pay2Account(companyId,info,singleStr,isNotSkipLateFee));
	}

	public RemoteModelResult<BaseDto> pay2AccountForWeiXin(String companyId,TBsPayInfo info,String isNotSkipLateFee) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.pay2AccountForWeiXin(companyId,info,isNotSkipLateFee));
	}

	public RemoteModelResult<MessageMap> checkAccountExists(String companyId,TBsPayInfo info, String singleStr) {
		return new RemoteModelResult<MessageMap>(this.tBsPayInfoService.checkAccountExists(companyId,info,singleStr));
	}

	public RemoteModelResult<MessageMap> checkAccountExistsNew(String companyId,TBsPayInfo info, String singleStr) {
		return new RemoteModelResult<MessageMap>(this.tBsPayInfoService.checkAccountExistsNew(companyId,info,singleStr));
	}

	public RemoteModelResult<BaseDto> listPage4Building(String companyId,TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.listPage4Building(companyId,info));
	}

	public RemoteModelResult<BaseDto> findHistory(String companyId, TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.findHistory(companyId,info));
	}

	public RemoteModelResult<BaseDto> findByObj4Building(String companyId,TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.findByObj4Building(companyId,info));
	}

	public RemoteModelResult<BaseDto> findByObj4BuildingNew(String companyId,TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.findByObj4BuildingNew(companyId,info));
	}

	public RemoteModelResult<BaseDto> listPageHistory4Building(String companyId, TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.listPageHistory4Building(companyId,info));
	}

	public RemoteModelResult<BaseDto> listPageHistory4Cust(String companyId,TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.listPageHistory4Cust(companyId,info));
	}

	public RemoteModelResult<BaseDto> backMoney(String companyId,TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.backMoney(companyId,info));
	}

	public RemoteModelResult<BaseDto> jmMoney(String companyId, TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.jmMoney(companyId,info));
	}

	public RemoteModelResult<BaseDto> addInvoices(String companyId,TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.addInvoices(companyId,info));
	}

	public RemoteModelResult<BaseDto> rollback(String companyId, TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.rollback(companyId,info));
	}

	public RemoteModelResult<BaseDto> findDatas(String companyId,TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.findDatas(companyId,info));
	}

	public RemoteModelResult<BaseDto> findExportBillingDatas(String companyId,TBsPayInfo info, Integer type) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.findExportBillingDatas(companyId,info, type));
	}

	public RemoteModelResult<BaseDto> findExportPayInfoDatas(String companyId,TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.findExportPayInfoDatas(companyId,info));
	}

	public RemoteModelResult<BaseDto> findTotalDatasByObj(String companyId,TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.findTotalDatasByObj(companyId,info));
	}

	public RemoteModelResult<BaseDto> financePayAccount(String companyId, TBsPayInfo info, String singleStr, String isNotSkipLateFee) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.financePayAccount(companyId,info,singleStr,isNotSkipLateFee));
	}

	public RemoteModelResult<BaseDto> financeBackMoney(String companyId,TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.financeBackMoney(companyId,info));
	}


	public RemoteModelResult<BaseDto> findWXApplets(String companyId, TBsPayInfo info) {
		return new RemoteModelResult<BaseDto>(this.tBsPayInfoService.financeWXAppletsMoney(companyId,info));
	}
}
