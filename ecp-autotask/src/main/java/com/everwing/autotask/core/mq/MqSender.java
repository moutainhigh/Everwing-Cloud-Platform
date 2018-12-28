package com.everwing.autotask.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.wy.fee.dto.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * mq信息发送
 *
 * @author DELL shiny
 * @create 2018/6/7
 */
@Component
public class MqSender {

    @Value("${queue.tcAccount.commonAccountDetail.key}")
    private String commonAccountDetailKey;

    @Value("${queue.tcAccount.lateFeeDetail.key}")
    private String lateFeeDetailKey;

    @Value("${queue.tcAccount.specialDetail.key}")
    private String specialDetailKey;

    @Value("${queue.tcAccount.lastBillFee.key}")
    private String lastBillFeeKey;

    @Value("${queue.tcAccount.billDetail.key}")
    private String billDetailKey;

    @Value("${queue.tcAccount.chargeDetail.key}")
    private String chargeDetailKey;
    
    @Value("${queue.acAccount.currentChargeDetail.update.key}")
    private String currentChargeUpdateKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendAcCommonDetailList(List<AcCommonAccountDetailDto> list){
        rabbitTemplate.convertAndSend(commonAccountDetailKey,list);
    }

    public void sendAcLateFeeList(List<AcLateFeeDto> list){
        rabbitTemplate.convertAndSend(lateFeeDetailKey,list);
    }

    public void sendAcSpeciaDetailList(List<AcSpecialDetailDto> list){
        rabbitTemplate.convertAndSend(specialDetailKey,list);
    }

    public void sendAcLastBillFeeList(List<AcLastBillFeeDto> list){
        rabbitTemplate.convertAndSend(lastBillFeeKey,list);
    }

    public void sendAcBillDetailList(List<AcBillDetailDto> list){
        rabbitTemplate.convertAndSend(billDetailKey,list);
    }

    public void sendAcChargeDetailList(List<AcChargeDetailDto> list){
        rabbitTemplate.convertAndSend(chargeDetailKey,list);
    }
    
    public void sendAcCurrentChargeForUpdate(List<AcChargeDetailDto> list) {
    	rabbitTemplate.convertAndSend(currentChargeUpdateKey,list);
    }


    public static void main(String[] args) {
        ProjectProductDto projectProductDto=new ProjectProductDto();
        projectProductDto.setCompanyId("09841dc0-204a-41f2-a175-20a6dcee0187");
        projectProductDto.setHouseCodeNew("222");
        projectProductDto.setIsAsset(0);
        projectProductDto.setLogStream("ssss");
        projectProductDto.setMoney(new BigDecimal(100));
        projectProductDto.setOperator("system");
        projectProductDto.setOrderId("asdafads");
        projectProductDto.setOrderJson("{}");
        projectProductDto.setProjectId("1013");
        projectProductDto.setRate(new BigDecimal(0.06));
        System.out.println(JSON.toJSONString(projectProductDto));
    }
}
