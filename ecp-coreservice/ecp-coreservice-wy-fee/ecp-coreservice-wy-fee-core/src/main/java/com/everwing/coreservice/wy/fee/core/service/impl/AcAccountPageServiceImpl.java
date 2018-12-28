package com.everwing.coreservice.wy.fee.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.fee.dto.*;
import com.everwing.coreservice.common.wy.fee.entity.AcBillDetail;
import com.everwing.coreservice.common.wy.fee.service.AcAccountPageService;
import com.everwing.coreservice.wy.fee.dao.mapper.AcAccountMapper;
import com.everwing.coreservice.wy.fee.dao.mapper.AcBillDetailMapper;
import com.everwing.coreservice.wy.fee.dao.mapper.AcCurrentChargeDetailMapper;
import com.everwing.coreservice.wy.fee.dao.mapper.AcLateFeeStreamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 新账户页面功能完成
 *
 * @author DELL shiny
 * @create 2018/6/20
 */
@Service(timeout = 3000)
@Component
public class AcAccountPageServiceImpl implements AcAccountPageService {

    @Autowired
    private AcAccountMapper acAccountMapper;

    @Autowired
    private AcBillDetailMapper acBillDetailMapper;

    @Autowired
    private AcCurrentChargeDetailMapper currentChargeDetailMapper;

    @Autowired
    private AcLateFeeStreamMapper lateFeeStreamMapper;

    @Override
    public BaseDto listPageBuildingInfo(String companyId,BuildingInfoDto buildingInfoDto) {
        List<BuildingInfoDto> buildingInfoDtos=acAccountMapper.listPageBuildingInfo(buildingInfoDto);
        BaseDto baseDto=new BaseDto();
        baseDto.setLstDto(buildingInfoDtos);
        baseDto.setPage(buildingInfoDto.getPage());
        return baseDto;
    }

    @Override
    public BaseDto listPageBuildingInfoByCustId(String companyId, BuildingInfoDto buildingInfoDto) {
        List<BuildingInfoDto> buildingInfoDtos=acAccountMapper.listPageBuildingInfoByCustId(buildingInfoDto);
        BaseDto baseDto=new BaseDto();
        baseDto.setLstDto(buildingInfoDtos);
        baseDto.setPage(buildingInfoDto.getPage());
        return baseDto;
    }


    @Override
    public BaseDto downLoadBill(String companyId,String id) {
        BaseDto baseDto=new BaseDto();
        AcBillDetail acBillDetail=acBillDetailMapper.selectByPrimaryKey(id);
        BillDetailPageDto pageDto = new BillDetailPageDto(acBillDetail);
        if(pageDto!=null) {
            BillDetailDto billDetailDto=pageDto.getBillDetail();
            if(billDetailDto!=null) {
                sumChargingTypeFee(billDetailDto);
                baseDto.setObj(billDetailDto);
            }
        }else {
           throw new ECPBusinessException("账单数据异常");
        }
        return baseDto;
    }



    private void sumChargingTypeFee(BillDetailDto billDetailDto) {
        BillDto wy=billDetailDto.getWy();
        BillDto bt=billDetailDto.getBt();
        BillDto water=billDetailDto.getWater();
        BillDto elect=billDetailDto.getElect();
        sumFee(wy,billDetailDto);
        sumFee(bt,billDetailDto);
        sumFee(water,billDetailDto);
        sumFee(elect,billDetailDto);
    }

    private void sumFee(BillDto billDto,BillDetailDto billDetailDto){
        if(billDto!=null){
            billDto.setLastUnPay(CommonUtils.null2BigDecimal(billDto.getTotal()).subtract(CommonUtils.null2BigDecimal(billDto.getCurrFee())).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            billDetailDto.setTotalLateFee(CommonUtils.null2BigDecimal(billDto.getLateFee()).add(CommonUtils.null2BigDecimal(billDetailDto.getTotalLateFee())).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            billDetailDto.setLastTotalBill(CommonUtils.null2BigDecimal(billDto.getLastUnPay()).add(CommonUtils.null2BigDecimal(billDetailDto.getLastTotalBill())).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            billDetailDto.setCurrentBilling(CommonUtils.null2BigDecimal(billDto.getCurrFee()).add(CommonUtils.null2BigDecimal(billDetailDto.getCurrentBilling())).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        }
    }

    @Override
    public BaseDto loadBillInfoByHouseCodeAndYear(String companyId,String houseCode,String year) {
        BaseDto baseDto=new BaseDto();
        List<AcBillDetail> billDetails=acBillDetailMapper.selectByHouseCodeNewAndYear(houseCode,year);
        List<BillDetailPageDto> billDetailPageDtos=new ArrayList<>(billDetails.size());
        for(AcBillDetail billDetail:billDetails){
            if(null!=billDetail) {
                BillDetailPageDto pageDto = new BillDetailPageDto(billDetail);
                billDetailPageDtos.add(pageDto);
            }
        }
        baseDto.setLstDto(billDetailPageDtos);
        return baseDto;
    }

    @Override
    public List<BillDetailPageDto> queryByHouseCodeNewAndYear(String companyId,String houseCode,String year) {
        List<AcBillDetail> billDetails=acBillDetailMapper.queryByHouseCodeNewAndYear(houseCode,year);
        List<BillDetailPageDto> billDetailPageDtos=new ArrayList<>(billDetails.size());
        for(AcBillDetail billDetail:billDetails){
            if(null!=billDetail) {
                BillDetailPageDto pageDto = new BillDetailPageDto(billDetail);
                billDetailPageDtos.add(pageDto);
            }
        }
        return billDetailPageDtos;
    }

    @Override
    public BaseDto loadAccountInfoByBuildingCode(String companyId, String buildingCode) {
        BaseDto baseDto=new BaseDto();
        Map<String,Object> accountInfo=acAccountMapper.selectAccountInfoByBuildingCode(buildingCode);
        baseDto.setObj(accountInfo);
        return baseDto;
    }

    @Override
    public BaseDto listPageCurrentChargeDetail(String companyId, OrderDetailInfoDto orderDetailInfoDto) {
        BaseDto baseDto=new BaseDto();
        List<Map<String,Object>> chargeDetailList=currentChargeDetailMapper.listPageBySearchObj(orderDetailInfoDto);
        baseDto.setLstDto(chargeDetailList);
        baseDto.setPage(orderDetailInfoDto.getPage());
        return baseDto;
    }

    @Override
    public BaseDto listPageLateFee(String companyIdByCurrRequest, LateFeeInfoDto lateFeeInfoDto) {
        BaseDto baseDto=new BaseDto();
        List<Map<String,Object>> lateFeeDetailList=lateFeeStreamMapper.listPageBySearchObj(lateFeeInfoDto);
        baseDto.setLstDto(lateFeeDetailList);
        baseDto.setPage(lateFeeInfoDto.getPage());
        return baseDto;
    }

    @Override
    public BaseDto listPagePrestoreDetail(String companyId,PreStoreInfoDto preStoreInfoDto) {
        BaseDto baseDto=new BaseDto();
        List<Map<String,Object>> lateFeeDetailList=acAccountMapper.listPagePrestoreDetail(preStoreInfoDto);
        baseDto.setLstDto(lateFeeDetailList);
        baseDto.setPage(preStoreInfoDto.getPage());
        return baseDto;
    }


}
