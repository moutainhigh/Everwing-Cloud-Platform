package com.everwing.coreservice.wy.fee.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.datasource.DBContextHolder;
import com.everwing.coreservice.common.wy.dto.BuildingAndCustDTO;
import com.everwing.coreservice.common.wy.fee.constant.*;
import com.everwing.coreservice.common.wy.fee.dto.*;
import com.everwing.coreservice.common.wy.fee.entity.*;
import com.everwing.coreservice.common.wy.fee.service.AcAccountService;
import com.everwing.coreservice.common.wy.fee.service.ProjectAccountService;
import com.everwing.coreservice.wy.fee.dao.mapper.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.ContextLoader;
import xyz.downgoon.snowflake.Snowflake;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 资产账户业务
 *
 * @author DELL shiny
 * @create 2018/5/16
 */
@Service
@Component
public class AcAccountServiceImpl implements AcAccountService {

	private static final Logger logger = LogManager.getLogger(AcAccountServiceImpl.class);

	@Autowired
	private AcCommonAccountDetailMapper acCommonAccountDetailMapper;

	@Autowired
	private AcAccountMapper acAccountMapper;

	@Autowired
	private AcSpecialAccountMapper acSpecialAccountMapper;

	@Autowired
	private AcSpecialDetailMapper acSpecialDetailMapper;

	@Autowired
	private AcLastBillFeeInfoMapper acLastBillFeeInfoMapper;

	@Autowired
	private AcBillDetailMapper acBillDetailMapper;

	@Autowired
	private AcCurrentChargeMapper acCurrentChargeMapper;

	@Autowired
	private AcDelayAccountMapper acDelayAccountMapper;

	@Autowired
	private AcCurrentChargeDetailMapper acCurrentChargeDetailMapper;

	@Autowired
	private ProjectAccountService projectAccountService;
	
	@Autowired
	private SpringRedisTools springRedisTools;

	@Autowired
	private AcLateFeeStreamMapper acLateFeeStreamMapper;
	
	@Autowired
	private AcLateFeeBillInfoMapper acLateFeeBillInfoMapper;
	
	private static final String OWNER = "owner";  //业主app小程序交费用的常量
	
	private static final int BATCH_ADD_SIZE = 500;//批量插入，五百个一次
	
	private static final String IS_PRODUCT_BILL_STR = "IS_PRODUCT_BILL_STR";
	
	private static final String IS_PRODUCT_BILL_YES = "IS_PRODUCT_BILL_YES";


	@Override
	public MessageMap rollbackOperationAccount(String companyId,String operaId, String houseCodeNews, String projectId, Map<String, String> amountMap) {

		// 根据账户类型进行回退
		if (CommonUtils.isNotEmpty(amountMap.get("wyAmount")) && Double.valueOf(amountMap.get("wyAmount")) > 0) {
			rollbackToNewAccountByType(companyId, operaId, Double.valueOf(amountMap.get("wyAmount")),
					ChargingType.WY, houseCodeNews);
		}
		if (CommonUtils.isNotEmpty(amountMap.get("btAmount")) && Double.valueOf(amountMap.get("btAmount")) > 0) {
			rollbackToNewAccountByType(companyId, operaId, Double.valueOf(amountMap.get("btAmount")),
					ChargingType.BT, houseCodeNews);
		}
		if (CommonUtils.isNotEmpty(amountMap.get("waterAmount")) && Double.valueOf(amountMap.get("waterAmount")) > 0) {
			rollbackToNewAccountByType(companyId, operaId, Double.valueOf(amountMap.get("waterAmount")),
					ChargingType.WATER, houseCodeNews);
		}
		if (CommonUtils.isNotEmpty(amountMap.get("electAmount")) && Double.valueOf(amountMap.get("electAmount")) > 0) {
			rollbackToNewAccountByType(companyId, operaId, Double.valueOf(amountMap.get("electAmount")),
					ChargingType.ELECT, houseCodeNews);
		}
		//
		if (CommonUtils.isNotEmpty(amountMap.get("commonAmount")) && Double.valueOf(amountMap.get("commonAmount")) > 0) {


			AcCommonAccountDetail acCommonAccountDetail=acCommonAccountDetailMapper.selectByOperaId(operaId);
			acCommonAccountDetail.setBusinessType(BusinessType.REGRESSES.getCode());
			AcAccount acAccount=acAccountMapper.selectByPrimaryKey(acCommonAccountDetail.getAccountId());
			acAccount.setCommonDepositAmount(acAccount.getCommonDepositAmount().subtract(acCommonAccountDetail.getChangeAmount()));
			acCommonAccountDetailMapper.updateByPrimaryKey(acCommonAccountDetail);
            acAccountMapper.updateByPrimaryKey(acAccount);

		}
		projectAccountService.rollbock(companyId, houseCodeNews, projectId,operaId);
		return new MessageMap(MessageMap.INFOR_SUCCESS, "回退操作成功！");

	}

	private void rollbackToNewAccountByType(String companyId, String operaId, Double amount, ChargingType type, String houseCodeNew) {
		// 个人资产户表
		AcAccount acAccount=acAccountMapper.selectByHouseCodeNew(houseCodeNew);
           BigDecimal amountBig =new BigDecimal(amount);

		//滞纳金
		AcLateFeeStream acLateFeeStream=acLateFeeStreamMapper.selectByOperaIdandType(operaId,houseCodeNew,type.getCode());
		if(!CommonUtils.isEmpty(acLateFeeStream)){

				acLateFeeStream.setBusinessType((short)LatefeeBusinessTypeEnum.ROLLBACK_LATE_FEE.getCode());
				//获取滞纳金信息表
				AcDelayAccount acDelayAccount = acDelayAccountMapper.selectByPrimaryKey(acLateFeeStream.getDelayAccountId());
				//滞纳金 信息表金额=回退前的金额-流水表的变动金额
				acDelayAccount.setAmount(acDelayAccount.getAmount().add(acLateFeeStream.getChangeAmount()));
				acLateFeeStreamMapper.updateByPrimaryKey(acLateFeeStream);
				acDelayAccountMapper.updateByPrimaryKeySelective(acDelayAccount);
				//资产账户表获取滞纳金总金额
				acAccount.setLateFeeAmount(acAccount.getLateFeeAmount().add(acLateFeeStream.getChangeAmount()));
			     amountBig=amountBig.subtract(acLateFeeStream.getChangeAmount());



		}
		//专项流水表
		AcSpecialDetail acSpecialDetail=acSpecialDetailMapper.selectByOperaIdAndType(operaId,houseCodeNew,type.getCode());

		if(!CommonUtils.isEmpty(acSpecialDetail)){

				acSpecialDetail.setBusinessType((short) BusinessType.REGRESSES.getCode());
				//获取专项抵扣账户表
				AcSpecialAccount acSpecialAccount=acSpecialAccountMapper.selectByPrimaryKey(acSpecialDetail.getSpecialId());
				//  专项抵扣账户表金额
				acSpecialAccount.setSpecialAmount(acSpecialAccount.getSpecialAmount().subtract(acSpecialDetail.getChangeAmount()));
                acSpecialDetailMapper.updateByPrimaryKey(acSpecialDetail);
				acSpecialAccountMapper.updateByPrimaryKey(acSpecialAccount);
				//获取专项抵扣总额
				acAccount.setSpecialDepositAmount(acAccount.getSpecialDepositAmount().subtract(acSpecialDetail.getChangeAmount()));
			    amountBig=amountBig.subtract(acSpecialDetail.getChangeAmount());

		}
		//收费结果明细表
		List<AcCurrentChargeDetail>acCurrentChargeDetails=acCurrentChargeDetailMapper.selectByOperaIdAndType(operaId,type.getCode());
		if(!CommonUtils.isEmpty(acCurrentChargeDetails)){
		for (AcCurrentChargeDetail acCurrentChargeDetail:acCurrentChargeDetails){
			acCurrentChargeDetail.setAccountType((short)AcChargeDetailBusinessTypeEnum.ROLLBACK.getCode());
			AcLastBillFeeInfo acLastBillFeeInfo=acLastBillFeeInfoMapper.selectByCodeAccountIdAndType(houseCodeNew,acAccount.getId(),(short)type.getCode());
			acLastBillFeeInfo.setLastBillFee(amountBig);
			acLastBillFeeInfoMapper.updateByPrimaryKeyForLastBill(acLastBillFeeInfo);
			acAccount.setLastArrearsAmount(acAccount.getLastArrearsAmount().add(acCurrentChargeDetail.getPayedAmount()));
			acCurrentChargeDetailMapper.updateByPrimaryKey(acCurrentChargeDetail);

		}
		}

		acAccount.setModifyTime(new Date());
		acAccountMapper.updateByPrimaryKey(acAccount);
	}


	@Override
	public boolean addAcCommonAccountDetail(String companyId, String projectId, String projectName, String houseCodeNew,
			BigDecimal money, BusinessType businessTypeEnum, ChargingType chargingType,PayChannel payChannel,
			String deductionDetailId, String desc, String operateDetailId, String operator)  {
		String logStream = UUID.randomUUID().toString();
		logger.info("{}增加资产账户通用流水开始,查询资产账户", logStream);
		AcAccount acAccount = acAccountMapper.selectByHouseCodeNew(houseCodeNew);
		String acAccountId = null;
		Date time = new Date();
		if (null == acAccount) {
			logger.info("{}未查询到资产账户,开始创建资产账户", logStream);
			acAccount = createAcAccount(acAccount, acAccountId, time, houseCodeNew, projectId, projectName);
			logger.info("{}资产账户创建完成.");
		} else {
			logger.info("{}查询到资产账户", logStream);
		}
		acAccountId = acAccount.getId();
		logger.info("{}开始创建资产账户通用流水", logStream);
		AcCommonAccountDetail acCommonAccountDetail = new AcCommonAccountDetail();
		acCommonAccountDetail.setId(logStream);
		if (businessTypeEnum.equals(BusinessType.PRESTORE)) {
			acCommonAccountDetail.setAfterAmount(acAccount.getCommonDepositAmount().add(money));
		} else if (businessTypeEnum.equals(BusinessType.DEDUCTIBLE)
				|| businessTypeEnum.equals(BusinessType.REFUND)) {
			acCommonAccountDetail.setAfterAmount(acAccount.getCommonDepositAmount().subtract(money));
		}
		acCommonAccountDetail.setBeforeAmount(acAccount.getCommonDepositAmount());
		acCommonAccountDetail.setChangeAmount(money);
		acCommonAccountDetail.setAccountId(acAccountId);
		acCommonAccountDetail.setBillDetailId(deductionDetailId);
		acCommonAccountDetail.setBusinessType(businessTypeEnum.getCode());
		acCommonAccountDetail.setCreateId(operator);
		acCommonAccountDetail.setCreateTime(time);
		acCommonAccountDetail.setDescription(desc);
		if (null != chargingType) {
			acCommonAccountDetail.setDikouType(chargingType.getCode());
		}
		acCommonAccountDetail.setHouseCodeNew(houseCodeNew);
		acCommonAccountDetail.setOperaId(operateDetailId);
		acCommonAccountDetail.setProjectId(projectId);
		acCommonAccountDetail.setProjectName(projectName);
		acCommonAccountDetail.setAccountId(acAccountId);
		acCommonAccountDetailMapper.insert(acCommonAccountDetail);
		logger.info("{}资产通用账户通用流水创建成功", logStream);
		logger.info("{}开始汇总到资产账户");
		acAccount.setCommonDepositAmount(acCommonAccountDetail.getAfterAmount());
		// 账户总金额=通用账户+预存账户
//		acAccount.setTotalAmount(acAccount.getCommonDepositAmount().add(acAccount.getSpecialDepositAmount()));
		int count = acAccountMapper.updateByPrimaryKeySelective(acAccount);
		if (count == 0) {
			logger.info("{}汇总到资产账户失败", logStream);
			throw new ECPBusinessException("汇总到资产账户失败!");
		} else {
			logger.info("{}汇总到资产账户成功", logStream);
			if (businessTypeEnum.equals(BusinessType.PRESTORE)) {
				projectAccountService.changePrestoreAccount(companyId, projectId, businessTypeEnum,
						AccountType.COMMON,null, money, houseCodeNew, operateDetailId, logStream, operator,null);
			} else if (businessTypeEnum.equals(BusinessType.DEDUCTIBLE)) {
				projectAccountService.changeCycleAccount(companyId, projectId, chargingType, businessTypeEnum,AccountType.COMMON,payChannel, money,
						houseCodeNew, operateDetailId, logStream, operator);
				projectAccountService.changePrestoreAccount(companyId, projectId, businessTypeEnum,AccountType.COMMON,
						chargingType, money, houseCodeNew, operateDetailId, logStream, operator, null);
			} else if (businessTypeEnum.equals(BusinessType.REFUND)) {
				projectAccountService.changeRefundAccount(companyId, projectId, money,
						chargingType, AccountType.COMMON,null, houseCodeNew, logStream, operator,operateDetailId);
			} else if (businessTypeEnum.equals(BusinessType.PAYMENT)){
				projectAccountService.changeCycleAccount(companyId, projectId, chargingType, businessTypeEnum,AccountType.COMMON,payChannel, money,
						houseCodeNew, operateDetailId, logStream, operator);
			}
			return true;
		}
	}

	@Override
	public boolean addAcSpecialAccountDetail(String companyId, String projectId, String projectName,
			String houseCodeNew, BigDecimal money, ChargingType chargingType,
			BusinessType businessTypeEnum,PayChannel payChannel, String desc, String deductionDetailId, String operateDetailId,
			String operator)  {
		String logStream = UUID.randomUUID().toString();
		logger.info("{}增加资产账户专项抵扣流水开始,查询资产账户", logStream);
		AcAccount acAccount = acAccountMapper.selectByHouseCodeNew(houseCodeNew);
		String acAccountId = null;
		Date time = new Date();
		if (null == acAccount) {
			logger.info("{}未查询到资产账户,开始创建资产账户", logStream);
			acAccount = createAcAccount(acAccount, acAccountId, time, houseCodeNew, projectId, projectName);
			acAccountId = acAccount.getId();
			logger.info("{}资产账户创建完成.");
		} else {
			logger.info("{}查询到资产账户", logStream);
			acAccountId = acAccount.getId();
		}
		logger.info("{}开始查找资产专项抵扣账户",logStream);
		AcSpecialAccount specialAccount = acSpecialAccountMapper.selectByAccountIdAndTypeAndHouseCodeNew(acAccountId,
				chargingType.getCode(), houseCodeNew);
		String specialId;
		if (null == specialAccount) {
			logger.info("{}未查询到资产专项抵扣账户,开始创建", logStream);
			specialAccount = createAcSpecialAccount(houseCodeNew, chargingType, acAccountId, projectId, projectName,
					operator, time);
			logger.info("{}资产专项抵扣账户创建完成", logStream);
		} else {
			logger.info("{}查询到资产专项抵扣账户", logStream);
		}
		specialId = specialAccount.getId();
		logger.info("{}开始创建资产专项抵扣流水", logStream);


		//这里调整一下beforeAmont和afterAmount的值
		BigDecimal beforeAmount = CommonUtils.isEmpty( specialAccount.getSpecialAmount() ) ? BigDecimal.valueOf(0) : specialAccount.getSpecialAmount() ;
		BigDecimal afterAmount = beforeAmount;
		
		
		if (businessTypeEnum.equals(BusinessType.PRESTORE)) {
			specialAccount.setSpecialAmount(specialAccount.getSpecialAmount().add(money));
			acAccount.setSpecialDepositAmount(acAccount.getSpecialDepositAmount().add(money));
			afterAmount = afterAmount.add(money);
		} else {
			specialAccount.setSpecialAmount(specialAccount.getSpecialAmount().subtract(money));
			acAccount.setSpecialDepositAmount(acAccount.getSpecialDepositAmount().subtract(money));
			afterAmount = beforeAmount.subtract(money);
//			money = new BigDecimal(0).subtract(money);
		}

		createAcSpecialDetail(logStream, specialId, houseCodeNew, money, businessTypeEnum, projectId, projectName, time,
				operator, desc, deductionDetailId, operateDetailId,beforeAmount,afterAmount);

		logger.info("{}资产专项抵扣流水写入成功!开始汇总到资产专项抵扣账户", logStream);

		specialAccount.setModifyTime(time);
		specialAccount.setModifyId(operator);
		int count = acSpecialAccountMapper.updateByPrimaryKey(specialAccount);
		if (count == 0) {
			logger.error("{}汇总到资产专项抵扣账户失败!", logStream);
			throw new ECPBusinessException("汇总到资产专项抵扣账户失败");
		} else {
			logger.info("汇总到资产专项抵扣账户成功,开始汇总到资产账户", logStream);
			// 通用+专项-滞纳金-当月计费-上月欠费
			acAccount.setTotalAmount(acAccount.getCommonDepositAmount().add(acAccount.getSpecialDepositAmount()));
			acAccount.setModifyTime(time);
			acAccount.setModifyId(operator);
			int updateCount = acAccountMapper.updateByPrimaryKey(acAccount);
			if (updateCount == 0) {
				logger.error("{}汇总到资产账户失败", logStream);
				throw new ECPBusinessException("汇总到资产账户失败");
			} else {
				logger.info("{}汇总到资产账户成功!", logStream);
				if (businessTypeEnum.equals(BusinessType.PRESTORE)) {
					projectAccountService.changePrestoreAccount(companyId, projectId, businessTypeEnum,AccountType.SPECIAL,
							chargingType, money.abs(), houseCodeNew, operateDetailId, logStream, operator,null);
				} else if (businessTypeEnum.equals(BusinessType.DEDUCTIBLE)) {
					projectAccountService.changePrestoreAccount(companyId, projectId, businessTypeEnum,AccountType.SPECIAL,
							chargingType, money.abs(), houseCodeNew, operateDetailId, logStream, operator,null);
					projectAccountService.changeCycleAccount(companyId, projectId, chargingType,businessTypeEnum,AccountType.SPECIAL,payChannel,
							 money.abs(), houseCodeNew, operateDetailId, logStream,operator);
				} else if (businessTypeEnum.equals(BusinessType.REFUND)) {
					projectAccountService.changeRefundAccount(companyId, projectId, money.abs(), chargingType,AccountType.SPECIAL, null,
							houseCodeNew, logStream, operator,operateDetailId);
				} else if (businessTypeEnum.equals(BusinessType.PAYMENT)){
					projectAccountService.changeCycleAccount(companyId, projectId, chargingType,businessTypeEnum,AccountType.SPECIAL,payChannel,
							money.abs(), houseCodeNew, operateDetailId, logStream,operator);
				}
				return true;
			}
		}
	}

	@Override
	public boolean addAcLastBillInfo(String companyId, String projectId, String projectName, String houseCodeNew,
			BigDecimal lastBillFee, ChargingType chargingType, String operator) {
		String logStream = UUID.randomUUID().toString();
		logger.info("{}增加资产账户上月欠费流水开始,查询资产账户", logStream);
		AcAccount acAccount = acAccountMapper.selectByHouseCodeNew(houseCodeNew);
		String acAccountId = null;
		Date time = new Date();
		if (null == acAccount) {
			logger.info("{}未查询到资产账户,开始创建资产账户", logStream);
			acAccount = createAcAccount(acAccount, acAccountId, time, houseCodeNew, projectId, projectName);
			acAccountId = acAccount.getId();
			logger.info("{}资产账户创建完成.");
		} else {
			logger.info("{}查询到资产账户", logStream);
			acAccountId = acAccount.getId();
		}
		logger.info("{}开始创建资产账户上月欠费流水", logStream);
		createAcLastBillInfo(logStream, houseCodeNew, lastBillFee, acAccountId, projectId, projectName, operator, time,
				chargingType);
		logger.info("{}资产账户上月欠费流水创建完成", logStream);
		logger.info("{}开始汇总上月欠费到资产账户");
		acAccount.setLastArrearsAmount(acAccount.getLastArrearsAmount().add(lastBillFee));
		acAccount.setTotalAmount(acAccount.getCommonDepositAmount().add(acAccount.getSpecialDepositAmount()));
		acAccount.setModifyId(operator);
		acAccount.setModifyTime(time);
		int count = acAccountMapper.updateByPrimaryKey(acAccount);
		if (count == 0) {
			logger.error("{}汇总上月欠费到资产账户失败!", logStream);
			throw new ECPBusinessException("汇总上月欠费到资产账户失败");
		} else {
			logger.info("{}汇总上月欠费到资产账户成功!", logStream);
			return true;
		}
	}

	@Override
	public boolean addBillDetail(String companyId, Date createTime, String billMonth, int billState,
			BillDetailDto billDetail, String houseCodeNew, BigDecimal billAmount, String billPayer, String billAddress,
			Date billInvalid, String projectId, String projectName, int payState) {
		String logStream = UUID.randomUUID().toString();
		logger.info("{}增加资产账户每月账单明细,查询资产账户", logStream);
		AcAccount acAccount = acAccountMapper.selectByHouseCodeNew(houseCodeNew);
		String acAccountId = null;
		Date time = new Date();
		if (null == acAccount) {
			logger.info("{}未查询到资产账户,开始创建资产账户", logStream);
			acAccount = createAcAccount(acAccount, acAccountId, time, houseCodeNew, projectId, projectName);
			logger.info("{}资产账户创建完成.");
		} else {
			logger.info("{}查询到资产账户", logStream);
		}
		acAccountId = acAccount.getId();
		logger.info("{}开始创建每月账单明细", logStream);
		createBillDetail(logStream, acAccountId, createTime, billMonth, billState, billDetail, houseCodeNew, billAmount,
				billPayer, billAddress, billInvalid, projectId, projectName, payState);
		logger.info("{}每月账单明细创建完成", logStream);
		return true;
	}

	@Override
	public boolean addChargeDetail(String companyId, String projectId, String projectName, String houseCodeNew,
			Date auditTime, BigDecimal chargeAmount, ChargingType chargingType,PayChannel payChannel, Date chargeTime,
			String lastChargeId, String chargeDetail, BigDecimal commonDiKou, BigDecimal specialDiKou,
			BigDecimal payedAmount, BigDecimal assignAmount, BigDecimal payableAmount, BigDecimal currentArreas,
			String operationDetailId, String operator, String chargingMonth,
			AcChargeDetailBusinessTypeEnum chargeDetailBusinessEnum)  {
		String logStream = UUID.randomUUID().toString();
		logger.info("{}增加收费结果明细开始,查询资产账户", logStream);
		AcAccount acAccount = acAccountMapper.selectByHouseCodeNew(houseCodeNew);
		String acAccountId = null;
		Date time = new Date();
		if (null == acAccount) {
			logger.info("{}未查询到资产账户,开始创建资产账户", logStream);
			acAccount = createAcAccount(acAccount, acAccountId, time, houseCodeNew, projectId, projectName);
			acAccountId = acAccount.getId();
			logger.info("{}资产账户创建完成.");
		} else {
			logger.info("{}查询到资产账户", logStream);
			acAccountId = acAccount.getId();
		}
		logger.info("{}开始查询当月收费账户", logStream);
		AcCurrentCharge acCurrentCharge = acCurrentChargeMapper.selectByAccountIdTypeAndHouseCodeNew(acAccountId,
				chargingType.getCode(), houseCodeNew);
		String currentChargeId;
		if (null == acCurrentCharge) {
			logger.info("{}未查询到当月收费账户,开始创建", logStream);
			acCurrentCharge = createCurrentCharge(acAccountId, houseCodeNew, chargeAmount, chargingType,
					chargingMonth, auditTime, projectId, projectName, time, operator);
		} else {
			logger.info("{}查询到当月收费账户", logStream);
		}
		currentChargeId = acCurrentCharge.getId();
		logger.info("{}开始创建收费结果明细", logStream);
		
		// 收费结果明细表增加字段  此处的创建方法目前只有计费的会用到，计费对于本次支付金额，暂时给0  2018-09-01 qhc
		createChargeDetail(logStream, houseCodeNew, chargeAmount, currentChargeId, chargingType, chargeTime,
				auditTime, lastChargeId, chargeDetail, commonDiKou, specialDiKou, projectId, projectName, operator,
				time, payedAmount, assignAmount, payableAmount, currentArreas, operationDetailId,
				chargeDetailBusinessEnum,BigDecimal.valueOf(0));
		logger.info("{}收费结果明细创建完成,向当月收费汇总金额", logStream);

		// 这里需要根据业务类型进行一个判断，如果是计费审合格就执行，其他略过
		// 对当月收费总金额的汇总应该发生在审核，抵扣，交费的时候其他不需要汇总
//		 if( 1 == chargeDetailBusinessEnum.getCode() ) {
//			 //计费不进当月计费（收费信息表）
//			 
//		 }else {
//			 
//		 }
//		acCurrentCharge.setCurrentBillFee(acCurrentCharge.getCurrentBillFee().add(chargeAmount));
//		int count = acCurrentChargeMapper.updateByPrimaryKey(acCurrentCharge);
//		if (count == 0) {
//			logger.error("{}汇总到当月收费账户失败!", logStream);
//			throw new ECPBusinessException("汇总到当月收费账户失败");
//		} else {
			logger.info("汇总到资产当月收费账户成功,开始汇总到资产账户", logStream);
//			acAccount.setCurrentBillAmount(acAccount.getCurrentBillAmount().add(chargeAmount));
			// 通用+专项
//			acAccount.setTotalAmount(acAccount.getCommonDepositAmount().add(acAccount.getSpecialDepositAmount()));
			acAccount.setModifyTime(time);
			acAccount.setModifyId(operator);
			int updateCount = acAccountMapper.updateByPrimaryKey(acAccount);
			if (updateCount == 0) {
				logger.error("{}汇总到资产账户失败", logStream);
				throw new ECPBusinessException("汇总到资产账户失败");
			} else {
				logger.info("{}汇总到资产账户成功!", logStream);
				if(chargeDetailBusinessEnum.equals(AcChargeDetailBusinessTypeEnum.PAY)){
					projectAccountService.changeCycleAccount(companyId,projectId,chargingType,BusinessType.PAYMENT,AccountType.SPECIAL,payChannel,chargeAmount,houseCodeNew,operationDetailId,logStream,operator);
				}
				return true;
			}
//		}
		// }
		// return true;
	}

	@Override
	public MessageMap batchInsertAcCurrentChargeDetail(String companyId, List<AcCurrentChargeDetail> insertList) {
		return null;
	}

	private void createChargeDetail(String logStream, String houseCodeNew, BigDecimal chargeAmount,
			String currentChargeId, ChargingType chargingType, Date chargeTime, Date auditTime,
			String lastChargeId, String chargeDetail, BigDecimal commonDiKou, BigDecimal specialDiKou, String projectId,
			String projectName, String operator, Date time, BigDecimal payedAmount, BigDecimal assignAmount,
			BigDecimal currenctArreas, BigDecimal payableAmount, String operationDetailId,
			AcChargeDetailBusinessTypeEnum chargeDetailBusinessEnum,BigDecimal currentPayment) {
		AcCurrentChargeDetail currentChargeDetail = new AcCurrentChargeDetail();
		currentChargeDetail.setId(logStream);
		currentChargeDetail.setHouseCodeNew(houseCodeNew);
		currentChargeDetail.setChargeAmount(chargeAmount);
		currentChargeDetail.setAccountId(currentChargeId);
		currentChargeDetail.setAccountType((short) chargingType.getCode());
		currentChargeDetail.setChargeTime(chargeTime);
		currentChargeDetail.setAuditTime(auditTime);
		currentChargeDetail.setLastChargeId(lastChargeId);
		currentChargeDetail.setChargeDetail(chargeDetail);
		currentChargeDetail.setCommonDikou( CommonUtils.isEmpty( commonDiKou ) ? BigDecimal.valueOf(0.0) : commonDiKou );
		currentChargeDetail.setSpecialDikou( CommonUtils.isEmpty( specialDiKou ) ? BigDecimal.valueOf(0.0) : specialDiKou);
		currentChargeDetail.setProjectId(projectId);
		currentChargeDetail.setProjectName(projectName);
		currentChargeDetail.setCreateId(operator);
		currentChargeDetail.setCreateTime(time);
		currentChargeDetail.setPayedAmount( CommonUtils.isEmpty( payedAmount ) ? BigDecimal.valueOf(0.0) : payedAmount);
		currentChargeDetail.setAssignAmount(assignAmount);
		currentChargeDetail.setCurrenctArreas(currenctArreas);
		currentChargeDetail.setUpdateTime(time);
		currentChargeDetail.setPayableAmount(payableAmount);
		currentChargeDetail.setOperaId(operationDetailId);
		currentChargeDetail.setCurrentPayment(currentPayment);
		currentChargeDetail.setBusinessType(chargeDetailBusinessEnum.getCode());
		acCurrentChargeDetailMapper.insert(currentChargeDetail);
	}

	private AcCurrentCharge createCurrentCharge(String acAccountId, String houseCodeNew, BigDecimal chargeAmount,
			ChargingType chargingType, String chargingMonth, Date auditTime, String projectId,
			String projectName, Date time, String operator) {
		String currentChargeId = UUID.randomUUID().toString();
		AcCurrentCharge currentCharge = new AcCurrentCharge();
		currentCharge.setId(currentChargeId);
		currentCharge.setHouseCodeNew(houseCodeNew);
		currentCharge.setCurrentBillFee(new BigDecimal(0));
		currentCharge.setAccountId(acAccountId);
		currentCharge.setAccountType((short) chargingType.getCode());
		currentCharge.setChargingMonth(chargingMonth);
		currentCharge.setAuditTime(auditTime);
		currentCharge.setProjectId(projectId);
		currentCharge.setProjectName(projectName);
		currentCharge.setCreateTime(time);
		currentCharge.setCreateId(operator);
		acCurrentChargeMapper.insert(currentCharge);
		return currentCharge;
	}

	private void createBillDetail(String logStream, String acAccountId, Date createTime, String billMonth,
			int billState, BillDetailDto billDetail, String houseCodeNew, BigDecimal billAmount, String billPayer,
			String billAddress, Date billInvalid, String projectId, String projectName, int payState) {
		AcBillDetail acBillDetail = new AcBillDetail();
		acBillDetail.setAccountId(acAccountId);
		acBillDetail.setId(logStream);
		acBillDetail.setCreateTime(createTime);
		acBillDetail.setBillMonth(billMonth);
		acBillDetail.setBillState(billState);
		acBillDetail.setHouseCodeNew(houseCodeNew);
		acBillDetail.setBillAmount(billAmount);
		acBillDetail.setBillPayer(billPayer);
		acBillDetail.setBillAddress(billAddress);
		acBillDetail.setBillInvalid(billInvalid);
		acBillDetail.setProjectId(projectId);
		acBillDetail.setPayState(payState);
		Snowflake snowflake = new Snowflake(2, 5);
		long billCode = snowflake.nextId();
		billDetail.setBillCode(snowflake.formatId(billCode));
		acBillDetail.setBillDetail(JSON.toJSONString(billDetail));
		acBillDetailMapper.insert(acBillDetail);
	}

	private void createAcLastBillInfo(String logStream, String houseCodeNew, BigDecimal lastBillFee, String acAccountId,
			String projectId, String projectName, String operator, Date time, ChargingType chargingType) {
		AcLastBillFeeInfo acLastBillFeeInfo = new AcLastBillFeeInfo();
		acLastBillFeeInfo.setId(logStream);
		acLastBillFeeInfo.setHouseCodeNew(houseCodeNew);
		acLastBillFeeInfo.setLastBillFee(lastBillFee);
		acLastBillFeeInfo.setAccountId(acAccountId);
		acLastBillFeeInfo.setProjectId(projectId);
		acLastBillFeeInfo.setProjectName(projectName);
		acLastBillFeeInfo.setCreateId(operator);
		acLastBillFeeInfo.setCreateTime(time);
		acLastBillFeeInfo.setModifyId(operator);
		acLastBillFeeInfo.setModifyTime(time);
		acLastBillFeeInfo.setAccountType((short) chargingType.getCode());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int month = calendar.get(Calendar.MONTH);
		acLastBillFeeInfo.setChargingMonth(month);
		acLastBillFeeInfoMapper.insert(acLastBillFeeInfo);
	}

	private void createAcSpecialDetail(String logStream, String specialId, String houseCodeNew, BigDecimal money,
			BusinessType businessTypeEnum, String projectId, String projectName, Date time,
			String operator, String desc, String deductionDetailId, String operateDetailId,
			BigDecimal beforeAmount,BigDecimal afterAmount) {
		AcSpecialDetail acSpecialDetail = new AcSpecialDetail();
		acSpecialDetail.setId(logStream);
		acSpecialDetail.setSpecialId(specialId);
		acSpecialDetail.setHouseCodeNew(houseCodeNew);
		acSpecialDetail.setChangeAmount(money.abs());
		acSpecialDetail.setBusinessType((short) businessTypeEnum.getCode());
		acSpecialDetail.setProjectId(projectId);
		acSpecialDetail.setProjectName(projectName);
		acSpecialDetail.setCreateTime(time);
		acSpecialDetail.setCreateId(operator);
		acSpecialDetail.setDescription(desc);
		acSpecialDetail.setBillDetailId(deductionDetailId);
		acSpecialDetail.setOperaId(operateDetailId);
		acSpecialDetail.setBeforeAmount(beforeAmount);
		acSpecialDetail.setAfterAmount(afterAmount);
		//后面出现了专项账户抵扣changeMoney为0的，前面的代码都做了限制，这里也限制如果是0的流水是无用的，不写
		if(money.abs().doubleValue() > 0) {
			int count = acSpecialDetailMapper.checkInsert(acSpecialDetail);
			if (count == 0) {
				acSpecialDetailMapper.insert(acSpecialDetail);
			} else {
				acSpecialDetailMapper.insertBySelect(acSpecialDetail);
			}
		}
		
	}

	private AcSpecialAccount createAcSpecialAccount(String houseCodeNew, ChargingType chargingType,
			String acAccountId, String projectId, String projectName, String operator, Date time) {
		String acSpecialId = UUID.randomUUID().toString();
		AcSpecialAccount acSpecialAccount = new AcSpecialAccount();
		acSpecialAccount.setId(acSpecialId);
		acSpecialAccount.setSpecialAmount(new BigDecimal(0));
		acSpecialAccount.setAccountType(chargingType.getCode());
		acSpecialAccount.setAccountId(acAccountId);
		acSpecialAccount.setHouseCodeNew(houseCodeNew);
		acSpecialAccount.setProjectId(projectId);
		acSpecialAccount.setProjectName(projectName);
		acSpecialAccount.setCreateId(operator);
		acSpecialAccount.setCreateTime(time);
		acSpecialAccount.setModifyTime(time);
		acSpecialAccountMapper.insert(acSpecialAccount);
		return acSpecialAccount;
	}

	private AcAccount createAcAccount(AcAccount acAccount, String acAccountId, Date time, String houseCodeNew,
			String projectId, String projectName) {
		acAccount = new AcAccount();
		BigDecimal initial = new BigDecimal(0);
		acAccountId = UUID.randomUUID().toString();
		acAccount.setId(acAccountId);
		acAccount.setModifyTime(time);
		acAccount.setTotalAmount(initial);
		acAccount.setCommonDepositAmount(initial);
		acAccount.setCreateId("system");
		acAccount.setCreateTime(time);
		acAccount.setCurrentBillAmount(initial);
		acAccount.setLastArrearsAmount(initial);
		acAccount.setLateFeeAmount(initial);
		acAccount.setHouseCodeNew(houseCodeNew);
		acAccount.setSpecialDepositAmount(initial);
		acAccount.setProjectId(projectId);
		acAccount.setProjectName(projectName);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int month = cal.get(Calendar.MONTH) + 1;
		acAccount.setChargingMonth(String.valueOf(month));
		acAccountMapper.insert(acAccount);
		return acAccount;
	}

	/**
	 * 此方法主要解决老系统，专项账户抵扣和通用账户抵扣之后发生了新账户系统数据的变动 需要做一系列的修改操作 1 修改收费结果明细表 2 修改当月收费总金额表
	 * 3 修改上月欠费总金额表 4 修改资产账户表
	 * 这里作出改变，现在收费结果明细表会在有变动的时候进行更新数据的，之前是一个月一条，现在是在发生抵扣或交费都会生成明细的 2018-06-22
	 * 
	 * @param chargeDetailDto
	 *            包含了变动信息 (其中包含了违约金的扣减，专项账户的扣减或者通用账户的扣减,这一条扣减的操作在这里实现)
	 * @author QHC
	 * @date 2018-06-13
	 */
	@Override
	public MessageMap updateAcCurrentChargeDetail(String companyId, AcChargeDetailDto chargeDetailDto)  {

		String houseCodeNew = chargeDetailDto.getHouseCodeNew();
		if (CommonUtils.isEmpty(houseCodeNew)) {
			return new MessageMap(MessageMap.INFOR_ERROR, "房屋号为空无法进行此操作");
		}
		logger.info("开始对新房屋编号{}进行收费结果明细表的修改操作", houseCodeNew);

		AcAccount acAccount = acAccountMapper.selectByHouseCodeNew(houseCodeNew);
		if (CommonUtils.isEmpty(acAccount)) {
			return new MessageMap(MessageMap.INFOR_ERROR, "未查询到符合条件的资产账户信息");
		}
		// 根据计费时间和状态查询当前的收费结果明细表(获取最新的一条数据) -- 抵扣应该是发生在最新的一条未审核的数据
		AcCurrentChargeDetail chargeDetail = acCurrentChargeDetailMapper.selectNewestChargeDetail(houseCodeNew,
				chargeDetailDto.getChargingType().getCode());   //不适用于通用账户

		// 当月收费总金额
		AcCurrentCharge acCurrentCharge = acCurrentChargeMapper.selectByAccountIdTypeAndHouseCodeNew(acAccount.getId(),
				chargeDetailDto.getChargingType().getCode(), houseCodeNew);

		// 需要做更新的
		AcLastBillFeeInfo lastBillFeeInfo = acLastBillFeeInfoMapper.selectByCodeAccountIdAndType(houseCodeNew,
				acAccount.getId(),(short)chargeDetailDto.getChargingType().getCode());

		if (CommonUtils.isEmpty(lastBillFeeInfo)) {
			return new MessageMap(MessageMap.INFOR_ERROR, "未查询到符合条件的上月收费总额和欠费总额信息");
		}

		// 说明有专项账户抵扣的操作 -- 针对操作 专项账户发生抵扣（不存在专项账户有钱，却又欠费，所以更新最新一条数据就好）
		if ( null != chargeDetailDto.getSpecialDiKou() ) {
			logger.info("开始对新房屋编号{}进行专项账户扣减修改操作", houseCodeNew);
			if (CommonUtils.isEmpty(chargeDetail)) {
				//专项账户抵扣是需要
				return new MessageMap(MessageMap.INFOR_ERROR, "未查询到符合条件的收费明细信息");
			}
			addSpecialDikouToNewAccount(companyId, houseCodeNew,null, chargeDetailDto, chargeDetail, lastBillFeeInfo,
					acAccount, acCurrentCharge);
		}

		// 说明有通用账户抵扣的操作 -- 针对操作 通用账户发生抵扣
		if (CommonUtils.isNotEmpty(chargeDetailDto.getCommonDiKou())
				&& chargeDetailDto.getCommonDiKou().doubleValue() > 0) {
			// 通用账户和专项账户不存在有钱却欠违约金的情况，所以这里不做违约金的操作
			logger.info("开始对新房屋编号{}进行通用账户扣减修改操作", houseCodeNew);
			//通用账户这里当月收费明细的需要重新取值(这个参数其实后面给null也可以，但是因为方法中用到这个参数中的一些值，所以还是查一条出来传参用)
			chargeDetail = acCurrentChargeDetailMapper.selectNewestChargeDetailCommon(houseCodeNew ,chargeDetailDto.getChargingType().getCode());
			addCommonDikouToNewAccount(companyId, houseCodeNew, chargeDetailDto, chargeDetail,lastBillFeeInfo,acAccount);
		}

		return null;

	}

	/**
	 * 单独做专项账户的抵扣工作(专项账户的抵扣是不一定大于0的。这里涉及到审核)
	 * 
	 * @param companyId
	 *            公司id
	 * @param houseCodeNew
	 *            新房屋编码
	 * @param chargeDetailDto
	 *            收费明细详情（包含了）
	 * @param chargeDetail
	 *            收费明细详情 (最新的一条)
	 * @return MessageMap 处理结果信息
	 */
	private MessageMap addSpecialDikouToNewAccount(String companyId, String houseCodeNew,PayChannel payChannel,
			AcChargeDetailDto chargeDetailDto, AcCurrentChargeDetail chargeDetail, AcLastBillFeeInfo lastBillFeeInfo,
			AcAccount acAccount, AcCurrentCharge acCurrentCharge) {

		logger.info("开始对新房屋编号{}进行审核后的专项账户抵扣系列修改操作", houseCodeNew);
		
		BigDecimal specialDikouForUp = new BigDecimal(0);
		
		// 有专项账户抵扣金额
        // 后面做了调整，专项庄户的抵扣已经取消了，没有专项账户了，所以这里修改 2018-11-20
//		if (chargeDetailDto.getSpecialDiKou().doubleValue() > 0) {
//			// 有金额发生了抵扣
//			AcSpecialDetailDto acSpecialDetailDto = chargeDetailDto.getSpecialDetailDto(); // 包含了专项账户抵扣的信息
//			AcLateFeeDto aclateFeeDto = chargeDetailDto.getAcLateFeeDto();
//
//			if (CommonUtils.isNotEmpty(aclateFeeDto.getMoney())) {
//				// 专项账户抵扣了违约金
//				addAcLateFeeAccountDetail(companyId, aclateFeeDto.getProjectId(),
//						aclateFeeDto.getProjectName(), houseCodeNew, aclateFeeDto.getMoney(),
//						aclateFeeDto.getChargingType(), aclateFeeDto.getBusinessType(), aclateFeeDto.getDesc(),
//						aclateFeeDto.getOperateDetailId(), aclateFeeDto.getPrincipal(), "system", 0);
//				// 抵扣了违约金对应新增一条专项账户的抵扣流水
//				addAcSpecialAccountDetail(companyId, aclateFeeDto.getProjectId(), aclateFeeDto.getProjectName(),
//						houseCodeNew, aclateFeeDto.getMoney(), ChargingType.getChargingTypeByCode(lastBillFeeInfo.getAccountType()),
//						BusinessType.DEDUCTIBLE_LATE_FEE,payChannel,
//						"专项账户发生对" + houseCodeNew + "的违约金抵扣操作", null, null, "system");
//
//			}
//
//			if (CommonUtils.isNotEmpty( acSpecialDetailDto.getMoneyPrincipal() )) {
//				BigDecimal specialDikou = acSpecialDetailDto.getMoneyPrincipal();
//				specialDikouForUp = specialDikou;   //用作后面汇总数据
//				// 专项账户抵扣了本金,对本金的抵扣规则是先抵扣之前月份的，一次往上抵扣，这里没抵扣一笔记录一条流水，具体都每条记录的id
//				// 根据新的房屋编号和，账户类型，查询出当前账户的欠费信息集合(按时间正序排列)
//				Map<String, Object> paraMap = new HashMap<>();
//				paraMap.put("houseCodeNew",houseCodeNew);
//				paraMap.put("accountType",chargeDetailDto.getChargingType().getCode());
//				paraMap.put("monthInfo", null);
//				List<AcCurrentChargeDetail> chargeDetailList = acCurrentChargeDetailMapper.getChargeDetailListByHouseCodeForAudit(paraMap);
//				if (CommonUtils.isNotEmpty(chargeDetailList)) {
//					// 循环进行抵扣操作
//					// 对不同计费月份进行抵扣(同一个资产的不同计费月份)
//					for (AcCurrentChargeDetail acCurrentChargeDetail : chargeDetailList) {
//						BigDecimal dikouAmount = new BigDecimal(0);
//						if (specialDikou.doubleValue() <= 0) {
//							continue;
//						}
//						AcSpecialDetail specialDetail = new AcSpecialDetail();
//						BigDecimal currentArrears = acCurrentChargeDetail.getCurrenctArreas();// 本期的总欠费
//						// 3.修改资产账户的汇总信息（这个留在后面一条汇总）
//						if (specialDikou.subtract(currentArrears).doubleValue() > 0) {
//							dikouAmount = currentArrears;
//							acCurrentChargeDetail
//									.setPayedAmount(acCurrentChargeDetail.getPayedAmount().add(currentArrears)); // 本期已付
//							acCurrentChargeDetail.setCurrentPayment( currentArrears );
//							acCurrentChargeDetail.setCurrenctArreas(new BigDecimal(0)); // 欠费为0
//							acCurrentChargeDetail
//									.setSpecialDikou(acCurrentChargeDetail.getSpecialDikou().add(currentArrears)); // 专项抵扣
//							specialDikou = specialDikou.subtract(currentArrears);
//						} else {
//							// 可以抵扣部分欠款
//							dikouAmount = specialDikou;
//							acCurrentChargeDetail.setPayedAmount(acCurrentChargeDetail.getPayedAmount().add(specialDikou)); // 本期已付
//							acCurrentChargeDetail.setCurrentPayment(specialDikou);
//							acCurrentChargeDetail
//									.setCurrenctArreas(acCurrentChargeDetail.getCurrenctArreas().subtract(specialDikou));
//							acCurrentChargeDetail
//									.setSpecialDikou(acCurrentChargeDetail.getSpecialDikou().add(specialDikou)); // 专项抵扣
//							specialDikou = new BigDecimal(0); // 总抵扣金额已经没有了
//						}
//						specialDetail.setBillDetailId(acCurrentChargeDetail.getId()); // 抵扣明细id
//						if (CommonUtils.isEmpty(acCurrentChargeDetail.getAuditTime())) {
//							acCurrentChargeDetail.setAuditTime(new Date()); // 审核时间
//						}
//						// 记录抵扣流水明细
//						if( dikouAmount.doubleValue() > 0 ) {
//							addAcSpecialAccountDetail(companyId, acCurrentChargeDetail.getProjectId(),
//									acCurrentChargeDetail.getProjectName(), houseCodeNew, dikouAmount,
//									chargeDetailDto.getChargingType(),
//									BusinessType.DEDUCTIBLE,payChannel,
//									"专项账户发生对" + houseCodeNew + chargeDetailDto.getChargingType().getDescription() + "的违约金抵扣操作",
//									acCurrentChargeDetail.getId(), null, "system");
//						}
//
//						// 新增一条收费结果明细表记录
//						acCurrentChargeDetail.setId(CommonUtils.getUUID());
//						acCurrentChargeDetail.setCreateTime(new Date());
//						acCurrentChargeDetail.setCreateId("system");
//						acCurrentChargeDetail.setUpdateTime(new Date());
//						acCurrentChargeDetail.setBusinessType( AcChargeDetailBusinessTypeEnum.SPECIAL_DK.getCode() );
//						acCurrentChargeDetailMapper.insert(acCurrentChargeDetail);
//
//					}
//
//				}
//
//			}
//		}
		
		// 不管是否有发生抵扣这里需要更新审核时间
		AcCurrentChargeDetail chargeDetialUp = new AcCurrentChargeDetail();
		chargeDetialUp.setId(chargeDetail.getId());
		chargeDetialUp.setUpdateTime(new Date());
		chargeDetialUp.setAuditTime(new Date());
		chargeDetialUp.setAccountType( chargeDetail.getAccountType() );
		acCurrentChargeDetailMapper.updateByPrimaryKeySelective(chargeDetialUp);
		logger.info("专项账户添加新账户数据成功{}", houseCodeNew);

		// 修改当月计费总金额信息(只应该发生在每月计费后的审核-- 专项账户的抵扣操作)
		AcCurrentCharge currentChargeUp = new AcCurrentCharge();
		currentChargeUp.setId(acCurrentCharge.getId());
		currentChargeUp.setCurrentBillFee(chargeDetail.getChargeAmount());
		currentChargeUp.setAuditTime(new Date());
		currentChargeUp.setChargingMonth( CommonUtils.getDateStr(new Date(), "yyyy-MM") );
		acCurrentChargeMapper.updateByPrimaryKeySelective(currentChargeUp);

		// 修正上月欠费金额，资产账户的欠费金额
		AcLastBillFeeInfo lastDetailFeeUp = new AcLastBillFeeInfo();
		AcAccount acAccountUp = new AcAccount();
		lastDetailFeeUp.setId(lastBillFeeInfo.getId());
		
		if( null != chargeDetailDto.getSpecialDetailDto().getMoneyPrincipal() ) {
			acAccountUp.setLastArrearsAmount( chargeDetail.getChargeAmount().subtract( specialDikouForUp ) );
			lastDetailFeeUp.setLastBillFee( chargeDetail.getChargeAmount().subtract( specialDikouForUp ) );
		}else {
			acAccountUp.setLastArrearsAmount( chargeDetail.getChargeAmount() );
			lastDetailFeeUp.setLastBillFee( chargeDetail.getChargeAmount() );  //加上本次的
		} 
		
		acLastBillFeeInfoMapper.updateByPrimaryKeyForLastBill(lastDetailFeeUp);

		acAccountUp.setId(acAccount.getId());
//		acAccountUp.setCurrentBillAmount(chargeDetail.getChargeAmount()); // 账单
		acAccountUp.setCurrentChargingAmount(chargeDetail.getChargeAmount()); //当月计费
		
		acAccountMapper.updateByPrimaryKeyForAmount(acAccountUp);
		return new MessageMap(MessageMap.INFOR_SUCCESS, "专项账户添加新账户数据成功");
	}

	/**
	 * 单独做通用账户的抵扣工作(通用账户只有发生了金额抵扣才做操作，不然不处理)
	 * 
	 * @param companyId
	 * @param houseCodeNew
	 * @param chargeDetailDto
	 * @param chargeDetail
	 * @return
	 */
	private MessageMap addCommonDikouToNewAccount(String companyId, String houseCodeNew,
			AcChargeDetailDto chargeDetailDto, AcCurrentChargeDetail chargeDetail,AcLastBillFeeInfo lastBillFeeInfo,AcAccount acAccount)  {

		logger.info("开始对新房屋编号{}进行通用账户抵扣系列修改操作", houseCodeNew);
		BigDecimal commonDiKouForUp = new BigDecimal(0);
		if ( CommonUtils.isNotEmpty( chargeDetailDto.getCommonDiKou() ) ) {
			// 有金额发生了抵扣
			AcCommonAccountDetailDto acCommonAccountDetailDto = chargeDetailDto.getAcCommonAccountDetailDto();
			AcLateFeeDto aclateFeeDto = chargeDetailDto.getAcLateFeeDto();

			//如果发生了抵扣违约金，那不一定抵扣了本金。同理如果发生了抵扣本金，那一样不一定抵扣了违约金（如果存在违约金那是一定会抵扣违约金的）
			// 先抵扣违约金(这里不发生本金的变动)
			if (CommonUtils.isNotEmpty(aclateFeeDto) && CommonUtils.isNotEmpty( aclateFeeDto.getMoney() ) ) {
				// 专项账户抵扣了违约金
				addAcLateFeeAccountDetail(companyId, aclateFeeDto.getProjectId(),
						aclateFeeDto.getProjectName(), houseCodeNew, aclateFeeDto.getMoney(),
						aclateFeeDto.getChargingType(), aclateFeeDto.getBusinessType(), aclateFeeDto.getDesc(),
						aclateFeeDto.getOperateDetailId(), aclateFeeDto.getPrincipal(), "system", 0);
				// 抵扣了违约金对应新增一条专项账户的抵扣流水
				addAcCommonAccountDetail(companyId, aclateFeeDto.getProjectId(), aclateFeeDto.getProjectName(),
						houseCodeNew, aclateFeeDto.getMoney(),
						BusinessType.getBusinessTypeByCode(aclateFeeDto.getBusinessType()),
						ChargingType.LATE_FEE, null,null,
						"通用账户对" + ChargingType.getChargingTypeByCode(chargeDetail.getAccountType()).getDescription()+ "抵扣"
						, null, "system");


			}

			// 如果设置了通用账户的抵扣，那肯定是发生了通用账户的抵扣
			if (  CommonUtils.isNotEmpty( acCommonAccountDetailDto.getMoney() ) ) {
				// 专项账户抵扣了本金,对本金的抵扣规则是先抵扣之前月份的，一次往上抵扣，这里没抵扣一笔记录一条流水，具体都每条记录的id
				// 根据新的房屋编号和，账户类型，查询出当前账户的欠费信息集合(按时间正序排列)
				Map<String, Object> paraMap = new HashMap<>();
				paraMap.put("houseCodeNew", houseCodeNew);
				paraMap.put("accountType", chargeDetailDto.getChargingType().getCode());
				paraMap.put("monthInfo", null);

				List<AcCurrentChargeDetail> chargeDetailList = acCurrentChargeDetailMapper.getChargeDetailListByHouseCode(paraMap);
				// 循环进行抵扣操作
				if (CommonUtils.isNotEmpty(chargeDetailList)) {
					BigDecimal commonDikou = acCommonAccountDetailDto.getMoney();// 抵扣欠费的总金额
					commonDiKouForUp = commonDikou;
					for (AcCurrentChargeDetail acCurrentChargeDetail : chargeDetailList) {
						BigDecimal dikouAmount = new BigDecimal(0);
						if (commonDikou.doubleValue() <= 0)
							continue;

						BigDecimal currentArrears = acCurrentChargeDetail.getCurrenctArreas();// 本期的总欠费
						// 3.修改资产账户的汇总信息（这个留在后面一条汇总）
						if (commonDikou.subtract(currentArrears).doubleValue() > 0) {
							dikouAmount = currentArrears;
							acCurrentChargeDetail
									.setPayedAmount(acCurrentChargeDetail.getPayedAmount().add(currentArrears)); // 本期已付
							acCurrentChargeDetail.setCurrenctArreas(new BigDecimal(0)); // 欠费为0
							acCurrentChargeDetail.setCurrentPayment( currentArrears );
							acCurrentChargeDetail
									.setCommonDikou(acCurrentChargeDetail.getCommonDikou().add(currentArrears)); // 专项抵扣
							commonDikou = commonDikou.subtract(currentArrears);
						} else {
							// 可以抵扣部分欠款
							dikouAmount = commonDikou;
							acCurrentChargeDetail.setPayedAmount(acCurrentChargeDetail.getPayedAmount().add(commonDikou)); // 本期已付
							acCurrentChargeDetail.setCurrentPayment(commonDikou);
							acCurrentChargeDetail
									.setCurrenctArreas(acCurrentChargeDetail.getCurrenctArreas().subtract(commonDikou));
							acCurrentChargeDetail.setCommonDikou(acCurrentChargeDetail.getCommonDikou().add(commonDikou)); // 专项抵扣
							commonDikou = new BigDecimal(0); // 总抵扣金额已经没有了
						}
						acCurrentChargeDetail.setUpdateTime(new Date());

						// 记录抵扣流水明细
						addAcCommonAccountDetail(companyId, chargeDetailDto.getProjectId(), chargeDetailDto.getProjectName(),
								houseCodeNew, dikouAmount, acCommonAccountDetailDto.getBusinessTypeEnum(),
								ChargingType.getChargingTypeByCode(chargeDetail.getAccountType()),null,
								acCurrentChargeDetail.getId(),
								"通用账户对" + ChargingType.getChargingTypeByCode(chargeDetail.getAccountType()).getDescription()
										+ "抵扣",
								null, "system");

						// 新增一条收费结果明细表记录
						acCurrentChargeDetail.setId(CommonUtils.getUUID());
						acCurrentChargeDetail.setCreateTime(new Date());
						acCurrentChargeDetail.setCreateId("system");
						acCurrentChargeDetail.setUpdateTime(new Date());
						acCurrentChargeDetail.setBusinessType( AcChargeDetailBusinessTypeEnum.COMMON_DK.getCode() );
						acCurrentChargeDetailMapper.insert(acCurrentChargeDetail);

					}
				}
				
				
				
				// 修正上月欠费金额，资产账户的欠费金额（只有发生了抵扣才进行执行）
				AcLastBillFeeInfo lastDetailFeeUp = new AcLastBillFeeInfo();
				AcAccount acAccountUp = new AcAccount();
				lastDetailFeeUp.setId(lastBillFeeInfo.getId());
				
				acAccountUp.setLastArrearsAmount( BigDecimal.valueOf(0).subtract(commonDiKouForUp)   );
				lastDetailFeeUp.setLastBillFee( BigDecimal.valueOf(0).subtract(commonDiKouForUp) );
				
				acAccountUp.setId(acAccount.getId());
//				acAccountUp.setCurrentBillAmount(chargeDetail.getChargeAmount()); // 当月计费总金额--去掉，跟计费总金额没关系
//				acAccountUp.setCurrentChargingAmount(chargeDetail.getChargeAmount());//这个也没关系
				
				acLastBillFeeInfoMapper.updateByPrimaryKeyForLastBill(lastDetailFeeUp);
				
				acAccountMapper.updateByPrimaryKeyForAmount(acAccountUp);
				
			}
		}
		logger.info("通用账户添加新账户数据成功{}", houseCodeNew);
		return new MessageMap(MessageMap.INFOR_SUCCESS, "通用账户添加新账户数据成功");
	}

	/**
	 * 前台做了批量操作的情况下，对新版账户的数据同步操作 说明：充值可以同时针对多个房屋的多个账户类型（物业，本体，水电等）
	 * 还可以指定月份信息，如果没有指定月份信息则按照最早的欠费开始扣减 这里针对老账户的话月份信息传递空值
	 * 根据不同的账户类型进行扣减操作(通用账户的充值放在最后，全部数据处理完之后操作)
	 * 如果是多房同时操作，同一个房同月分要交齐物业，本体，水费，电费。暂不支持多房操作，
     * 然后又只交其中一个房的物业，其他房也欠费但是不交。扣减无法做到，扣减会按照月份逐一扣减
	 * 
	 * @param companyId
	 *            公司id
	 * @param houseCodeNews
	 *            涉及到修改的房号信息
	 * @param amountMap
	 *            各种账户类型的金额汇总
	 * @param monthInfo
	 *            指定的月份信息
	 * @param operateId
	 *            操作人id
	 * @return MessageMap 执行结果
	 */
	@Override
	public MessageMap batchRechargeToNewAccount(String companyId, String operateId, List<String> houseCodeNews,
			Map<String, String> amountMap, List<String> monthInfo,String batchNo,int payType)  {

		// 根据账户类型进行充值
		if (CommonUtils.isNotEmpty(amountMap.get("wyAmount")) && Double.valueOf(amountMap.get("wyAmount")) > 0) {
			rechargeToNewAccountByType(companyId, operateId, Double.valueOf(amountMap.get("wyAmount")),
					ChargingType.WY, houseCodeNews, monthInfo,batchNo);
		}
		if (CommonUtils.isNotEmpty(amountMap.get("btAmount")) && Double.valueOf(amountMap.get("btAmount")) > 0) {
			rechargeToNewAccountByType(companyId, operateId, Double.valueOf(amountMap.get("btAmount")),
					ChargingType.BT, houseCodeNews, monthInfo,batchNo);
		}
		if (CommonUtils.isNotEmpty(amountMap.get("waterAmount")) && Double.valueOf(amountMap.get("waterAmount")) > 0) {
			rechargeToNewAccountByType(companyId, operateId, Double.valueOf(amountMap.get("waterAmount")),
					ChargingType.WATER, houseCodeNews, monthInfo,batchNo);
		}
		if (CommonUtils.isNotEmpty(amountMap.get("electAmount")) && Double.valueOf(amountMap.get("electAmount")) > 0) {
			rechargeToNewAccountByType(companyId, operateId, Double.valueOf(amountMap.get("electAmount")),
					ChargingType.ELECT, houseCodeNews, monthInfo,batchNo);
		}
		if (CommonUtils.isNotEmpty(amountMap.get("commonAmount"))
				&& Double.valueOf(amountMap.get("commonAmount")) > 0) {
			// 缴费之后对通用账户进行充值操作
			AcAccount acAccount = acAccountMapper.selectByHouseCodeNew(houseCodeNews.get(0)); // 通用账户充值充在第一个房号下
			addAcCommonAccountDetail(companyId, acAccount.getProjectId(), acAccount.getProjectName(),
					houseCodeNews.get(0), new BigDecimal(amountMap.get("commonAmount")), BusinessType.PRESTORE,
					null, PayChannel.getPayChannalByCode(payType), null, "通用账户充值", batchNo, operateId);
		}

		return new MessageMap(MessageMap.INFOR_SUCCESS, "充值操作成功！");
	}

	/**
	 * 按照类型执行 batchRechargeToNewAccount 的具体交费操作
	 * 		流程有一些变化 之前是存在专项账户有余额的，现在不存在了，只有通用账户有余额，也就是专项账户的都要去掉
	 * 
	 * @param operateId
	 * @param houseCodeNews
	 * @param monthInfo
	 * @param totalAmount
	 *            交费总金额(区分账户类型)
	 * @return
	 */
	public MessageMap rechargeToNewAccountByType(String companyId, String operateId, double totalAmount,
			ChargingType chargingType, List<String> houseCodeNews, List<String> monthInfo,String batchNo)  {

		// 根据房依次单个操作
		for (int i = 0; i < houseCodeNews.size(); i++) {
			String houseCodeNew = houseCodeNews.get(i);
			// 还是一样要先从本类型账户的违约金开始抵扣
			if (totalAmount <= 0) {
				continue;
			}
			AcAccount acAccount = acAccountMapper.selectByHouseCodeNew(houseCodeNew);
			if (CommonUtils.isEmpty(acAccount)) {
				continue;
			}
			AcDelayAccount acDelayAccount = acDelayAccountMapper.selectByAcAccountIdAndType(acAccount.getId(),
					chargingType.getCode());

			// 当前账户存在欠费违约金的情况，先抵扣
			if (CommonUtils.isNotEmpty(acDelayAccount) && acDelayAccount.getAmount().doubleValue() > 0) {

				BigDecimal lateFeeAmount = new BigDecimal(0);
				if (totalAmount > acDelayAccount.getAmount().doubleValue()) {
					lateFeeAmount = acDelayAccount.getAmount(); // 全部抵扣
				} else {
					lateFeeAmount = BigDecimal.valueOf(totalAmount); // 部分抵扣
				}

				// 增加一条违约金交费流水（这里包含了对违约金总账户和资产账户的违约金汇总）
				addAcLateFeeAccountDetail(companyId, acAccount.getProjectId(),
						acAccount.getProjectName(), houseCodeNew, lateFeeAmount,
						ChargingType.getChargingTypeByCode(chargingType.getCode()),
						LatefeeBusinessTypeEnum.PAY_LATE_FEE.getCode(), LatefeeBusinessTypeEnum.PAY_LATE_FEE.getDesc(),
						batchNo, new BigDecimal(0), operateId, 1);

				totalAmount -= lateFeeAmount.doubleValue();
			}

			if (totalAmount <= 0) {
				continue;
			}
			Map<String, Object> paraMap = new HashMap<>();
			paraMap.put("houseCodeNew", houseCodeNew);
			paraMap.put("accountType", chargingType.getCode());
			paraMap.put("monthInfo", monthInfo);

			List<AcCurrentChargeDetail> chargeDetailList = acCurrentChargeDetailMapper
					.getChargeDetailListByHouseCode(paraMap);
			// 历史欠费信息要进行更新
			AcLastBillFeeInfo acLastBillFeeInfo = acLastBillFeeInfoMapper.selectByCodeAccountIdAndType(houseCodeNew,
					acAccount.getId(), (short) chargingType.getCode());
			if ( CommonUtils.isEmpty(acLastBillFeeInfo) || acLastBillFeeInfo.getLastBillFee().doubleValue() <= 0 ) {


//				// 抵扣完最后一个房屋的数据，此时如果还有钱 -- 充值到最后一个房号的专项账户余额中
//				if (i == houseCodeNews.size() - 1 && totalAmount > 0) {
//					addAcSpecialAccountDetail(companyId, acAccount.getProjectId(), acAccount.getProjectName(), houseCodeNew,
//							BigDecimal.valueOf(totalAmount),
//							chargingType,
//							BusinessType.PRESTORE,PayChannel.CASH, "前台收费" + houseCodeNew + "充值到专用账户",
//							null, batchNo, operateId);
//				}
				continue; // 不欠费自然不可能欠违约金，说明这个房已经交清
			}
			
			double dikouAmount = totalAmount; //用于记录总的抵扣金额汇总到资产账户信息中
			// 按照欠费时间依次扣减总的费用
			for (AcCurrentChargeDetail acCurrentChargeDetail : chargeDetailList) {

				if (totalAmount <= 0) {
					continue;
				}
				double principalAmount = 0.0; // 本金
				if (totalAmount > acCurrentChargeDetail.getCurrenctArreas().doubleValue()) {
					principalAmount = acCurrentChargeDetail.getCurrenctArreas().doubleValue();
				}else {
					principalAmount = totalAmount;
				}
				acCurrentChargeDetail
						.setPayedAmount(acCurrentChargeDetail.getPayedAmount().add( BigDecimal.valueOf(principalAmount)) );
				acCurrentChargeDetail.setCurrentPayment( BigDecimal.valueOf(principalAmount) );
				acCurrentChargeDetail.setCurrenctArreas(
						acCurrentChargeDetail.getCurrenctArreas().subtract( BigDecimal.valueOf(principalAmount)) );
				acCurrentChargeDetail.setUpdateTime(new Date());
				acCurrentChargeDetail.setCreateTime(new Date());
				acCurrentChargeDetail.setBusinessType(AcChargeDetailBusinessTypeEnum.PAY.getCode());
				acLastBillFeeInfo.setLastBillFee( acLastBillFeeInfo.getLastBillFee().subtract( BigDecimal.valueOf(principalAmount)) );
				acAccount.setLastArrearsAmount( acAccount.getLastArrearsAmount().subtract( BigDecimal.valueOf(principalAmount) ) );
				totalAmount -= principalAmount;

				// 新增一条收费结果明细表记录
				String logStream = CommonUtils.getUUID();
				acCurrentChargeDetail.setId(logStream);
				acCurrentChargeDetail.setCreateTime(new Date());
				acCurrentChargeDetail.setCreateId(operateId);
				acCurrentChargeDetail.setUpdateTime(new Date());
				acCurrentChargeDetail.setBusinessType( AcChargeDetailBusinessTypeEnum.PAY.getCode() );
				acCurrentChargeDetail.setOperaId(batchNo);
				acCurrentChargeDetailMapper.insert(acCurrentChargeDetail);

				//汇总到项目账户
				projectAccountService.changeCycleAccount(companyId,acAccount.getProjectId(),chargingType,BusinessType.PAYMENT,AccountType.SPECIAL,PayChannel.CASH, BigDecimal.valueOf(principalAmount),houseCodeNew,batchNo,logStream,operateId);

			}
			// 单个房屋执行完成（进行历史总欠费的汇总修改）
            acLastBillFeeInfo.setModifyTime(new Date());
			acLastBillFeeInfoMapper.updateByPrimaryKeySelective(acLastBillFeeInfo);
			acAccountMapper.updateByPrimaryKeySelective(acAccount);
			// 抵扣完最后一个房屋的数据，此时如果还有钱 -- 充值到最后一个房号的专项账户余额中
			if (i == houseCodeNews.size() - 1 && totalAmount > 0) {
				//后面又突然取消了专项账户的使用，这里充值如果有多的怎么办？本想不会，但是有一种特殊的：托收，所以这里还是要放入通用账户
                addAcCommonAccountDetail(companyId, acAccount.getProjectId(), acAccount.getProjectName(),
                        houseCodeNews.get(0), BigDecimal.valueOf(totalAmount), BusinessType.PRESTORE,
                        null, PayChannel.CASH, null, "通用账户充值", batchNo, operateId);
			}
		}
		return new MessageMap(MessageMap.INFOR_SUCCESS,
				houseCodeNews.toString() + ":" + chargingType.getCode() + "充值操作成功！");
	}

	public boolean insetDataToBill(String companyId,String projectId,List<String> dataList) {
		
		if (CommonUtils.isEmpty(dataList)) {
			logger.info("为项目{}生成账单数据，没有找到可用的房号，生成数据结束", projectId);
			return false;
		}
		logger.info(" 开始  》 》 为项目{}生成账单数据", projectId);
		List<BillDetailDto> insertData = new ArrayList<>();
		// 单个房屋进行物业，本体，水费，电费的账单数据生成
		for (String houseCodeNew : dataList) {
			try {
				Map<String, Object> map = acCurrentChargeDetailMapper.getBillInfoByHouseCodeNew(houseCodeNew);
				// 每一条都是一条账单信息
				if (CommonUtils.isEmpty(map)) {
					continue;
				}
				String createTime = map.get("createTime").toString();
				String chargeMonth = map.get("chargeMonth").toString();
				BigDecimal billAmount = (BigDecimal) map.get("chargeTotal");
				String billAddress = map.get("billAddress").toString();
				String projectName = map.get("projectName").toString();
				String buildingArea = map.get("buildingArea").toString();
				String houseCode = map.get("houseCodeNew").toString();
				BillDetailDto detailDto = new BillDetailDto();
				String wyDetail = CommonUtils.isEmpty(map.get("wyDetail")) ? "" : map.get("wyDetail").toString();
				String btDetail = CommonUtils.isEmpty(map.get("btDetail")) ? "" : map.get("btDetail").toString();
				String waterDetail = CommonUtils.isEmpty(map.get("waterDetail")) ? "" : map.get("waterDetail").toString();
				String electDetail = CommonUtils.isEmpty(map.get("electDetail")) ? "" : map.get("electDetail").toString();

				//得到当月的计费金额，并且做修改
				if( CommonUtils.isNotEmpty(wyDetail) ) {
					BillDto wyBill = JSON.parseObject(wyDetail, BillDto.class);
					//老账户计费写入违约金信息的时候把违约金全部算给了物业管理费下，没看出代码错在哪里，懒得看了，在这里拿新账户的，后面新版计费一样要拿新账户，所以不改老账户的了
					//根据新房号和房屋类型   
					double wylateFee = acDelayAccountMapper.selectByHouseCodeNewAndType(houseCodeNew, 1);
					wyBill.setLateFee(CommonUtils.null2String(wylateFee));
					detailDto.setWy(wyBill);
				}
				if( CommonUtils.isNotEmpty( btDetail ) ) {
					BillDto btBill = JSON.parseObject(btDetail, BillDto.class);
					//老账户计费写入违约金信息的时候把违约金全部算给了物业管理费下，没看出代码错在哪里，懒得看了，在这里拿新账户的，后面新版计费一样要拿新账户，所以不改老账户的了
					//根据新房号和房屋类型   
					double btlateFee = acDelayAccountMapper.selectByHouseCodeNewAndType(houseCodeNew, 2);
					btBill.setLateFee(CommonUtils.null2String(btlateFee));
					detailDto.setBt(btBill);
				}
				if( CommonUtils.isNotEmpty( waterDetail ) ) {
					BillDto waterBill = JSON.parseObject(waterDetail, BillDto.class);
					//老账户计费写入违约金信息的时候把违约金全部算给了物业管理费下，没看出代码错在哪里，懒得看了，在这里拿新账户的，后面新版计费一样要拿新账户，所以不改老账户的了
					//根据新房号和房屋类型   
					double waterlateFee = acDelayAccountMapper.selectByHouseCodeNewAndType(houseCodeNew, 3);
					waterBill.setLateFee(CommonUtils.null2String(waterlateFee));
					detailDto.setWater(waterBill);
				}
				if( CommonUtils.isNotEmpty( electDetail ) ) {
					BillDto electBill = JSON.parseObject(electDetail, BillDto.class);
					//老账户计费写入违约金信息的时候把违约金全部算给了物业管理费下，没看出代码错在哪里，懒得看了，在这里拿新账户的，后面新版计费一样要拿新账户，所以不改老账户的了
					//根据新房号和房屋类型   
					double electlateFee = acDelayAccountMapper.selectByHouseCodeNewAndType(houseCodeNew, 4);
					electBill.setLateFee(CommonUtils.null2String(electlateFee));
					detailDto.setElect(electBill);
				}

				//放入总的订单数据需要的信息
				detailDto.setCurrentTotalBill(billAmount.toString());
				detailDto.setHouseCode(houseCode);
				detailDto.setBillGenTime( CommonUtils.getDateStr(new Date(), "yyyy-MM-dd") );
				detailDto.setFullName(billAddress);
				detailDto.setArea(buildingArea);
				
//				//还差一个查询业主的姓名  合版删除
//				//修改本月计费信息
//				String custName = personCustCertMapper.getCustNameByHouseCode(houseCode);
//				if( CommonUtils.isEmpty( custName ) ) {
//					logger.info("房屋{}对应的业主是空，无法生成账单数据",houseCode);
//					continue;
//				}
//				detailDto.setCustName(custName);
//
//				String collectionId = personCustCertMapper.findByBuildingCode(houseCodeNew);
//		        if (null != collectionId) {
//		        	detailDto.setIsCollection("是");
//		        } else {
//		        	detailDto.setIsCollection("否");
//		        }
//
//		        //邮编
//
//		        String zipCode = personCustCertMapper.findByProjectId( projectId );
//		        detailDto.setPostCode(zipCode);
				addBillDetail(companyId, CommonUtils.getDate(createTime), chargeMonth, 0, detailDto,
					houseCodeNew, billAmount, detailDto.getCustName(), billAddress, null, projectId, projectName, 0);

				//修改资产账户中本期账单金额  = 全部欠费 + 违约金
				acAccountMapper.updateCurrentBillByHouseCode(houseCode);
				
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                DataSourceTransactionManager txManager= (DataSourceTransactionManager) ContextLoader.getCurrentWebApplicationContext().getBean("transactionManager");
                TransactionStatus status=txManager.getTransaction(def);
                try {
                    txManager.commit(status);
                } catch (Exception e) {
                    e.printStackTrace();
                    txManager.rollback(status);
                }
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("单个处理账单数据时{}出现异常，这里不做处理", houseCodeNew);
			}

		}
		logger.info("本批次执行完成！");
		return true;
	}
	
	
	@Override
	public boolean sumFictitiousForNewAccount(String companyId, String houseCodeNew, int accountType,
			BigDecimal totalAmount) {
		// 根据房屋编号，账户类型，查询最新一条计费数据
		AcCurrentChargeDetail detail = acCurrentChargeDetailMapper.getAcCurrentChargeDetail(houseCodeNew, accountType);
		if (CommonUtils.isEmpty(detail)) {
			logger.info("汇总虚拟表账单数据时未找到可用数据{}", houseCodeNew);
			return false;
		}
		AcCurrentChargeDetail upDetail = new AcCurrentChargeDetail();
		upDetail.setId(detail.getId());
		upDetail.setChargeAmount(totalAmount);
		upDetail.setPayableAmount(totalAmount);
		upDetail.setCurrenctArreas(totalAmount);
		acCurrentChargeDetailMapper.updateByPrimaryKeySelective(upDetail);
		logger.info("汇总虚拟表账单数成功{}", houseCodeNew);
		return true;
	}

	/**
	 * 处理收银员在前台做了退款操作之后需要进行的操作
	 */
	@Override
	public boolean depositAccountRefund(String companyId, Map<String, String> amountMap, String houseCodeNew,
			String projectId, String projectName, String oprDetailId, String oprId)  {
		
		//获取出每种类型的减免金额
		if( CommonUtils.isEmpty( amountMap )) {
			logger.info("对房屋编号{}进行减免操作，减免金额信息为空，减免结束");
			return false;
		}
		if( CommonUtils.isNotEmpty( amountMap.get("wyAmount") ) && CommonUtils.null2Double( amountMap.get("wyAmount") ) > 0 ) {
			//说明有存在物业管理费的退款
			BigDecimal wyAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("wyAmount") ) ) ;
			addAcSpecialAccountDetail(companyId, projectId, projectName, houseCodeNew, 
					wyAmount, ChargingType.WY, BusinessType.REFUND,null,
					"前台操作了物业管理费专项账户的退款", null, oprDetailId, oprId);
		}
		if( CommonUtils.isNotEmpty( amountMap.get("btAmount") ) && CommonUtils.null2Double( amountMap.get("btAmount") ) > 0 ) {
			//说明有存在本体的退款
			BigDecimal btAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("btAmount") ) ) ;
			addAcSpecialAccountDetail(companyId, projectId, projectName, houseCodeNew, 
					btAmount, ChargingType.BT, BusinessType.REFUND,null,
					"前台操作了本体基金专项账户的退款", null, oprDetailId, oprId);
		}
		if( CommonUtils.isNotEmpty( amountMap.get("waterAmount") ) && CommonUtils.null2Double( amountMap.get("waterAmount") ) > 0 ) {
			//说明有存在水费的退款
			BigDecimal waterAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("waterAmount") ) ) ;
			addAcSpecialAccountDetail(companyId, projectId, projectName, houseCodeNew, 
					waterAmount, ChargingType.WATER, BusinessType.REFUND,null,
					"前台操作了水费专项账户的退款", null, oprDetailId, oprId);
		}
		if( CommonUtils.isNotEmpty( amountMap.get("electAmount") ) && CommonUtils.null2Double( amountMap.get("electAmount") ) > 0 ) {
			//说明有存在电费的退款
			BigDecimal electAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("electAmount") ) ) ;
			addAcSpecialAccountDetail(companyId, projectId, projectName, houseCodeNew, 
					electAmount, ChargingType.ELECT, BusinessType.REFUND,null,
					"前台操作了电费专项账户的退款", null, oprDetailId, oprId);
		}
		if( CommonUtils.isNotEmpty( amountMap.get("commonAmount") ) && CommonUtils.null2Double( amountMap.get("commonAmount") ) > 0 ) {
			//说明有存在电费的退款
			BigDecimal commonAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("commonAmount") ) ) ;
			addAcCommonAccountDetail(companyId, projectId, projectName, houseCodeNew, 
					commonAmount, BusinessType.REFUND, null, null,null, "前台操作通用账户的退款", oprDetailId, oprId);
		}
		
		return true;
	}

	@Override
	public List<Map> queryArrearsByHouseCodeNews(String companyId, List<String> houseCodeNews,String projectId) {
		return acAccountMapper.queryArrearsByHouseCodeNews(houseCodeNews,projectId);
	}

	@Override
	public Map queryArrearsByhouseCode(String companyId,String houseCode,String projectId) {
		return acAccountMapper.queryArrearsByhouseCode(houseCode,projectId);
	}

	@Override
	public List<Map> queryArrearsByBuildingName(String companyId, String buildingName, String projectId) {
		return acAccountMapper.queryArrearsByBuildingName(buildingName,projectId);
	}

	@Override
	public BuildingAndCustInfoDto queryBuildingDetailsByhouseCode(String companyId, String projectId, String houseCode) {
		return acAccountMapper.queryBuildingDetailsByhouseCode(projectId,houseCode);
	}

	@Override
	public List<Map<String, String>> queryCustByBuildId(String companyId, String projectId, String buildId) {
		return acAccountMapper.queryCustByBuildId(projectId,buildId);
	}

	@Override
	public List<BillOfYearDto> queryBillByhouseCodeAndYear(String companyId, String projectId, String year, String houseCode) {
		return acAccountMapper.queryBillByhouseCodeAndYear(projectId,year,houseCode);
	}

	@Override
	public List<PayDetailDto> getCostByHouseCode(String companyId, String houseCodeNew) {
		return acAccountMapper.queryCostByHouseCodeNew(houseCodeNew);

	}


	public boolean addAcLateFeeAccountDetail( String companyId, String projectId,
			   String projectName, String houseCodeNew,
			   BigDecimal money, ChargingType chargingType,
			   int businessType, String desc,
			   String operateDetailId, BigDecimal principal,String operator,int isPay)  {

		logger.info("{}增加资产账户滞纳金流水开始,查询资产账户",houseCodeNew);
		//先通过housecodenew 和 账户类型查询资产账户信息
		String logStream= UUID.randomUUID().toString();
		
		AcAccount acAccount=acAccountMapper.selectByHouseCodeNew(houseCodeNew);
		String acAccountId=null;
		Date time=new Date();
		if(null==acAccount){
			logger.info("{}未查询到资产账户,开始创建资产账户",logStream);
			acAccount = createAcAccount(acAccount,acAccountId,time,houseCodeNew,projectId,projectName);
			logger.info("{}资产账户创建完成.");
		}else {
			logger.info("{}查询到资产账户",logStream);
			acAccountId=acAccount.getId();
		}
		logger.info("{}开始查找资产滞纳金账户",logStream);
		AcDelayAccount acDelayAccount=acDelayAccountMapper.selectByAcAccountIdAndType(acAccountId,chargingType.getCode());
		String acDelayAccountId=null;
		if(null==acDelayAccount){
			logger.info("{}未找到此类型{}的滞纳金账户,开始创建",logStream,chargingType.getCode());
			acDelayAccount=new AcDelayAccount();
			acDelayAccountId=UUID.randomUUID().toString();
			acDelayAccount.setAccountId(acAccount.getId());
			acDelayAccount.setId(acDelayAccountId);
			acDelayAccount.setAmount(new BigDecimal(0));
			acDelayAccount.setProjectId(projectId);
			acDelayAccount.setProjectName(projectName);
			acDelayAccount.setCreateId("system");
			acDelayAccount.setCreateTime(time);
			acDelayAccount.setHouseCodeNew(houseCodeNew);
			acDelayAccount.setModifyTime(time);
			acDelayAccountMapper.insert(acDelayAccount);
			logger.info("{}资产滞纳金账户创建成功!",logStream);
			}else{
		logger.info("{}查询到资产滞纳金账户",logStream);
			acDelayAccountId=acDelayAccount.getId();
		}
		logger.info("{}开始创建资产滞纳金流水",logStream);
		
		//这里的基础数据从数据库拿，不再传递过来
		AcLateFeeBillInfo acLateFeeBillInfo = acLateFeeBillInfoMapper.getAcLateFeeBillInfoByProject(projectId,chargingType.getCode());
		if(CommonUtils.isEmpty( acLateFeeBillInfo )) {
			logger.info("{}项目未找到可用于计算违约金的计费信息，不能计费违约金",houseCodeNew);
			return false;
		}
		
		//算出需要进行计算违约金的本金的日期
		//****违约金的本金相关的操作，每次写入违约金流水需要去查一次新的本金信息S
		int overDays = (int)CommonUtils.null2Double( acLateFeeBillInfo.getOverdueDays() ) ;
		//计算出：如果今天需要添加到计算违约金本金，推算出对应的审核时间
		String rightDate = CommonUtils.getDateStr( CommonUtils.addDay(new Date(), (0-overDays)),"yyyy-MM-dd"); 
		double newestPrincipal = acLateFeeBillInfoMapper.getNewestPrincipalByHouseCode(houseCodeNew, rightDate, chargingType.getCode());
		//****违约金的本金相关的操作，每次写入违约金流水需要去查一次新的本金信息E
		
		
		createAcLateFeeStream(acDelayAccount,logStream,acDelayAccountId,houseCodeNew,sumLateFeeDelayAccount(new BigDecimal(0),businessType,money,1 ),
		businessType,projectId,projectName,time,operator,desc,operateDetailId,BigDecimal.valueOf( newestPrincipal ),acLateFeeBillInfo.getRate(),
		acLateFeeBillInfo.getIsSingleinterest(),acLateFeeBillInfo.getOverdueDays());
		logger.info("{}资产滞纳金流水创建完成开始汇总到滞纳金账户",logStream);
		
		//发生了违约金的明细，这里需要根据业务类型来汇总滞纳金账户信息
		//acDelayAccount.setAmount( sumLateFeeDelayAccount( new BigDecimal(0) ,businessType,money,2 ) );
		acDelayAccount.setAmount( sumLateFeeDelayAccount( acDelayAccount.getAmount() ,businessType,money,2 ) );
		int count=acDelayAccountMapper.updateByPrimaryKey(acDelayAccount);
		if(count==0){
		logger.error("{}汇总到资产滞纳金账户失败",logStream);
			throw new ECPBusinessException("汇总到资产滞纳金账户失败");
		}else {
			logger.info("{}汇总到资产滞纳金账户成功");
			logger.info("{}开始汇总到资产账户");
			acAccount.setLateFeeAmount( sumLateFeeDelayAccount(acAccount.getLateFeeAmount() ,businessType,money,2 ) );
			int updateCount=acAccountMapper.updateByPrimaryKey(acAccount);
		if(updateCount==0){
			logger.error("{}汇总到资产账户失败",logStream);
			throw new ECPBusinessException("汇总到资产账户失败");
		}else {
			logger.info("{}汇总到资产账户成功!",logStream);
			//只有在支付或者抵扣时写项目滞纳金流水
			if(isPay==1) {
				projectAccountService.changeDelayAccount(companyId, projectId, money, chargingType, houseCodeNew, operateDetailId, logStream, operator);
				}
			return true;
			}
		}
	}
	
	/**
	 * 根据业务类型决定如何汇总到滞纳金总账户信息
	 * @param amount  原始账户值
	 * @param businessType  业务类型
	 * @param money 本次流水的变动金额
	 * @param sumType 本次进行聚合的类型（1.插入违约金流水用，  2，汇总违约金欠费用）
	 * @return
	 */
	private BigDecimal sumLateFeeDelayAccount(BigDecimal amount ,int businessType,BigDecimal money,int sumType) {
		if(4 != businessType) {
			money = new BigDecimal(0).subtract(money);
		}
		//抵扣业务虽然可以减少违约金，但是如果小于0是不允许的，都是正数
//		if( 1 == sumType && amount.doubleValue() + money.doubleValue() < 0 ) {
//			throw new ECPBusinessException("违约金的操作金额异常，出现负数");
//		}
		return amount.add(money);
	}
	
	private void createAcLateFeeStream(AcDelayAccount acDelayAccount,String logStream,String acDelayAccountId, String houseCodeNew, BigDecimal money, int businessType, String projectId, String projectName, Date time, String operator, String desc, String operateDetailId, BigDecimal principal, BigDecimal rate, int isSingle, int day) {
        AcLateFeeStream acLateFeeStream=new AcLateFeeStream();
        acLateFeeStream.setId(logStream);
        acLateFeeStream.setDelayAccountId(acDelayAccountId);
        acLateFeeStream.setHouseCodeNew(houseCodeNew);
        acLateFeeStream.setChangeAmount(money.abs());
        acLateFeeStream.setBeforeAmount(new BigDecimal(0));
        acLateFeeStream.setAfterAmount(money);
        acLateFeeStream.setProjectId(projectId);
        acLateFeeStream.setProjectName(projectName);
        acLateFeeStream.setCreateId(operator);
        acLateFeeStream.setCreateTime(time);
        acLateFeeStream.setDescription(desc);
        acLateFeeStream.setOperaId(operateDetailId);
        acLateFeeStream.setPrincipalAccount(principal);
        acLateFeeStream.setRate(rate);
        acLateFeeStream.setIsSingleinterest(isSingle);
        acLateFeeStream.setOverdueDays(day);
        acLateFeeStream.setBusinessType((short)businessType);
        int count=acLateFeeStreamMapper.checkBefore(acLateFeeStream);
        if(count>0) {
            acLateFeeStreamMapper.insertBySelect(acLateFeeStream);
        }else {
            acLateFeeStreamMapper.insert(acLateFeeStream);
        }
    }

    /**
     * app微信小程序，业主直接为物业水电交费，写入数据到新账户
     */
	@Override
	public MessageMap payToNewAccountForWeiXin(String companyId, PayDetailInfoForWeiXin entity) {
		//交费可分为  1，违约金   2 本金 (先处理违约金部分)
		if( CommonUtils.isEmpty( entity )) {
			logger.info("参数为空无法充值");
			return new MessageMap(MessageMap.INFOR_WARNING,"参数为空无法充值");
		}
		if( CommonUtils.isNotEmpty( entity.getWyLateFeeAmount() ) && entity.getWyLateFeeAmount().doubleValue() > 0  ) {
			//为物业交费违约金
			// 增加一条违约金交费流水（这里包含了对违约金总账户和资产账户的违约金汇总）
			payLateFeeFromWeiXin(companyId,ChargingType.WY,entity.getWyLateFeeAmount(),entity.getProjectId(),entity.getProjectName(),
					entity.getOperaId(),entity.getHouseCodeNew());
		}
		if( CommonUtils.isNotEmpty( entity.getBtLateFeeAmount() ) && entity.getBtLateFeeAmount().doubleValue() > 0  ) {
			payLateFeeFromWeiXin(companyId,ChargingType.BT,entity.getBtLateFeeAmount(),entity.getProjectId(),entity.getProjectName(),
					entity.getOperaId(),entity.getHouseCodeNew());
		}
		if( CommonUtils.isNotEmpty( entity.getWaterLateFeeAmount() ) && entity.getWaterLateFeeAmount().doubleValue() > 0 ) {
			payLateFeeFromWeiXin(companyId,ChargingType.WATER,entity.getWaterLateFeeAmount(),entity.getProjectId(),entity.getProjectName(),
					entity.getOperaId(),entity.getHouseCodeNew());
		}
		if( CommonUtils.isNotEmpty( entity.getElectLateFeeAmount() ) && entity.getElectLateFeeAmount().doubleValue() > 0 ) {
			payLateFeeFromWeiXin(companyId,ChargingType.ELECT,entity.getElectLateFeeAmount(),entity.getProjectId(),entity.getProjectName(),
					entity.getOperaId(),entity.getHouseCodeNew());
		}

		//处理完违约金，处理本金
		if( CommonUtils.isNotEmpty( entity.getWyAmount() ) && entity.getWyAmount().doubleValue() > 0  ) {
			payPrincipalFromWeiXin(companyId,ChargingType.WY,entity.getWyAmount(),entity.getProjectId(),entity.getProjectName(),
					entity.getOperaId(),entity.getHouseCodeNew());
		}
		
		if( CommonUtils.isNotEmpty( entity.getBtAmount() ) && entity.getBtAmount().doubleValue() > 0 ) {
			payPrincipalFromWeiXin(companyId,ChargingType.BT,entity.getBtAmount(),entity.getProjectId(),entity.getProjectName(),
					entity.getOperaId(),entity.getHouseCodeNew());
		}
		
		if( CommonUtils.isNotEmpty( entity.getWaterAmount() ) && entity.getWaterAmount().doubleValue() > 0 ) {
			payPrincipalFromWeiXin(companyId,ChargingType.WATER,entity.getWaterAmount(),entity.getProjectId(),entity.getProjectName(),
					entity.getOperaId(),entity.getHouseCodeNew());
		}
		
		if( CommonUtils.isNotEmpty( entity.getElectAmount() ) && entity.getElectAmount().doubleValue() > 0 ) {
			payPrincipalFromWeiXin(companyId,ChargingType.ELECT,entity.getElectAmount(),entity.getProjectId(),entity.getProjectName(),
					entity.getOperaId(),entity.getHouseCodeNew());
		}
		return new MessageMap(MessageMap.INFOR_SUCCESS,"交费成功！");
	}
	
	/**
	 * 微信小程序交费和老账户过来的有些差异，所以方法无法共用，这里单独处理---违约金
	 * @param chargingType 违约金账户类型
	 * @param amount 发生交费的金额
	 * @return
	 */
	public boolean payLateFeeFromWeiXin( String companyId,
										 ChargingType chargingType,
										 BigDecimal amount,
										 String projectId,
										 String projectName,
										 String operaId,
										 String houseCodeNew) {
		//这里主要是操作违约金相关，主要变化的参数是账户类型和金额
		addAcLateFeeAccountDetail(companyId, projectId,
				projectName, houseCodeNew,amount ,
				chargingType,LatefeeBusinessTypeEnum.PAY_LATE_FEE.getCode(), LatefeeBusinessTypeEnum.PAY_LATE_FEE.getDesc(),
				operaId, new BigDecimal(0),OWNER, 1);
		return true;
	}
	
	
	/**
	 * 微信小程序交费和老账户过来的有些差异，所以方法无法共用，这里单独处理---交费本金
	 * @param chargingType 收费项
	 * @param totalAmount 发生交费的金额
	 * @return
	 */
	public boolean payPrincipalFromWeiXin( String companyId,
										   ChargingType chargingType,
										 BigDecimal totalAmount,
										 String projectId,
										 String projectName,
										 String operaId,
										 String houseCodeNew) {
		
		AcAccount acAccount = acAccountMapper.selectByHouseCodeNew(houseCodeNew);
		
		// 历史欠费信息要进行更新
		AcLastBillFeeInfo acLastBillFeeInfo = acLastBillFeeInfoMapper.selectByCodeAccountIdAndType(houseCodeNew,
				acAccount.getId(), (short) chargingType.getCode());
		
		if (CommonUtils.isEmpty(acAccount) || CommonUtils.isEmpty( acLastBillFeeInfo )) {
			return false;
		}
		
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("houseCodeNew", houseCodeNew);
		paraMap.put("accountType", chargingType.getCode());
		paraMap.put("monthInfo", null);//月份暂时用不上
		
		List<AcCurrentChargeDetail> chargeDetailList = acCurrentChargeDetailMapper
				.getChargeDetailListByHouseCode(paraMap);
		
		
		if ( acLastBillFeeInfo.getLastBillFee().doubleValue() <= 0 ) {
			//如果过来没有欠费信息，但是交费肯定有原因，这里充值在专项账户吧(理论这种不存在的)
			addAcSpecialAccountDetail(companyId, acAccount.getProjectId(), acAccount.getProjectName(), houseCodeNew,
					totalAmount,chargingType,
					BusinessType.PRESTORE,PayChannel.SMALL_ROUTINE, "业主通过小程序" + houseCodeNew + "充值到专用账户",
					null, operaId,OWNER);
			return true; // 不欠费自然不可能欠违约金，说明这个房已经交清
		}
		
		// 按照欠费时间依次扣减总的费用
		for (AcCurrentChargeDetail acCurrentChargeDetail : chargeDetailList) {

			if (totalAmount.doubleValue() <= 0) {
				continue;
			}
			double principalAmount = 0.0; // 本金
			if (totalAmount.doubleValue() > acCurrentChargeDetail.getCurrenctArreas().doubleValue()) {
				principalAmount = acCurrentChargeDetail.getCurrenctArreas().doubleValue();
			}else {
				principalAmount = totalAmount.doubleValue();
			}
			acCurrentChargeDetail
					.setPayedAmount(acCurrentChargeDetail.getPayedAmount().add( BigDecimal.valueOf(principalAmount)) );
			acCurrentChargeDetail.setCurrentPayment( BigDecimal.valueOf(principalAmount) );
			acCurrentChargeDetail.setCurrenctArreas(
					acCurrentChargeDetail.getCurrenctArreas().subtract( BigDecimal.valueOf(principalAmount)) );
			acCurrentChargeDetail.setUpdateTime(new Date());
			acCurrentChargeDetail.setCreateTime(new Date());
			acCurrentChargeDetail.setBusinessType(AcChargeDetailBusinessTypeEnum.PAY.getCode());
			acLastBillFeeInfo.setLastBillFee( acLastBillFeeInfo.getLastBillFee().subtract( BigDecimal.valueOf(principalAmount)) );
			acAccount.setLastArrearsAmount( acAccount.getLastArrearsAmount().subtract( BigDecimal.valueOf(principalAmount) ) );
			totalAmount = totalAmount.subtract( BigDecimal.valueOf( principalAmount ) );

			// 新增一条收费结果明细表记录
			String logStream = CommonUtils.getUUID();
			acCurrentChargeDetail.setId(logStream);
			acCurrentChargeDetail.setCreateTime(new Date());
			acCurrentChargeDetail.setCreateId(OWNER);
			acCurrentChargeDetail.setUpdateTime(new Date());
			acCurrentChargeDetail.setBusinessType( AcChargeDetailBusinessTypeEnum.PAY.getCode() );
			acCurrentChargeDetail.setOperaId(operaId);
			acCurrentChargeDetailMapper.insert(acCurrentChargeDetail);

			//汇总到项目账户
			projectAccountService.changeCycleAccount(companyId,acAccount.getProjectId(),chargingType,BusinessType.PAYMENT,AccountType.SPECIAL,PayChannel.SMALL_ROUTINE, BigDecimal.valueOf(principalAmount),houseCodeNew,operaId,logStream,OWNER);

		}
		// 单个房屋执行完成（进行历史总欠费的汇总修改）
		acLastBillFeeInfoMapper.updateByPrimaryKeySelective(acLastBillFeeInfo);
		acAccountMapper.updateByPrimaryKeySelective(acAccount);
		// 抵扣完最后一个房屋的数据，此时如果还有钱 -- 充值到最后一个房号的专项账户余额中
		if( totalAmount.doubleValue() > 0 ) {
			addAcSpecialAccountDetail(companyId, acAccount.getProjectId(), acAccount.getProjectName(), houseCodeNew,
					totalAmount,chargingType,BusinessType.PRESTORE,PayChannel.SMALL_ROUTINE, "业主小程序" + houseCodeNew + "充值到专用账户",
					null, operaId, OWNER);
		}
		
		return true;
		
	}

	@Override
	public List<BuildingAndCustDTO> getBuindingAndCustByMobile(String companyId, String mobile,String projectId) {
		return acAccountMapper.queryByMobile(mobile,projectId);
	}

	/**
	 * 导出报表生产通用抵扣和专项抵扣数据
	 * @param companyId
	 * @param projectId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Override
	public List<Map> accountPayTypeReports(String companyId,String projectId,String startTime,String endTime) {
		double deductionMoney=acCommonAccountDetailMapper.fingDeductionByDetail(BusinessType.DEDUCTIBLE.getCode(),projectId,startTime,endTime);

         double wy =acSpecialDetailMapper.findDeductionByDetail(BusinessType.DEDUCTIBLE.getCode(),ChargingType.WY.getCode(),projectId,startTime,endTime);
		double bt =acSpecialDetailMapper.findDeductionByDetail(BusinessType.DEDUCTIBLE.getCode(),ChargingType.BT.getCode(),projectId,startTime,endTime);
		double water = acSpecialDetailMapper.findDeductionByDetail(BusinessType.DEDUCTIBLE.getCode(),ChargingType.WATER.getCode(),projectId,startTime,endTime);
		double ele =acSpecialDetailMapper.findDeductionByDetail(BusinessType.DEDUCTIBLE.getCode(),ChargingType.ELECT.getCode(),projectId,startTime,endTime);

		Map<String,Double>map =new HashMap<>();
		List<Map> list=new ArrayList<>();
		map.put("commMoney",deductionMoney);
		map.put("wy",wy);
		map.put("bt",bt);
		map.put("water",water);
		map.put("ele",ele);
		list.add(map);

         return list;
	}

	/**
	 * @author QHC
	 * 新账户生成账单数据的
	 * @param companyId 公司id
	 * @param projectId 项目id
	 * @date 2018-08-26
	 */
	@Override
	public MessageMap productBillInfoNew(final String companyId, final String projectId) {
		//处理页面重复的情况
		String isProduct = IS_PRODUCT_BILL_STR + "_"+ projectId; 
		Object isProductObj = springRedisTools.getByKey( isProduct );
		if( CommonUtils.isEmpty( isProductObj ) ) {
			//暂存一天吧
			springRedisTools.addData(isProduct,IS_PRODUCT_BILL_YES,60 * 60 * 24,TimeUnit.SECONDS);
		}else {
			return new MessageMap(MessageMap.INFOR_SUCCESS, "账单数据生成中，请等待。。。");
		}
		// 按照项目，按照房进行汇总（将分散物业，本体，水费，电费的汇总在一起）
		final List<String> houseCodes = acBillDetailMapper.getAllBuildingCodeByProjectId(projectId);
		if (CommonUtils.isEmpty(houseCodes)) {
			logger.info("项目{}不存在可用于创建账单的数据",projectId);
			return new MessageMap(MessageMap.INFOR_SUCCESS, "没找到可用的生成账单数据，请求结束");
		}
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(8,16,4,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                new ThreadPoolExecutor.AbortPolicy());
		
		//这里做一个多线程批量插入账单数据的
		int totalTimes = houseCodes.size()/BATCH_ADD_SIZE+1;   //总批次数
        int remainderAmounts=houseCodes.size()%BATCH_ADD_SIZE;  // 最后一个批次的余数
        boolean isImplement= (remainderAmounts==0);  //最后一个批次是否执行取决于  remainderAmounts 是否为0
		for ( int i=0; i < totalTimes; i++ ) {
			final List<String> insertHouseCodes;
			if( i == totalTimes -1 ) {  //最后一个批次
				if( isImplement ) {
					break;
				}
				insertHouseCodes=houseCodes.subList(i*BATCH_ADD_SIZE,i*BATCH_ADD_SIZE+remainderAmounts);
			}else {
				insertHouseCodes=houseCodes.subList(i*BATCH_ADD_SIZE,(i+1)*BATCH_ADD_SIZE);
			}
			executor.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					DBContextHolder.setDBType(companyId);
					insetDataToBill(companyId,projectId,insertHouseCodes);
					return true;
				}
				
			});
		}
		        
		return new MessageMap(MessageMap.INFOR_SUCCESS, "账单数据生成中，请等待。。。");
	}

}
