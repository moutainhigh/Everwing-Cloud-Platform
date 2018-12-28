package com.everwing.coreservice.wy.fee.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.VersionException;
import com.everwing.coreservice.common.wy.fee.constant.AcChargeDetailBusinessTypeEnum;
import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.constant.PayChannel;
import com.everwing.coreservice.common.wy.fee.dto.AcCommonAccountDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.BillDetailDto;
import com.everwing.coreservice.common.wy.fee.entity.AcCurrentChargeDetail;
import com.everwing.coreservice.common.wy.fee.entity.PayDetailInfoForWeiXin;
import com.everwing.coreservice.common.wy.fee.service.AcAccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资产账户api
 *
 * @author DELL shiny
 * @create 2018/5/16
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AcAccountApi {

    @Reference(check = false)
    private AcAccountService acAccountService;

    public RemoteModelResult addAcCommonAccountDetail(AcCommonAccountDetailDto acCommonAccountDetailDto) throws VersionException {
        //todo 校验字段
        boolean flag=acAccountService.addAcCommonAccountDetail(acCommonAccountDetailDto.getCompanyId(),
                acCommonAccountDetailDto.getProjectId(), acCommonAccountDetailDto.getProjectName(),
                acCommonAccountDetailDto.getHouseCodeNew(), acCommonAccountDetailDto.getMoney(),
                acCommonAccountDetailDto.getBusinessTypeEnum(), acCommonAccountDetailDto.getChargingType(),acCommonAccountDetailDto.getPayChannel(),
                acCommonAccountDetailDto.getDeductionDetailId(), acCommonAccountDetailDto.getDesc(),
                acCommonAccountDetailDto.getOperateDetailId(),acCommonAccountDetailDto.getOperator());
        if(flag){
            return new RemoteModelResult();
        }else {
            return new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
        }
    }

    /**
     * 专项抵扣流水变动
     * @param companyId 公司id
     * @param projectId 项目id
     * @param projectName 项目名称
     * @param houseCodeNew 建筑code
     * @param money        变动金额
     * @param chargingType 账户类型枚举
     * @param businessTypeEnum 业务类型
     * @param desc          描述
     * @param deductionDetailId 抵扣明细id
     * @param operateDetailId  前台操作明细id
     * @param operator       操作人
     * @return 通用返回结果
     */
    public RemoteModelResult addSpecialAccountDetail(String companyId, String projectId, String projectName, String houseCodeNew, BigDecimal money, ChargingType chargingType,BusinessType businessTypeEnum,PayChannel payChannel,String desc,String deductionDetailId,String operateDetailId,String operator) throws VersionException {
        //todo 校验字段
        boolean flag=acAccountService.addAcSpecialAccountDetail(companyId, projectId, projectName, houseCodeNew, money, chargingType, businessTypeEnum,payChannel, desc, deductionDetailId, operateDetailId, operator);
        if(flag){
            return new RemoteModelResult();
        }else {
            return new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
        }
    }

    /**
     * 新增上月欠费流水
     * @param companyId 公司id
     * @param projectId 项目id
     * @param projectName 项目名称
     * @param houseCodeNew 房屋编码
     * @param lastBillFee  上期欠费
     * @param chargingType 欠费账户类型
     * @param operator 操作人
     * @return 通用返回结果
     */
    public RemoteModelResult addLastBillFeeInfo(String companyId, String projectId, String projectName, String houseCodeNew, BigDecimal lastBillFee, ChargingType chargingType, String operator){
        //todo 校验字段
        boolean flag=acAccountService.addAcLastBillInfo(companyId, projectId, projectName, houseCodeNew, lastBillFee, chargingType, operator);
        if(flag){
            return new RemoteModelResult();
        }else {
            return new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
        }
    }

    /**
     * 新增每月账单明细
     * @param companyId  公司id
     * @param createTime 账单生成时间
     * @param billMonth  账单月份
     * @param billState  账单状态
     * @param billDetail 账单明细
     * @param houseCodeNew 房屋编码
     * @param billAmount  账单金额
     * @param billPayer  账单付款人
     * @param billAddress 账单地址
     * @param billInvalid 作废时间
     * @param projectId   项目id
     * @param payState   缴费状态
     * @return 通用返回结果
     */
    public RemoteModelResult addBillDetail(String companyId, Date createTime, String billMonth, int billState, BillDetailDto billDetail, String houseCodeNew, BigDecimal billAmount, String billPayer, String billAddress, Date billInvalid, String projectId, String projectName, int payState){
        boolean flag=acAccountService.addBillDetail(companyId, createTime, billMonth, billState, billDetail, houseCodeNew, billAmount, billPayer, billAddress, billInvalid, projectId,projectName, payState);
        if(flag){
            return new RemoteModelResult();
        }else {
            return new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
        }
    }

    /**
     * 新增收费结果明细
     * @param companyId 公司id
     * @param projectId 项目id
     * @param projectName 项目名称
     * @param houseCodeNew 房屋编码
     * @param auditTime  审核实际
     * @param chargeAmount 计费总金额
     * @param chargingType 计费类型
     * @param chargeTime   计费时间
     * @param lastChargeId 上次计费id
     * @param chargeDetail 计费详情
     * @param commonDiKou  通用抵扣金额
     * @param specialDiKou 专项抵扣金额
     * @param payedAmount  已付金额
     * @param assignAmount 分摊金额
     * @param payableAmount 本期应付金额
     * @param operationDetailId 前台操作明细id
     * @param operator      操作人
     * @return 通用返回结果
     */
    public RemoteModelResult addChargeDetail(String companyId, String projectId, String projectName, String houseCodeNew, Date auditTime, BigDecimal chargeAmount,
                                             ChargingType chargingType,PayChannel payChannel, Date chargeTime, String lastChargeId, String chargeDetail, BigDecimal commonDiKou, BigDecimal specialDiKou,
                                             BigDecimal payedAmount, BigDecimal assignAmount, BigDecimal payableAmount, BigDecimal currentArrears, String operationDetailId,
                                             String operator, String chargingMonth, AcChargeDetailBusinessTypeEnum chargeDetailBusinessEnum ) throws VersionException {
        boolean flag=acAccountService.addChargeDetail(companyId, projectId, projectName,houseCodeNew, auditTime, chargeAmount, chargingType,payChannel, chargeTime, lastChargeId,
        			chargeDetail, commonDiKou, specialDiKou, payedAmount, assignAmount, payableAmount, currentArrears, operationDetailId, operator,chargingMonth,chargeDetailBusinessEnum);
        if(flag){
            return new RemoteModelResult();
        }else {
            return new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
        }
    }
    
    
    /**
     * 批量插入本月收费明细表
     * @param companyId
     * @param detailList
     * @return
     */
    public RemoteModelResult<MessageMap> batchInsertAcCurrentChargeDetail(String companyId,List<AcCurrentChargeDetail> detailList){
    	return new RemoteModelResult<MessageMap>( acAccountService.batchInsertAcCurrentChargeDetail(companyId,detailList ) );
    }
    
    
    /**
     * 在所有计费结束后进行对账单数据的生成操作,按照项目进行
     * @param companyId 公司标识
     * @param projectId 项目id
     * @return
     */
    public RemoteModelResult<MessageMap> productBillInfo(String companyId,String projectId){
    	return new RemoteModelResult<MessageMap>( acAccountService.productBillInfoNew(companyId, projectId));
    }

    public RemoteModelResult<Boolean> payToNewAccountForWeiXin(String companyId, PayDetailInfoForWeiXin entity){
        MessageMap messageMap = acAccountService.payToNewAccountForWeiXin(companyId,entity);
        if(MessageMap.INFOR_SUCCESS.equals(messageMap.getFlag())){
            return new RemoteModelResult<>(true);
        }
        return new RemoteModelResult<>(false);
    }

    public RemoteModelResult<List<Map>> accountPayTypeReports (String companyId,String projectId,String startTime,String endTime){
        RemoteModelResult<List<Map>> result = new RemoteModelResult<>();
        result.setModel(acAccountService.accountPayTypeReports(companyId,projectId,startTime,endTime));
        return  result;
    }

}
