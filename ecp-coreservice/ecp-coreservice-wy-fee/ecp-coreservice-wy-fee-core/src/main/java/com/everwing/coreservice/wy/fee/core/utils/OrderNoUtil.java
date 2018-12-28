package com.everwing.coreservice.wy.fee.core.utils;

import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderDto;
import com.everwing.coreservice.wy.fee.dao.mapper.AcOrderMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zgrf
 * @Description: 订单号生成器
 * @Date: Create in 2018-08-19 12:53
 **/
@Component
public class OrderNoUtil {

    @Autowired
    private AcOrderMapper acOrderMapper;

    //订单号规则：项目编号+业务类型(1位)+操作人类型(1位)+客户端类型(1位)
    // +订单类型(1位)+4位时间(4位)+6位递增数字(6位)+2位随机数字(1-2位)
    // = 15-16位数字
    public String createOrderNo(AcOrderDto orderDto,AcBusinessOperaDetailDto operaDetailDto){
        int businessType = operaDetailDto.getBusinessType();
        int persionType = operaDetailDto.getPersonType();
        int clientType = operaDetailDto.getClientType();
        int orderType = orderDto.getOrderType();
        String increase = acOrderMapper.queryOrderSequence();
        int random = (int)((Math.random()*9+1)*10);
        DateTime dt1 = new DateTime();
        String date = dt1.toString("MMdd");
        StringBuilder orderNo = new StringBuilder();
        orderNo.append(operaDetailDto.getProjectId()).append(businessType).append(persionType).append(clientType).
                append(orderType).append(date).append(increase).append(random);
        return orderNo.toString();
    }

}
