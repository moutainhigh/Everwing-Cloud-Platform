package com.everwing.coreservice.common.wy.fee.service;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderHistoryDto;
import com.everwing.coreservice.common.wy.fee.entity.AcOrder;
import com.everwing.coreservice.common.wy.fee.entity.ProjectPrestoreDetail;

import java.util.List;

/**
 * @author shiny
 * Created by DELL on 2018/5/29.
 */
public interface AcOrderService {


    AcOrderDto queryByNo(String companyId,String orderNo);

    Integer updateOrderPayState(String companyId,String orderNo,int payState,int orderState,String payChannelTradeNo);

    Integer updateOrderRcorded(String companyId,String orderNo,int rcorded);

    String createCycleOrderInfo(String companyId, AcOrderDto orderDto, AcBusinessOperaDetailDto acBusinessOperaDetailDto);
    List<AcOrderHistoryDto> queryCompleteOrder(String companyId, String houseCode, String month,String userId);
    List<AcOrderHistoryDto> queryOrderByYear(String companyId, String houseCode, String year);

    BaseDto listPageOrderData(String companyId,AcOrder acOrder);

    MessageMap returnOrConfirmGiveInfo(String companyId , String ids,AcOrder acOrder);

    String getOrderNo(String companyId,String projectId,int businessType,int personType,int clientType,int orderType );

    MessageMap insertOrder(String companyId,AcOrderDto orderDto,ProjectPrestoreDetail record);

}
