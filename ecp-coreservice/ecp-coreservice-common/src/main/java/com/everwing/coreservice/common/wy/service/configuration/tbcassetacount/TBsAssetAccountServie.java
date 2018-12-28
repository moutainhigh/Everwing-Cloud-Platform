package com.everwing.coreservice.common.wy.service.configuration.tbcassetacount;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.AccountPaymentEntiy;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.importEntity.TBsAssetAccountImportBean;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.AccountBillDto;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;

import java.util.List;

public interface TBsAssetAccountServie {

	/**
	 * 根据建筑编号查询5个账户是否有欠费
	 */
	BaseDto queryAccountSituationByBuildCode(WyBusinessContext ctx,String buildCode,String buildName);
	public String checkNoBillOrNoAudit(WyBusinessContext ctx, String buildCode, String buildName);
	
	/**
	 * 根据buildCode查询总欠费
	 */
	BaseDto queryTotalArrears(String companyId,List<TcBuildingList> tcBuildingList);
	
	//导入老数据
	MessageMap importBeforeDatas(WyBusinessContext ctx, String batchNo,String excelPath, String oprUserId);
	MessageMap importDatas(WyBusinessContext ctx, String batchNo,List<TBsAssetAccountImportBean> accountImportBeans, String oprUserId);

    List<AccountPaymentEntiy> getListTcBuildingAllParent(String companyId, String projectCode);

    List<AccountBillDto> getAccountBillDto(String companyId, String buildingCode);

	List<AccountPaymentEntiy> getListThirdTcBuildingAllParent(String companyId, String projectCode);
}
