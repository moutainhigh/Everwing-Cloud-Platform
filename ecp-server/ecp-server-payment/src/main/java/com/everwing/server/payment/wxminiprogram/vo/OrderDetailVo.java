package com.everwing.server.payment.wxminiprogram.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.wy.fee.constant.AcPayStateEnum;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.constant.PayChannel;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderCycleDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-08-22 16:41
 **/

public class OrderDetailVo implements Serializable{

    private Date payTime;

    private String payNumber;

    private String payType;

    private String payRemark;

    private String payStatus;

    public OrderDetailVo(Date payTime, String payNumber, String payType, String payRemark, String payStatus) {
        this.payTime = payTime;
        this.payNumber = payNumber;
        this.payType = payType;
        this.payRemark = payRemark;
        this.payStatus = payStatus;
    }

    public OrderDetailVo() {
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayRemark() {
        return payRemark;
    }

    public void setPayRemark(String payRemark) {
        this.payRemark = payRemark;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public void setAcOrderDto(AcOrderDto acOrderDto){
        this.payNumber = acOrderDto.getTransactionId();
        this.payTime = acOrderDto.getPaymentTime();
        this.payType = PayChannel.getPayChannalByCode(acOrderDto.getPaymentCahnnel()).getDesc();
        this.payStatus = AcPayStateEnum.getAcPayStateEnumByCode(acOrderDto.getPayState()).getDesc();
        StringBuilder sbPayRemark = new StringBuilder();
        List<AcOrderCycleDetailDto> list = acOrderDto.getOrderCycleDetailList();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        for (AcOrderCycleDetailDto acOrderCycleDetailDto : list) {
            jsonObject = new JSONObject();
            jsonObject.put(ChargingType.getChargingTypeByCode(acOrderCycleDetailDto.getAccountType()).getDescription(),acOrderCycleDetailDto.getDetailAmount());
            jsonObject.put("违约金",acOrderCycleDetailDto.getLateAmount());
            jsonArray.add(jsonObject);
        }
        this.payRemark = jsonArray.toString();
    }

    @Override
    public String toString() {
        return "OrderDetailVo{" +
                "payTime=" + payTime +
                ", payNumber='" + payNumber + '\'' +
                ", payType='" + payType + '\'' +
                ", payRemark='" + payRemark + '\'' +
                ", payStatus='" + payStatus + '\'' +
                '}';
    }
}
