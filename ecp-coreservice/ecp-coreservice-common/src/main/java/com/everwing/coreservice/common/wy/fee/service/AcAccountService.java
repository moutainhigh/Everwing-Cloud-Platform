package com.everwing.coreservice.common.wy.fee.service;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.dto.BuildingAndCustDTO;
import com.everwing.coreservice.common.wy.fee.constant.AcChargeDetailBusinessTypeEnum;
import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.constant.PayChannel;
import com.everwing.coreservice.common.wy.fee.dto.*;
import com.everwing.coreservice.common.wy.fee.entity.AcCurrentChargeDetail;
import com.everwing.coreservice.common.wy.fee.entity.PayDetailInfoForWeiXin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通用账户流水service
 * @author shiny
 * Created by DELL on 2018/5/16.
 */
public interface AcAccountService {

    /**
     * 资产账户通用账户变动
     * @param companyId 公司id
     * @param projectId 项目id
     * @param projectName 项目名称
     * @param houseCodeNew 房屋编码
     * @param money        变动金额
     * @param businessTypeEnum  业务类型
     * @param chargingType 收费项
     * @param deductionDetailId 抵扣明细id
     * @param desc              描述
     * @param operateDetailId  业务操作id
     * @param operator  操作人
     * @return 通用返回结果
     */
    boolean addAcCommonAccountDetail(String companyId, String projectId, String projectName, String houseCodeNew, BigDecimal money, BusinessType businessTypeEnum, ChargingType chargingType,PayChannel payChannel, String deductionDetailId, String desc, String operateDetailId, String operator) ;

    boolean addAcSpecialAccountDetail(String companyId, String projectId, String projectName, String houseCodeNew, BigDecimal money, ChargingType chargingType, BusinessType businessTypeEnum,PayChannel payChannel, String desc, String deductionDetailId, String operateDetailId, String operator) ;

    boolean addAcLastBillInfo(String companyId, String projectId, String projectName, String houseCodeNew, BigDecimal lastBillFee, ChargingType chargingType, String operator);

    boolean addBillDetail(String companyId, Date createTime, String billMonth, int billState, BillDetailDto billDetail, String houseCodeNew, BigDecimal billAmount, String billPayer, String billAddress, Date billInvalid, String projectId, String projectName, int payState);

    boolean addChargeDetail( String companyId,String projectId,String projectName,String houseCodeNew,Date auditTime,BigDecimal chargeAmount,
    						 ChargingType chargingType,PayChannel payChannel,Date chargeTime,String lastChargeId,String chargeDetail,BigDecimal commonDiKou,BigDecimal specialDiKou,
                             BigDecimal payedAmount,BigDecimal assignAmount,BigDecimal payableAmount,BigDecimal currentArreas,String operationDetailId,
                             String operator,String chargingMoth,AcChargeDetailBusinessTypeEnum chargeDetailBusinessEnum) ;
    
    MessageMap batchInsertAcCurrentChargeDetail( String companyId,List<AcCurrentChargeDetail> insertList);
    
    MessageMap updateAcCurrentChargeDetail( String companyId, AcChargeDetailDto ChargeDetailDto ) ;
    
    /**
     * 前台做了批量操作的情况下，对新版账户的数据同步操作
     * @param companyId 公司id
     * @param houseCodeNews 涉及到修改的房号信息
     * @param amountMap 各种账户类型的金额汇总
     * @param operateId 前台操作人id
     * @return MessageMap 执行结果
     */
    MessageMap batchRechargeToNewAccount(String companyId,String operateId,List<String> houseCodeNews,Map<String, String> amountMap,List<String> monthInfo,String batchNo,int payType) ;
    
    /**
     * 根据项目进行所有项目审核后的账单数据生成工作
     * @param companyId
     * @param projectId
     * @return
     */
    MessageMap productBillInfoNew(String companyId,String projectId);
    
    boolean sumFictitiousForNewAccount(String companyId,String houseCodeNew,int accountType,BigDecimal totalAmount);
    
    boolean depositAccountRefund(String companyId, Map<String, String> amountMap,String houseCodeNew,String projectId,String projectName,String oprDetailId,String oprId) ;


    /**
     * 微信小程序交费到新账户
     * @param companyId
     * @param entity
     * @return
     */
    MessageMap payToNewAccountForWeiXin(String companyId,PayDetailInfoForWeiXin entity);

    MessageMap rollbackOperationAccount( String companyId,String operaId, String houseCodeNew, String projectId, Map<String, String> amountMap);

    /**
     * 根据房屋编码 查询欠费总额
     * @param companyId
     * @param houseCodeNews
     * @return
     */
    List<Map> queryArrearsByHouseCodeNews(String companyId,List<String> houseCodeNews,String projectId);

    List<Map> queryArrearsByBuildingName(String companyId,String buildingName,String projectId);

    BuildingAndCustInfoDto queryBuildingDetailsByhouseCode(String companyId, String projectId, String houseCode);

    List<Map<String,String>> queryCustByBuildId(String companyId,String projectId,String buildId);

    List<BillOfYearDto> queryBillByhouseCodeAndYear(String companyId,String projectId,String year,String houseCode);

    /**
     * 根据房屋编码查询各个计费项以及滞纳金欠费明细
     * @param companyId
     * @param houseCodeNew
     * @return
     */
    List<PayDetailDto> getCostByHouseCode(String companyId,String houseCodeNew);

    List<BuildingAndCustDTO> getBuindingAndCustByMobile(String companyId, String mobile,String projectId);

    /**
     * 添加导出收费总表财务需要的抵扣部分
     * @param companyId
     * @return
     */
    List<Map> accountPayTypeReports(String companyId,String projectId,String startTime,String endTime);

    Map queryArrearsByhouseCode(String companyId,String houseCode,String projectId);
}
