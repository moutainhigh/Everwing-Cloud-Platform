package com.everwing.coreservice.wy.fee.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.VersionException;
import com.everwing.coreservice.common.wy.fee.constant.AccountType;
import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.constant.PayChannel;
import com.everwing.coreservice.common.wy.fee.service.ProjectAccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 项目预存账户Api
 *
 * @author DELL shiny
 * @create 2018/5/16
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProjectAccountApi {

    @Reference(check = false)
    private ProjectAccountService projectAccountService;
    /**
     * 项目预存账户变动
     * @param companyId 公司id
     * @param projectId 项目id
     * @param money     变动金额
     * @param businessType 业务类型
     * @param accountType 账户类型
     * @param chargingType 收费项
     * @param buildingCode 建筑编码
     * @param streamId  业务id
     * @param operator 操作人
     * @param logStream 日志流
     * @param orderId 订单id
     * @return 通用返回结果
     */
    public RemoteModelResult addPrestoreAccountDetail(String companyId, String projectId, BusinessType businessType, AccountType accountType,ChargingType chargingType, BigDecimal money, String buildingCode, String streamId, String logStream, String operator,String orderId) throws VersionException {
        //todo 参数校验
        boolean flag=projectAccountService.changePrestoreAccount(companyId,projectId,businessType,accountType,chargingType,money,buildingCode,streamId,logStream,operator,orderId);
        if(flag){
            return new RemoteModelResult();
        }else {
            return new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
        }
    }

    /**
     * 项目周期性账户变动
     * @param companyId 公司id
     * @param projectId 项目id
     * @param chargingType 收费项
     * @param businessType 业务类型
     * @param accountType  账户类型
     * @param money     金额
     * @param buildingCode 建筑code
     * @param streamId   业务id
     * @param logStream 日志流
     * @param operator 操作人
     * @return 通用返回结果
     */
    public RemoteModelResult addCycleAccountDetail(String companyId, String projectId, ChargingType chargingType, BusinessType businessType,AccountType accountType,PayChannel payChannel, BigDecimal money, String buildingCode, String streamId, String logStream, String operator) throws VersionException {
        //todo 参数校验
        boolean flag=projectAccountService.changeCycleAccount(companyId, projectId, chargingType, businessType,accountType,payChannel, money, buildingCode, streamId, logStream, operator);
        if(flag){
            return new RemoteModelResult();
        }else {
            return new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
        }
    }

    /**
     * 项目产品类账户变动
     * @param companyId 公司id
     * @param projectId 项目id
     * @param money     金额
     * @param orderId   订单id
     * @param orderJson 订单json
     * @param isAsset   是否关联资产(1关联0不关联)
     * @param buildingCode 建筑code
     * @param logStream 日志流
     * @param operator 操作人
     * @param rate 税率
     * @return 通用返回结果
     */
    public RemoteModelResult addProductAccountDetail(String companyId,String projectId,BigDecimal money,String orderId,String orderJson,int isAsset,String buildingCode,String logStream,String operator,BigDecimal rate) throws VersionException {
        //todo 参数校验
        boolean flag=projectAccountService.changeProductAccount(companyId, projectId, money, orderId, orderJson, isAsset, buildingCode,logStream, operator, rate);
        if(flag){
            return new RemoteModelResult();
        }else {
            return new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
        }
    }

    /**
     * 变动项目滞纳金账户
     * @param companyId 公司id
     * @param projectId 项目id
     * @param money     金额
     * @param delayEnum  账户类型
     * @param houseCodeNew 房屋编号
     * @param streamId    业务id
     * @param logStream   日志串
     * @param operator    操作人
     * @return 通用返回结果
     */
    public RemoteModelResult addDelayAccountDetail(String companyId, String projectId, BigDecimal money, ChargingType delayEnum,String houseCodeNew,String streamId,String logStream,String operator) throws VersionException {
        //todo 参数校验
        boolean flag=projectAccountService.changeDelayAccount(companyId, projectId,  money, delayEnum, houseCodeNew, streamId, logStream, operator);
        if(flag){
            return new RemoteModelResult();
        }else {
            return new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
        }
    }

    /**
     * 变动 项目退费
     * @param companyId 公司id
     * @param projectId 项目id
     * @param money   金额
     * @param chargingType 收费项
     * @param orderId  订单id
     * @param logStream 日志串
     * @param houseCodeNew 建筑编码 如果是通用预存和专项预存需要传入
     * @param operator 操作人id
     * @param businessOperaId 业务操作id
     * @return 返回结果
     */

   
   public RemoteModelResult addRefundAccount(String companyId,String projectId,BigDecimal money,ChargingType chargingType,AccountType accountType,String orderId,String houseCodeNew,String logStream,String operator,String businessOperaId) throws VersionException {
    boolean flag = projectAccountService.changeRefundAccount(companyId,projectId,money,chargingType,accountType,orderId,houseCodeNew,logStream,operator,businessOperaId);
    if(!flag){
        return new RemoteModelResult();
    }else{
        return new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
    }
   }

    /**
     * 变动罚金
     * @param companyId 公司id
     * @param projectId 项目id
     * @param money 金额
     * @param logStream 日志串
     * @param operator 操作人id
     * @param streamId 关联id
     * @param orderId  订单id
     * @return 返回结果
     */
   public RemoteModelResult addFineAccount(String companyId,String projectId,BigDecimal money, String logStream,String orderId,String streamId,String operator ) throws VersionException {
       boolean flag=projectAccountService.changeFineAccount(companyId,projectId,money,logStream,orderId,streamId,operator);
        if(!flag){
            return new RemoteModelResult();
        }else{
            return  new RemoteModelResult(ReturnCode.SYSTEM_ERROR);
        }
   }
   

   public RemoteModelResult getProjectAccountInfoById(String companyId,String projectId) {
	   return new RemoteModelResult<>(projectAccountService.getProjectAccountById(companyId, projectId));
   }
   
}
