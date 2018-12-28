package com.everwing.coreservice.common.wy.fee.service;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.fee.dto.*;

import java.util.List;

/**
 * @author shiny
 * Created by DELL on 2018/6/20.
 */
public interface AcAccountPageService {

    /**
     * 分页查询账户欠费
     * @param companyId
     * @param buildingInfoDto
     * @return
     */
    BaseDto listPageBuildingInfo(String companyId, BuildingInfoDto buildingInfoDto);

    /**
     * 通过houseCode查询账单信息
     * @param houseCode
     * @param companyId
     * @param year
     * @return
     */
    BaseDto loadBillInfoByHouseCodeAndYear(String companyId,String houseCode,String year);

    List<BillDetailPageDto> queryByHouseCodeNewAndYear(String companyId, String houseCode, String year);

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

}
