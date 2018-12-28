package com.everwing.coreservice.wy.fee.order.api;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderDto;
import com.everwing.coreservice.common.wy.fee.entity.ProjectPrestoreDetail;
import com.everwing.coreservice.common.wy.fee.service.AcOrderService;
import com.everwing.coreservice.common.wy.fee.service.ProjectAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 资产账户订单api
 *
 * @author DELL shiny
 * @create 2018/5/29
 */
@Component
public class AcFeeOrderApi {

    @Autowired
    private AcOrderService acOrderService;

    @Autowired
    private ProjectAccountService projectAccountService;

    /**
     *获取订单号
     * @param companyId 公司id
     * @param businessType 业务类型
     * @param personType 操作人类型
     * @param clientType 客户端类型
     * @param orderType 订单类型
     * @return
     */
    public RemoteModelResult<String> getOrderNo(String companyId,String projectId,int businessType,int personType,int clientType,int orderType){
        RemoteModelResult<String> remoteModelResult = new RemoteModelResult<>();
        String orderNo = acOrderService.getOrderNo(companyId,projectId,businessType,personType,clientType,orderType);
        remoteModelResult.setModel(orderNo);
        return remoteModelResult;
    }

    /**
     *插入一条订单流水
     * @param companyId 公司id
     * @param orderDto  orderNo:订单号 amount：金额 payer：支付人 payerMobile：手机号 orderState：订单状态(1 已生成，2已完成，已作废) payState:支付状态(1，未支付，2支付中,，3部分支付，4已支付)
     *                  orderType:订单类型(1 周期性收费订单 2产品类收费订单) operaId:操作人id  paymentCahnnel:支付方式(1 微信，2支付宝，3网银，4拉卡拉)
     *                 houseCodeNew: 房号 isRcorded :是否入账(1 已入帐，2未入账)
     * @return
     */
    public RemoteModelResult<MessageMap> insertOrder(String companyId,AcOrderDto orderDto,ProjectPrestoreDetail record){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        MessageMap messageMap = acOrderService.insertOrder(companyId,orderDto,record);
        remoteModelResult.setModel(messageMap);
        return remoteModelResult;

    }




}
