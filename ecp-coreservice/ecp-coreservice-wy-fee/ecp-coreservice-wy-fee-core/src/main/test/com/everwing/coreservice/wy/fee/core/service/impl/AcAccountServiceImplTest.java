package com.everwing.coreservice.wy.fee.core.service.impl;

import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.dto.BillDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.BillDto;
import com.everwing.coreservice.common.wy.fee.service.AcAccountService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by DELL on 2018/5/24.
 */
public class AcAccountServiceImplTest extends BaseTest{

    @Autowired
    private AcAccountService acAccountService;

    @Test
    public void addAcCommonAccountDetail()throws Exception{
        acAccountService.addAcCommonAccountDetail("111","1","2","1122",new BigDecimal(1000), BusinessType.PRESTORE, null,null,"通用预存","wewwew","shiny");
    }

    @Test
    public void addAcSpecialAccountDetail() throws Exception {
        acAccountService.addAcSpecialAccountDetail("1","2","事务测试","1122",new BigDecimal(50), ChargingType.WATER, BusinessType.PRESTORE,"事务测试","2222","3333","shiny");
    }

    @Test
    public void addAcLastBillInfo() throws Exception{
        acAccountService.addAcLastBillInfo("1","2","上月欠费测试","1122",new BigDecimal(10), ChargingType.BT,"shiny");
    }

    @Test
    public void addBillDetail() throws Exception{
        BillDetailDto billDetailDto=new BillDetailDto();
        billDetailDto.setPostCode("1234");
        billDetailDto.setArea("12");
        billDetailDto.setBillCode("zd2342");
        billDetailDto.setBillGenTime("2018-06");
        billDetailDto.setIsCollection("是");
        billDetailDto.setCurrentBilling("500");
        billDetailDto.setCurrentTotalBill("1000");
        billDetailDto.setCustName("ceshi");
        billDetailDto.setFullName("测试一号房");
        billDetailDto.setHouseCode("1013_1000_10001_10000_10004_10001_10014_10002");
        billDetailDto.setLastTotalBill("300");
        billDetailDto.setTotalLateFee("200");
        billDetailDto.setLogoPath("1231231");
        BillDto wy=new BillDto();
        wy.setCurrFee("50");
        wy.setLastUnPay("20");
        wy.setLateFee("0");
        wy.setPrice(new BigDecimal(2.5));
        wy.setShareFee("0");
        wy.setRate("1");
        billDetailDto.setWy(wy);
        BillDto bt=new BillDto();
        bt.setCurrFee("50");
        bt.setLastUnPay("20");
        bt.setLateFee("0");
        bt.setPrice(new BigDecimal(2.5));
        bt.setShareFee("0");
        bt.setRate("1");
        billDetailDto.setBt(bt);
        BillDto water=new BillDto();
        water.setCurrFee("50");
        water.setLastUnPay("20");
        water.setLateFee("0");
        water.setPrice(new BigDecimal(2.5));
        water.setShareFee("0");
        water.setRate("1");
        water.setWaterPrice("2");
        billDetailDto.setWater(water);
        acAccountService.addBillDetail("09841dc0-204a-41f2-a175-20a6dcee0187",new Date(),"5",0,billDetailDto,"1122",new BigDecimal(12),"shiny","test",new Date(),"2","test",1);
    }

}