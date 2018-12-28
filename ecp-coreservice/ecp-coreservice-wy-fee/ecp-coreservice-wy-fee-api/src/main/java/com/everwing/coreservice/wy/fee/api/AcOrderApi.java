package com.everwing.coreservice.wy.fee.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderHistoryDto;
import com.everwing.coreservice.common.wy.fee.entity.AcOrder;
import com.everwing.coreservice.common.wy.fee.service.AcOrderService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 资产账户订单api
 *
 * @author DELL shiny
 * @create 2018/5/29
 */
@Component
public class AcOrderApi {

    @Reference(check = false)
    private AcOrderService acOrderService;

    /**
     * 插入订单 周期性订单明细
     * @param companyId
     * @return 订单编号
     */
    public RemoteModelResult<String> createCycleOrderInfo(String companyId, AcOrderDto dto,AcBusinessOperaDetailDto operaDetailDto){
        String orderNo = acOrderService.createCycleOrderInfo(companyId,dto,operaDetailDto);
        return new RemoteModelResult<String>(orderNo) ;
    }

    /**
     * 查询周期性订单明细
     * @param companyId
     * @param orderNo
     * @return
     */
    public RemoteModelResult<AcOrderDto> queryCycleOrderInfoByOrderNo(String companyId,String orderNo) {
        return new RemoteModelResult<AcOrderDto>(acOrderService.queryByNo(companyId,orderNo));
    }

    /**
     * 更新订单状态
     * @param companyId
     * @param orderNo
     * @param payState
     * @param orderState
     * @return
     */
    public RemoteModelResult<Integer> updateOrderPayState(String companyId,String orderNo,int payState,int orderState,String payChannelTradeNo) {
        return new RemoteModelResult<Integer>(acOrderService.updateOrderPayState(companyId,orderNo,payState,orderState,payChannelTradeNo));
    }

    /**
     * 更新入账状态
     * @param companyId
     * @param orderNo
     * @param isRcorded
     * @return
     */
    public RemoteModelResult<Integer> updateOrderRcorded(String companyId,String orderNo,int isRcorded) {
        return new RemoteModelResult<Integer>(acOrderService.updateOrderRcorded(companyId,orderNo,isRcorded));

    }

    public RemoteModelResult<BaseDto> listPageOrderData(String companyId,AcOrder acOrder){

        return new RemoteModelResult<BaseDto>(acOrderService.listPageOrderData(companyId,acOrder));


    }

    public RemoteModelResult<MessageMap> returnOrConfirmGiveInfo(String companyId , String ids,AcOrder acOrder){

        return new RemoteModelResult<MessageMap>(acOrderService.returnOrConfirmGiveInfo(companyId,ids,acOrder));
    }

    /**
     * 查询已完成或者已入账的历史订单
     * 微信小程序专用
     * @return
     */
    public RemoteModelResult<List<AcOrderHistoryDto>> queryCompleteOrder(String companyId, String houseCode, String month,String userId) {
        List<AcOrderHistoryDto> list = acOrderService.queryCompleteOrder(companyId, houseCode,month,userId);
        return new RemoteModelResult<>(list);
    }

    public RemoteModelResult<List<AcOrderHistoryDto>> queryOrderByYear(String companyId, String houseCode, String year) {
        List<AcOrderHistoryDto> list = acOrderService.queryOrderByYear(companyId, houseCode,year);
        return new RemoteModelResult<>(list);
    }
}
