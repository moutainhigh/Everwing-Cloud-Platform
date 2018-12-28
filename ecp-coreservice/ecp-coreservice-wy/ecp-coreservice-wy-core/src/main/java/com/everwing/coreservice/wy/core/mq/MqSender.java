package com.everwing.coreservice.wy.core.mq;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.fee.dto.*;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    
    @Value("${queue.acAccount.chargeDetail.batchRecharge.key}")
    private String chargeDetailForBatchRechargeKey;
    
    @Value("${queue.acAccount.chargeDetail.sunFictitious.key}")
    private String chargeDetailSunFictitious;
    
    @Value("${queue.acAccount.chargeDetail.lateFeeReduction.key}")
    private String lateFeeReduction;
    
    @Value("${queue.acAccount.deposit.refund.key}")
    private String depositAccountRefund;

    @Value("${queue.project.productDetail.key}")
    private String projectProductDetail;

    @Value("${queue.project.rollback.operation.key}")
    private  String rollbackOperation;

    @Value("${queue.acAccount.businessOpera.key}")
    private String businessOpera;

    @Value("${queue.acAccount.cycleOrderDetail.key}")
    private String cycleOrderDetail;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private final static Logger logger = Logger.getLogger(MqSender.class);

    public void sendAcCommonDetailList(List<AcCommonAccountDetailDto> list,String companyId){
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+commonAccountDetailKey+"************");
    		rabbitTemplate.convertAndSend(commonAccountDetailKey,list);
    	}
    }

    public void sendAcLateFeeList(List<AcLateFeeDto> list,String companyId){
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+lateFeeDetailKey+"************");
    		rabbitTemplate.convertAndSend(lateFeeDetailKey,list);
    	}
    }

    public void sendAcSpeciaDetailList(List<AcSpecialDetailDto> list,String companyId){
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+specialDetailKey+"************");
    		rabbitTemplate.convertAndSend(specialDetailKey,list);
    	}
    }

    public void sendAcLastBillFeeList(List<AcLastBillFeeDto> list,String companyId){
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+lastBillFeeKey+"************");
    		rabbitTemplate.convertAndSend(lastBillFeeKey,list);
    	}
    }

    public void sendAcBillDetailList(List<AcBillDetailDto> list,String companyId){
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+billDetailKey+"************");
    		rabbitTemplate.convertAndSend(billDetailKey,list);
    	}
    }

    public void sendAcChargeDetailList(List<AcChargeDetailDto> list,String companyId){
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+chargeDetailKey+"************");
    		rabbitTemplate.convertAndSend(chargeDetailKey,list);
    	}
    }

    public void sendAcCurrentChargeForUpdate(List<AcChargeDetailDto> list,String companyId) {
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+currentChargeUpdateKey+"************");
    		rabbitTemplate.convertAndSend(currentChargeUpdateKey,list);
    	}
    }
    
    public void sendAcChargeDetailForBatchRecharge(Map<String, Object> paramMap,String companyId) {
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+chargeDetailForBatchRechargeKey+"************");
    		rabbitTemplate.convertAndSend(chargeDetailForBatchRechargeKey,paramMap);
    	}
    }
    
    public void sendAcChargeDetailSunFictitious(Map<String, String> paramMap,String companyId) {
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+chargeDetailSunFictitious+"************");
    		rabbitTemplate.convertAndSend(chargeDetailSunFictitious,paramMap);
    	}
    }
    
    public void sendLateFeeReduction(Map<String, Object> paramMap,String companyId) {
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+lateFeeReduction+"************");
    		rabbitTemplate.convertAndSend(lateFeeReduction,paramMap);
    	}
    }
    
    public void sendDepositAccountRefund(Map<String, Object> paramMap,String companyId) {
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+depositAccountRefund+"************");
    		rabbitTemplate.convertAndSend(depositAccountRefund,paramMap);
    	}
    }

    public void sendProjectProductDetail(List<ProjectProductDto> projectProductDtos,String companyId){
        if( chaeckIsAllowCompany(companyId) ) {
            rabbitTemplate.convertAndSend(projectProductDetail,projectProductDtos);
        }
    }
    public void sendRollbackOperation(Map<String, Object> paramMap,String companyId) {
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+rollbackOperation+"************");
    		rabbitTemplate.convertAndSend(rollbackOperation,paramMap);
    	}
    }

    public void sendBusinessOperas(List<AcBusinessOperaDetailDto> businessOperaDtos,String companyId){
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+businessOpera+"************");
    		rabbitTemplate.convertAndSend(businessOpera,businessOperaDtos);
    	}
    }

    public void sendCycleOrderDetail(List<FrontOperaDto> frontOperaDtos,String companyId){
    	if( chaeckIsAllowCompany(companyId) ) {
    		logger.info("***********发送消息队列:"+cycleOrderDetail+"************");
    		rabbitTemplate.convertAndSend(cycleOrderDetail,frontOperaDtos);
    	}
    }

    /**
     * 这个方法是用来校验公司是否开通了新账户的
     * 之前只有深圳开通新账户，重庆，天津不开，现在要求放开2018-11-19
     * @param companyId 公司id
     * @return
     */
    public boolean chaeckIsAllowCompany(String companyId) {
        return true;
//    	if( CommonUtils.isEmpty( companyId ) ){
//    		return false;
//    	}
//    	return "09841dc0-204a-41f2-a175-20a6dcee0187".equals(companyId);
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
