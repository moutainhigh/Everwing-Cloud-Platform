package com.everwing.coreservice.wy.fee.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.VersionException;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.entity.AcLateFeeBillInfo;
import com.everwing.coreservice.common.wy.fee.service.AcAccountLateFeeService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 资产违约金（滞纳金）违约金账户api
 * 	违约金区分资产账户和项目账户
 * @author qhc
 * @date 2018-05-28
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AcAccountLateFeeApi {

	@Reference(check = false)
	private AcAccountLateFeeService acAccountLateFeeService;
	
	
	 /**
     * 新增资产账户滞纳金流水
     * @param companyId 公司id
     * @param projectId 项目id
     * @param projectName 项目名称
     * @param houseCodeNew 房屋编码
     * @param money 变动金额
     * @param businessType 业务类型
     * @param desc 描述
     * @param operateDetailId 前台操作明细id
     * @param principal  本金
     * @param rate        税率
     * @param isSingle   是否单利
     * @param day        逾期天数
     * @param operator   操作人
	  *@param isPay      1支付或抵扣0.计算
     * @return 通用返回结果（如果成功返回成功的messagemap）
     */
    
	public RemoteModelResult addLateFeeAccountDetail(String companyId, String projectId, String projectName, String houseCodeNew, BigDecimal money, ChargingType chargingType, int businessType, String desc, String operateDetailId, BigDecimal principal, BigDecimal rate, int isSingle, int day, String operator, int isPay) throws VersionException {
        //todo 校验字段
        boolean flag=acAccountLateFeeService.addAcLateFeeAccountDetail(companyId, projectId, projectName, houseCodeNew, money,chargingType, businessType, desc, operateDetailId, principal,operator,isPay);
        if(flag){
            return new RemoteModelResult(new MessageMap(MessageMap.INFOR_SUCCESS, "添加资产违约金账户成功！"));
        }else {
            return new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
        }
    }
	
	
	/**
	 * 这里冗余一个插入违约金计费规则的实现
	 * @author qhc
	 * @date 2018-05-28
	 * @param entity 计费规则实体 
	 * @return MessageMap 一个包含结果和结果描述的模板
	 */
	public RemoteModelResult<MessageMap> addLateFeeBillInfo(AcLateFeeBillInfo entity){
		return new RemoteModelResult<MessageMap>(acAccountLateFeeService.addLateFeeBillInfo(entity));
	}
	
	
	
}
