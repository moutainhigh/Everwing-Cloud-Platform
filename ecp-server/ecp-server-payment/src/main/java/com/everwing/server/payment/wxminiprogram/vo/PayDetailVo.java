package com.everwing.server.payment.wxminiprogram.vo;

import com.everwing.coreservice.common.wy.fee.dto.PayDetailDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-07-29 17:39
 **/

public class PayDetailVo implements Serializable{

    private BigDecimal payMoney = BigDecimal.ZERO;
    private String houseCodeNew;
    private List<PayDetailDto> payDetailed;

    public PayDetailVo() {
    }

    public PayDetailVo(String houseCodeNew, List<PayDetailDto> payDetailed) {
        this.houseCodeNew = houseCodeNew;
        this.payDetailed = payDetailed;
        this.payDetailed = payDetailed;
    }

    public PayDetailVo(BigDecimal payMoney, String houseCodeNew, List<PayDetailDto> payDetailed) {
        this.payMoney = payMoney;
        this.houseCodeNew = houseCodeNew;
        this.payDetailed = payDetailed;
    }

    public BigDecimal getPayMoney() {
        for (PayDetailDto payDetailDto : payDetailed) {
            payMoney = payMoney.add(new BigDecimal(payDetailDto.getMoney()).add(new BigDecimal(payDetailDto.getLateFee())));
        }
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public List<PayDetailDto> getPayDetailed() {
        return payDetailed;
    }

    public void setPayDetailed(List<PayDetailDto> payDetailed) {
        this.payDetailed = payDetailed;
    }

    @Override
    public String toString() {
        return "PayDetailVo{" +
                "payMoney=" + payMoney +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", payDetailed=" + payDetailed +
                '}';
    }
}
