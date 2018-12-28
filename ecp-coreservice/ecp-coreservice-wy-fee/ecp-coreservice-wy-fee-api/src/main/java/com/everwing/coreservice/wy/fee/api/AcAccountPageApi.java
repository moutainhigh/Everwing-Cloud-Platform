package com.everwing.coreservice.wy.fee.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.fee.dto.*;
import com.everwing.coreservice.common.wy.fee.service.AcAccountPageService;
import com.everwing.coreservice.common.wy.fee.service.AcBusinessOperaService;
import org.springframework.stereotype.Component;

/**
 * 新账户页面api
 *
 * @author DELL shiny
 * @create 2018/6/20
 */
@Component
public class AcAccountPageApi {

    @Reference(check=false)
    private AcAccountPageService acAccountPageService;

    @Reference
    private AcBusinessOperaService acBusinessOperaService;

    public RemoteModelResult loadBuildingInfoByBuildingCode( BuildingInfoDto buildingInfoDto){
        return new RemoteModelResult(acAccountPageService.listPageBuildingInfo(CommonUtils.getCompanyIdByCurrRequest(),buildingInfoDto));
    }

    public RemoteModelResult<BaseDto> loadBuildingInfoByCustId(BuildingInfoDto buildingInfoDto) {
        return new RemoteModelResult(acAccountPageService.listPageBuildingInfoByCustId(CommonUtils.getCompanyIdByCurrRequest(),buildingInfoDto));
    }

    public RemoteModelResult loadBillInfoByHouseCodeAndYear(String houseCode,String year){
        return new RemoteModelResult(acAccountPageService.loadBillInfoByHouseCodeAndYear(CommonUtils.getCompanyIdByCurrRequest(),houseCode,year));
    }

    public RemoteModelResult<BaseDto> loadAccountInfoByBuildingCode(String houseCode) {
        return new RemoteModelResult<>(acAccountPageService.loadAccountInfoByBuildingCode(CommonUtils.getCompanyIdByCurrRequest(),houseCode));
    }

    public RemoteModelResult<BaseDto> listCurrentChargeDetail(OrderDetailInfoDto orderDetailInfoDto) {
        return new RemoteModelResult<>(acAccountPageService.listPageCurrentChargeDetail(CommonUtils.getCompanyIdByCurrRequest(),orderDetailInfoDto));
    }

    public RemoteModelResult<BaseDto> listLateFee(LateFeeInfoDto lateFeeInfoDto) {
        return new RemoteModelResult<>(acAccountPageService.listPageLateFee(CommonUtils.getCompanyIdByCurrRequest(),lateFeeInfoDto));
    }

    public RemoteModelResult<BaseDto> listPrestoreDetail(PreStoreInfoDto preStoreInfoDto) {
        return new RemoteModelResult<>(acAccountPageService.listPagePrestoreDetail(CommonUtils.getCompanyIdByCurrRequest(),preStoreInfoDto));
    }

    public RemoteModelResult downLoadBill(String id) {
        return new RemoteModelResult<>(acAccountPageService.downLoadBill(CommonUtils.getCompanyIdByCurrRequest(),id));
    }

    public RemoteModelResult<BaseDto> listBusinessOperaDetail(BusinessOperaDetailDto businessOperaDetailDto) {
        return new RemoteModelResult<>(new BaseDto(acBusinessOperaService.listPageBusinessOperaDetail(CommonUtils.getCompanyIdByCurrRequest(),businessOperaDetailDto)));
    }
}
