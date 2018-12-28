package com.everwing.coreservice.common.wy.service.business.pay;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;

public interface TBsPayInfoService {

	BaseDto listPage(String companyId, TBsPayInfo info);

	BaseDto findByObj(String companyId, TBsPayInfo info);

	BaseDto pay2Account(String companyId, TBsPayInfo info,String singleStr, String isNotSkipLateFee);

	MessageMap checkAccountExists(String companyId, TBsPayInfo info,String singleStr);

	BaseDto pay2AccountForWeiXin(String companyId, TBsPayInfo info,String isNotSkipLateFee);

	MessageMap checkAccountExistsNew(String companyId, TBsPayInfo info,String singleStr);

	BaseDto listPage4Building(String companyId, TBsPayInfo info);

	BaseDto findHistory(String companyId, TBsPayInfo info);

	BaseDto findByObj4Building(String companyId, TBsPayInfo info);

	BaseDto findByObj4BuildingNew(String companyId, TBsPayInfo info);

	BaseDto listPageHistory4Building(String companyId, TBsPayInfo info);

	BaseDto listPageHistory4Cust(String companyId, TBsPayInfo info);

	BaseDto backMoney(String companyId, TBsPayInfo info);

	BaseDto jmMoney(String companyId, TBsPayInfo info);

	BaseDto addInvoices(String companyId, TBsPayInfo info);

	BaseDto rollback(String companyId, TBsPayInfo info);

	BaseDto findDatas(String companyId, TBsPayInfo info);

	BaseDto findExportBillingDatas(String companyId, TBsPayInfo info, Integer type);

	BaseDto findExportPayInfoDatas(String companyId, TBsPayInfo info);

	BaseDto findTotalDatasByObj(String companyId, TBsPayInfo info);

	BaseDto financePayAccount(String companyId, TBsPayInfo info, String singleStr, String isNotSkipLateFee);

	BaseDto financeBackMoney(String companyId, TBsPayInfo info);

	BaseDto financeWXAppletsMoney(String companyId, TBsPayInfo info);
}
