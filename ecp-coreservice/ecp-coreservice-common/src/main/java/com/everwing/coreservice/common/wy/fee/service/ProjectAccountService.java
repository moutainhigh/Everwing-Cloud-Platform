package com.everwing.coreservice.common.wy.fee.service;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.fee.constant.AccountType;
import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.constant.PayChannel;
import com.everwing.coreservice.common.wy.fee.entity.ProjectAccount;
import com.everwing.coreservice.common.wy.fee.entity.ProjectPrestoreDetail;

import java.math.BigDecimal;

/**
 * 项目账户service
 *
 * @author DELL shiny
 * @create 2018/5/16
 */
public interface ProjectAccountService {
    /**
     * 项目周期性账户变动
     * @param companyId 公司id
     * @param projectId 项目id
     * @param cycleEnum 周期性账户类型
     * @param businessType 业务类型
     * @param accountType 账户类型
     * @param money     金额
     * @param buildingCode 建筑code
     * @param streamId   业务id
     * @param logStream 日志流
     * @param operator 操作人
     * @return 通用返回结果
     */
    boolean changeCycleAccount(String companyId, String projectId, ChargingType cycleEnum, BusinessType businessType,AccountType accountType,PayChannel payChannel, BigDecimal money, String buildingCode, String streamId, String logStream, String operator);

    /**
     * 产品类收费账户变动
     * @return 是否成功A
     */
    boolean changeProductAccount(String companyId,String projectId,BigDecimal money,String orderId,String orderJson,int isAsset,String buildingCodeNew,String logStream,String operator,BigDecimal rate);

    /**
     * 滞纳金账户变动
     * @return 是否成功
     */
    boolean changeDelayAccount(String companyId, String projectId, BigDecimal money, ChargingType delayEnum, String houseCodeNew, String streamId, String logStream, String operator);

    /**
     * 项目预存账户变动
     * @param companyId 公司id
     * @param projectId 项目id
     * @param money     变动金额
     * @param businessType 业务类型
     * @param accountType   账户类型
     * @param subEnum 子类型
     * @param buildingCode 建筑编码
     * @param streamId  业务id
     * @param operator 操作人
     * @param logStream 日志流
     * @param orderId 订单id
     * @return 通用返回结果
     */
    boolean changePrestoreAccount(String companyId, String projectId, BusinessType businessType,AccountType accountType, ChargingType chargingType, BigDecimal money, String buildingCode, String streamId, String logStream, String operator,String orderId);

    /**
     * 退费账户变动
     * @return 是否成功
     */

    boolean changeRefundAccount(String companyId, String projectId, BigDecimal money, ChargingType chargingType,AccountType accountType,String orderId, String houseCodeNew, String logStream,
			String operator,String businessOperaId);

    /**
     * 罚金账户变动
     * @return 是否成功
     */
    boolean changeFineAccount(String companyId,String projectId,BigDecimal money, String logStream,String orderId,String streamId,String operator);

	
    BaseDto<ProjectAccount, Object> getProjectAccountById(String companyId,String projectId);

    /**
     * 回退
     * @param companyId
     * @param houseCodeNew
     * @param projectId
     * @param operaId
     * @return 是否成功
     */
    boolean rollbock(String companyId, String houseCodeNew, String projectId, String operaId);

}

