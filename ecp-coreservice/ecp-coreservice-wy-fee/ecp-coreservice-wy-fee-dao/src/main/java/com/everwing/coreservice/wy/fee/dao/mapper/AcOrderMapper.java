package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.dto.AcOrderCycleDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderHistoryDto;
import com.everwing.coreservice.common.wy.fee.entity.AcOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AcOrderMapper {

    AcOrderDto queryByNo(@Param("orderNo") String orderNo);

    int updateOrderPayState(@Param("orderNo")String orderNo, @Param("payState")int payState, @Param("orderState")int orderState,@Param("payChannelTradeNo")String payChannelTradeNo);

    int updateOrderRcorded(@Param("orderNo")String orderNo, @Param("rcorded")int rcorded);

    int insertAcOrderDto(AcOrderDto dto);

    int insertCycleOrderInfoList(List<AcOrderCycleDetailDto> list);

    List<AcOrderHistoryDto> queryOrderByStateAndDate(@Param("houseCodeNew")String houseCodeNew, @Param("month") String month,@Param("userId")String userId);
    List<AcOrderHistoryDto> queryOrderByYear(@Param("houseCodeNew")String houseCodeNew, @Param("year") String month);


    String queryOrderSequence();

    List<AcOrder> listPageOrderData(AcOrder acOrder);

    void returnOrConfirmGiveInfo(Map<String, Object> paramMap);

    void deleteOrderById(String id);


}