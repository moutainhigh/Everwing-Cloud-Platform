package com.everwing.coreservice.wy.fee.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import com.everwing.coreservice.common.wy.fee.dto.BuildingInfoDto;
import com.everwing.coreservice.common.wy.fee.dto.LateFeeInfoDto;
import com.everwing.coreservice.common.wy.fee.dto.OrderDetailInfoDto;
import com.everwing.coreservice.common.wy.fee.dto.PreStoreInfoDto;
import com.everwing.coreservice.common.wy.fee.entity.FinaceAccount;
import com.everwing.coreservice.common.wy.fee.entity.PayDetailInfoForWeiXin;
import com.everwing.coreservice.common.wy.fee.service.AcAccountService;
import com.everwing.coreservice.common.wy.fee.service.FinaceAcAccountPageService;
import org.springframework.stereotype.Component;

/**
 * 新账户页面api
 *
 * @author DELL shiny
 * @create 2018/6/20
 */
@Component
public class FinaceAcAccountPageApi {

    @Reference(check=false)
    private FinaceAcAccountPageService finaceAcAccountPageService;
    @Reference(check=false)
    private AcAccountService acAccountService;
    
    public RemoteModelResult loadBuildingInfoByBuildingCode( BuildingInfoDto buildingInfoDto){
        return new RemoteModelResult(finaceAcAccountPageService.listPageBuildingInfo(CommonUtils.getCompanyIdByCurrRequest(),buildingInfoDto));
    }

    public RemoteModelResult<BaseDto> loadBuildingInfoByCustId(BuildingInfoDto buildingInfoDto) {
        return new RemoteModelResult(finaceAcAccountPageService.listPageBuildingInfoByCustId(CommonUtils.getCompanyIdByCurrRequest(),buildingInfoDto));
    }

    public RemoteModelResult loadBillInfoByBuildingCodeAndYear(String buildingCode,String year){
        return new RemoteModelResult(finaceAcAccountPageService.loadBillInfoByBuildingCodeAndYear(CommonUtils.getCompanyIdByCurrRequest(),buildingCode,year));
    }

    public RemoteModelResult<BaseDto> loadAccountInfoByBuildingCode(String houseCode) {
        return new RemoteModelResult<>(finaceAcAccountPageService.loadAccountInfoByBuildingCode(CommonUtils.getCompanyIdByCurrRequest(),houseCode));
    }

    public RemoteModelResult<BaseDto> listCurrentChargeDetail(OrderDetailInfoDto orderDetailInfoDto) {
        return new RemoteModelResult<>(finaceAcAccountPageService.listPageCurrentChargeDetail(CommonUtils.getCompanyIdByCurrRequest(),orderDetailInfoDto));
    }

    public RemoteModelResult<BaseDto> listLateFee(LateFeeInfoDto lateFeeInfoDto) {
        return new RemoteModelResult<>(finaceAcAccountPageService.listPageLateFee(CommonUtils.getCompanyIdByCurrRequest(),lateFeeInfoDto));
    }

    public RemoteModelResult<BaseDto> listPrestoreDetail(PreStoreInfoDto preStoreInfoDto) {
        return new RemoteModelResult<>(finaceAcAccountPageService.listPagePrestoreDetail(CommonUtils.getCompanyIdByCurrRequest(),preStoreInfoDto));
    }

    public RemoteModelResult downLoadBill(String id) {
        return new RemoteModelResult<>(finaceAcAccountPageService.downLoadBill(CommonUtils.getCompanyIdByCurrRequest(),id));
    }

    public RemoteModelResult<BaseDto> listPageBillInfoByBuildingFinace(FinaceAccount finaceAccount) {
        return new RemoteModelResult<>(finaceAcAccountPageService.listPageBillInfoByBuildingFinace(CommonUtils.getCompanyIdByCurrRequest(),finaceAccount));
    }

    public RemoteModelResult<BaseDto> jmMoney(TBsPayInfo info) {
        return new RemoteModelResult<>(finaceAcAccountPageService.jmMoney(CommonUtils.getCompanyIdByCurrRequest(),info));
    }
    public RemoteModelResult<BaseDto> pay2Account(TBsPayInfo info, String singleStr, String isNotSkipLateFee) {
        return new RemoteModelResult<>(finaceAcAccountPageService.pay2Account(CommonUtils.getCompanyIdByCurrRequest(),info,singleStr,isNotSkipLateFee));
    }
    public RemoteModelResult<BaseDto> backMoney(TBsPayInfo info) {
        return new RemoteModelResult<>(finaceAcAccountPageService.backMoney(CommonUtils.getCompanyIdByCurrRequest(),info));
    }
    
    
    public RemoteModelResult<MessageMap> payToNewAccountForWeiXin(PayDetailInfoForWeiXin entity) {
        return new RemoteModelResult<MessageMap>(acAccountService.payToNewAccountForWeiXin(CommonUtils.getCompanyIdByCurrRequest(),entity));
    }
    
}
