package com.everwing.coreservice.common.wy.fee.utils;

import com.everwing.coreservice.common.wy.fee.constant.AcOrderTypeEnum;
import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ClientType;
import com.everwing.coreservice.common.wy.fee.constant.OperatorType;

import java.util.Calendar;

/**
 * @desc orderNo生成
 * @author DELL shiny
 * @create 2018/8/13
 */
public class OrderNoGenerator {

    public static String generateNo(BusinessType businessType, OperatorType operatorType, ClientType clientType, AcOrderTypeEnum orderTypeEnum){
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(businessType.getCode());
        stringBuffer.append(operatorType.getCode());
        stringBuffer.append(clientType.getCode());
        stringBuffer.append(orderTypeEnum.getType());
        Calendar calendar=Calendar.getInstance();
        stringBuffer.append(calendar.get(Calendar.MONTH)+1);
        stringBuffer.append(calendar.get(Calendar.DAY_OF_MONTH));
        return stringBuffer.toString();
    }
}
