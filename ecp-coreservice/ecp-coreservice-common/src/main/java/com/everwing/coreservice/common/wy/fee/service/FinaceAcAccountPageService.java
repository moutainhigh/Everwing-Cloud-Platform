package com.everwing.coreservice.common.wy.fee.service;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import com.everwing.coreservice.common.wy.fee.dto.BuildingInfoDto;
import com.everwing.coreservice.common.wy.fee.dto.LateFeeInfoDto;
import com.everwing.coreservice.common.wy.fee.dto.OrderDetailInfoDto;
import com.everwing.coreservice.common.wy.fee.dto.PreStoreInfoDto;
import com.everwing.coreservice.common.wy.fee.entity.FinaceAccount;

public interface FinaceAcAccountPageService {

    /**
     * 通过查询条件账单信息
    * @param companyId
     * @return
     */
    BaseDto listPageBillInfoByBuildingFinace(String companyId, FinaceAccount finaceAccount);
    /**
     * 分页查询账户欠费
     * @param companyId
     * @param buildingInfoDto
     * @return
     */
    BaseDto listPageBuildingInfo(String companyId, BuildingInfoDto buildingInfoDto);

    /**
     * 通过buildingCode查询账单信息
     * @param buildingCode
     * @param companyId
     * @param year
     * @return
     */
    BaseDto loadBillInfoByBuildingCodeAndYear(String companyId,String buildingCode,String year);

    /**
     * 获取详情头部信息
     * @param companyId
     * @param buildingCode
     * @return
     */
    BaseDto loadAccountInfoByBuildingCode(String companyId, String buildingCode);


    BaseDto listPageCurrentChargeDetail(String companyId, OrderDetailInfoDto orderDetailInfoDto);

    BaseDto listPageLateFee(String companyId, LateFeeInfoDto lateFeeInfoDto);

    BaseDto listPagePrestoreDetail(String companyId, PreStoreInfoDto preStoreInfoDto);

    BaseDto listPageBuildingInfoByCustId(String companyId, BuildingInfoDto buildingInfoDto);

    BaseDto downLoadBill(String companyId,String id);

    BaseDto jmMoney(String companyId,TBsPayInfo info);

    BaseDto pay2Account(String companyId,TBsPayInfo info, String singleStr, String isNotSkipLateFee);

    BaseDto backMoney(String companyId,TBsPayInfo info);
    

}
