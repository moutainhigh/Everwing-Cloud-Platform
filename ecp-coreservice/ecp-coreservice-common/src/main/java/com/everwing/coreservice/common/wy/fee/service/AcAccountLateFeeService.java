package com.everwing.coreservice.common.wy.fee.service;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.entity.AcLateFeeBillInfo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 资产违约金（滞纳金）违约金账户service接口
 * 	违约金区分资产账户和项目账户
 * @author qhc
 * @date 2018-05-28
 */
public interface AcAccountLateFeeService {


	boolean addAcLateFeeAccountDetail(String companyId, String projectId, String projectName, String houseCodeNew, BigDecimal money, ChargingType chargingType, int businessType, String desc, String operateDetailId, BigDecimal principal, String operator, int isPay) ;
	
	MessageMap addLateFeeBillInfo(AcLateFeeBillInfo entity);
	
	/**
	 * 
	 * @param companyId 公司编码
	 * @param amountMap	减免金额(这里其本身已经包含了账户的类型)
	 * @param houseCodeNew  新房号
	 */
	void addLateFeeInfoForReduction(String companyId,Map<String, String> amountMap,String houseCodeNew,String projectId,String projectName,String oprDetailId,String oprId ) ;
	
}
