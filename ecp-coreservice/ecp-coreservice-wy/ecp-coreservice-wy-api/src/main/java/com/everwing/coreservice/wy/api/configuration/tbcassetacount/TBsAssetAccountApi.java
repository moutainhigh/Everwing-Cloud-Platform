package com.everwing.coreservice.wy.api.configuration.tbcassetacount;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.AccountPaymentEntiy;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.AccountBillDto;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.service.configuration.tbcassetacount.TBsAssetAccountServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tBsAssetAccountApi")
public class TBsAssetAccountApi {

	@Autowired
	private TBsAssetAccountServie tBsAssetAccountServie;
	
	public RemoteModelResult<BaseDto> queryAccountSituationByBuildCode(WyBusinessContext ctx, String buildCode, String buildName){
		
		return new RemoteModelResult<BaseDto>(this.tBsAssetAccountServie.queryAccountSituationByBuildCode(ctx, buildCode,buildName));
	}
	
	
	/**
	 * 平台调用的总欠费接口
	 */
	public RemoteModelResult<BaseDto> queryTotalArrears(List<TcBuildingList> tcBuilList){
		return new RemoteModelResult<BaseDto>(this.tBsAssetAccountServie.queryTotalArrears(CommonUtils.getCompanyIdByCurrRequest(),tcBuilList));
	}


	public RemoteModelResult<MessageMap> importBeforeDatas(WyBusinessContext ctx,String batchNo, String excelPath,String oprUserId) {
		return new RemoteModelResult<MessageMap>(this.tBsAssetAccountServie.importBeforeDatas(ctx,  batchNo, excelPath,oprUserId));
	}

    public RemoteModelResult<List<AccountPaymentEntiy>> getListTcBuildingAllParent(String companyId, String projectCode) {
		RemoteModelResult<List<AccountPaymentEntiy>> result = new RemoteModelResult<>();
		result.setModel( tBsAssetAccountServie.getListTcBuildingAllParent(companyId,projectCode));
		return result;
    }

    public RemoteModelResult<List<AccountBillDto>> getAccountBillDto(String companyId, String buildingCode) {
		RemoteModelResult<List<AccountBillDto>> result = new RemoteModelResult<>();
		result.setModel( tBsAssetAccountServie.getAccountBillDto(companyId,buildingCode));
		return result;
    }

	public RemoteModelResult<List<AccountPaymentEntiy>> getListThirdTcBuildingAllParent(String companyId, String projectCode) {
		RemoteModelResult<List<AccountPaymentEntiy>> result = new RemoteModelResult<>();
		result.setModel( tBsAssetAccountServie.getListThirdTcBuildingAllParent(companyId,projectCode));
		return result;
	}
}
