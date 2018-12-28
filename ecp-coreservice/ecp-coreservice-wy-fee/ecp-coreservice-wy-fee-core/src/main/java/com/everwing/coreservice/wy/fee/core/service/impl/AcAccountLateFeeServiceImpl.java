package com.everwing.coreservice.wy.fee.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.entity.*;
import com.everwing.coreservice.common.wy.fee.service.AcAccountLateFeeService;
import com.everwing.coreservice.common.wy.fee.service.ProjectAccountService;
import com.everwing.coreservice.wy.fee.dao.mapper.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 资产违约金账户业务实现
 * @author qhc
 * @date 2018-05-28
 */
@Service
@Component
public class AcAccountLateFeeServiceImpl implements AcAccountLateFeeService {

	 private static final Logger logger= LogManager.getLogger(AcAccountServiceImpl.class);
	 
	 @Autowired
	 private AcAccountMapper acAccountMapper;
	 
	 @Autowired
	 private AcDelayAccountMapper acDelayAccountMapper;
	 
	 @Autowired
	 private AcLateFeeStreamMapper acLateFeeStreamMapper;


     @Autowired
     private AcLastBillFeeInfoMapper acLastBillFeeInfoMapper;

     @Autowired
     private AcLateFeeBillInfoMapper acLateFeeBillInfoMapper;

     @Autowired
     private AcCurrentChargeDetailMapper acCurrentChargeDetailMapper;

	 @Autowired
     private ProjectAccountService projectAccountService;
	
	 
	 @Override
     public boolean addAcLateFeeAccountDetail(String companyId, String projectId,
                                              String projectName, String houseCodeNew,
                                              BigDecimal money, ChargingType chargingType,
                                              int businessType, String desc,
                                              String operateDetailId, BigDecimal principal, String operator, int isPay)  {
		 
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

       if( CommonUtils.isEmpty( acLateFeeBillInfo ) )  {
    	   logger.info("没找到对应的计费规则，无法计算违约金信息{}",projectId);
    	   return false;
       }

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
	 * 这里冗余一个插入违约金计费规则的实现
	 * 		违约金计费规则表是按照项目进行记录的，会随着更新都增加数据
	 * 		在计费规则那里如果有项目的计费规则发生改变，会需要调用这里进行数据的更新和插入
	 * @author qhc
	 * @date 2018-05-28
	 * @param entity 计费规则实体 
	 * @return MessageMap 一个包含结果和结果描述的模板
	 */
	 @Override
	 public MessageMap addLateFeeBillInfo(AcLateFeeBillInfo entity) {
		
		if(CommonUtils.isEmpty( entity ) 
				|| CommonUtils.isEmpty( entity.getProjectId() )
				|| CommonUtils.isEmpty( entity.getIsSingleinterest() )
				|| CommonUtils.isEmpty( entity.getRate() )
				|| CommonUtils.isEmpty( entity.getOverdueDays() ) ){
		    return new MessageMap(MessageMap.INFOR_ERROR, "请求参数为空，无法执行插入操作");
		}
		
		acLateFeeBillInfoMapper.insert(entity);
		//TODO 违约金规则变化不知道是不是会导致违约金流水中违约金的计算方式发生改变
		//违约金如果有发生变化的话，进行新增，以前的违约金规则也作为保留，只是作废数据
		List<AcLateFeeBillInfo> lateFeeBillInfoList = acLateFeeBillInfoMapper.getLateFeeBillInfoByContent(entity);
		if( CommonUtils.isEmpty( lateFeeBillInfoList ) ) {
			//只有查询结果为空说明，规则发生了变动，否则是没有变化的
			acLateFeeBillInfoMapper.updateIsUsedToNo(entity.getProjectId());
			
			entity.setIsUsed(0);//本条设置为正在使用
			entity.setId(CommonUtils.getUUID());
			
			acLateFeeBillInfoMapper.insert(entity);
			logger.info("****{}新增一条违约金计费规则成功****",entity.getProjectId());
			
			return new MessageMap(MessageMap.INFOR_SUCCESS,"新增一条违约金计费规则成功");
		}
		
		return new MessageMap(MessageMap.INFOR_WARNING,"计费规则没有发生改变，不做任何操作");
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
	 
	 
	 private AcAccount createAcAccount(AcAccount acAccount,String acAccountId,Date time,String houseCodeNew,String projectId,String projectName){
        acAccount=new AcAccount();
        BigDecimal initial=new BigDecimal(0);
        acAccountId=UUID.randomUUID().toString();
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
        acAccountMapper.insert(acAccount);
        return acAccount;
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
		if( (4 != businessType) ) {
		    money = new BigDecimal(0).subtract(money);
		}
		//抵扣业务虽然可以减少违约金，但是如果小于0是不允许的，都是正数
//		if( 1 == sumType && amount.doubleValue() + money.doubleValue() < 0 ) {
//			throw new ECPBusinessException("违约金的操作金额异常，出现负数");
//		}
		return amount.add(money);
	}



	/**
	 * 此方法处理收银员操作了减免，为新账户带来的数据插入
	 * 		每种类型的减免都需要进行单独处理
	 */
	@Override
	public void addLateFeeInfoForReduction( String companyId, 
											Map<String, String> amountMap, 
											String houseCodeNew,
											String projectId,
											String projectName,
											String oprDetailId,
											String oprId )  {
		//获取出每种类型的减免金额
		if( CommonUtils.isEmpty( amountMap )) {
			logger.info("对房屋编号{}进行减免操作，减免金额信息为空，减免结束");
			return;
		}
        // 获取所有的欠费账户
        List<AcDelayAccount> list = acDelayAccountMapper.selectByHouseCodeNew(houseCodeNew);
        BigDecimal amount ;

        // 物业减免金额
        BigDecimal wyAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("wyJmAmount") ) ) ;

        // 本体减免金额
        BigDecimal btAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("btJmAmount") ) ) ;

        // 水费减免金额
        BigDecimal waterAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("waterJmAmount") ) ) ;

        // 电费减免金额
        BigDecimal electAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("electJmAmount") ) ) ;

        // 循环所有账户，当减免金额大于欠费中的账户金额时，把相应的差值增加到滞纳金中，然后账户余额减少相应的余额，保证进行本金减免时，新老账户数据保持一致
		for(AcDelayAccount acDelayAccount : list){
		    if(acDelayAccount.getAccountType()==1 && wyAmount.doubleValue()>0.00 && wyAmount.doubleValue()>acDelayAccount.getAmount().doubleValue() ){
                amount = acDelayAccount.getAmount().abs();
                Integer type=1;
                String id =  acDelayAccount.getId();

                //减免金额和账户余额的差值，后面要用
                BigDecimal diffrentAmount =  wyAmount.subtract(amount).abs();

                //进行滞纳金金额的增加
                addData(houseCodeNew,diffrentAmount, projectId,projectName, oprDetailId, oprId,id,amount,type);



            } else if (acDelayAccount.getAccountType()==2 && btAmount.doubleValue()>0.00 && btAmount.doubleValue()>acDelayAccount.getAmount().doubleValue()){
                amount = acDelayAccount.getAmount().abs();
                String id =  acDelayAccount.getId();
                Integer type=2;

                BigDecimal diffrentAmount =  btAmount.subtract(amount).abs();

                addData(houseCodeNew,diffrentAmount, projectId,projectName, oprDetailId, oprId,id,amount,type);

            } else if(acDelayAccount.getAccountType()==3 && waterAmount.doubleValue()>0.00 && waterAmount.doubleValue()> acDelayAccount.getAmount().doubleValue()){
                amount = acDelayAccount.getAmount().abs();
                String id =  acDelayAccount.getId();
                Integer type=3;

                BigDecimal diffrentAmount =  waterAmount.subtract(amount).abs();

                addData(houseCodeNew,diffrentAmount, projectId,projectName, oprDetailId, oprId,id,amount,type);



            }else if(acDelayAccount.getAccountType()==4 && electAmount.doubleValue()>0.00 && electAmount.doubleValue()>acDelayAccount.getAmount().doubleValue()){
                amount = acDelayAccount.getAmount().abs();
                String id =  acDelayAccount.getId();
                Integer type=4;

                BigDecimal diffrentAmount = electAmount.subtract(amount).abs();

                addData(houseCodeNew,diffrentAmount, projectId,projectName, oprDetailId, oprId,id,amount,type);

            }
        }

		if( CommonUtils.isNotEmpty( amountMap.get("wyJmAmount") ) && CommonUtils.null2Double( amountMap.get("wyJmAmount") ) > 0 ) {
			//说明有存在物业违约金的减免
			BigDecimal wyJmAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("wyJmAmount") ) ) ;
			addAcLateFeeAccountDetail(companyId, projectId, projectName, houseCodeNew, wyJmAmount,ChargingType.WY,
					2, "前台对物业管理费违约金进行减免",oprDetailId, new BigDecimal(0), oprId, 0);
		}
		if( CommonUtils.isNotEmpty( amountMap.get("btJmAmount") ) && CommonUtils.null2Double( amountMap.get("btJmAmount") ) > 0 ) {
			//说明有存在本体违约金的减免
			BigDecimal btJmAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("btJmAmount") ) ) ;
			addAcLateFeeAccountDetail(companyId, projectId,projectName, houseCodeNew, btJmAmount,ChargingType.BT,
					2, "前台对本体基金违约金进行减免",oprDetailId, new BigDecimal(0), oprId, 0);
		}
		if( CommonUtils.isNotEmpty( amountMap.get("waterJmAmount") ) && CommonUtils.null2Double( amountMap.get("waterJmAmount") ) > 0 ) {
			//说明有存在水费违约金的减免
			BigDecimal waterJmAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("waterJmAmount") ) ) ;
			addAcLateFeeAccountDetail(companyId, projectId,projectName, houseCodeNew, waterJmAmount,ChargingType.WATER,
					2, "前台对水费违约金进行减免",oprDetailId, new BigDecimal(0), oprId, 0);
		}
		if( CommonUtils.isNotEmpty( amountMap.get("electJmAmount") ) && CommonUtils.null2Double( amountMap.get("electJmAmount") ) > 0 ) {
			//说明有存在电费违约金的减免
			BigDecimal electJmAmount = BigDecimal.valueOf( CommonUtils.null2Double( amountMap.get("electJmAmount") ) ) ;
			addAcLateFeeAccountDetail(companyId,projectId,projectName, houseCodeNew, electJmAmount,ChargingType.ELECT,
					2, "前台对电费违约金进行减免",oprDetailId, new BigDecimal(0), oprId, 0);
		}


	}

    /**
     * 当前台进行本金减免时，为保证新老账户数据一致，这里进行把减免的金额和滞纳金的差值增加到滞纳金中，同时相应的账户总额要减少相应的差值
     * @param houseCodeNew  房号
     * @param diffrentAmount  前台操作减免金额和滞纳金的差值
     * @param projectId  项目id
     * @param projectName 项目名称
     * @param oprDetailId  操作人id
     * @param oprId
     * @param id  欠费账户的id 比如：水，电 等
     * @param amount 欠费账户总额
     * @param type  账户类型  比如：水，电，物业，本体
     */
	public void addData(String houseCodeNew , BigDecimal diffrentAmount,String projectId,
                        String projectName,
                        String oprDetailId,
                        String oprId,
                        String id,
                        BigDecimal amount,
                        Integer type
                        ){


        AcLateFeeBillInfo acLateFeeBillInfo =  acLateFeeBillInfoMapper.getAcLateFeeBillInfoByProject(projectId,type);

        AcLateFeeStream acLateFeeStream = new AcLateFeeStream();
        acLateFeeStream.setId(CommonUtils.getUUID());
        acLateFeeStream.setDelayAccountId(id);
        acLateFeeStream.setHouseCodeNew(houseCodeNew);
        acLateFeeStream.setBeforeAmount(amount);
        acLateFeeStream.setAfterAmount(amount.add(diffrentAmount));
        acLateFeeStream.setChangeAmount(diffrentAmount);
        acLateFeeStream.setBusinessType(BusinessType.DEDUCTIBLE.getCode());
        acLateFeeStream.setProjectId(projectId);
        acLateFeeStream.setProjectName(projectName);
        acLateFeeStream.setCreateTime(new Date());
        acLateFeeStream.setCreateId(oprDetailId);
        acLateFeeStream.setDescription("减免本金");
        acLateFeeStream.setOperaId(oprId);
        acLateFeeStream.setIsSingleinterest(acLateFeeBillInfo.getIsSingleinterest());
        acLateFeeStream.setRate(acLateFeeBillInfo.getRate());
        acLateFeeStream.setOverdueDays(acLateFeeBillInfo.getOverdueDays());
        acLateFeeStreamMapper.insert(acLateFeeStream);
        //更新滞纳金总额
        AcDelayAccount paramDelayAccount = new AcDelayAccount();
        paramDelayAccount.setId(id);
        paramDelayAccount.setAmount(amount.add(diffrentAmount));
        acDelayAccountMapper.updateByPrimaryKey(paramDelayAccount);



        //查出资产账户
        AcAccount account =  acAccountMapper.selectByHouseCodeNew(houseCodeNew);

        AcAccount acAccount = new AcAccount();
        acAccount.setHouseCodeNew(houseCodeNew);
        acAccount.setLateFeeAmount(account.getLateFeeAmount().add(diffrentAmount));
        acAccount.setLastArrearsAmount(account.getLastArrearsAmount().subtract(diffrentAmount));
        //资产账户中滞纳金总额增加，上月欠费总额增加
        acAccountMapper.updateAcAccountByHouseCodeNew(acAccount);

        AcLastBillFeeInfo acLastBillFeeInfo = new AcLastBillFeeInfo();
        acLastBillFeeInfo.setLastBillFee(diffrentAmount);
        acLastBillFeeInfo.setAccountId(account.getId());
        acLastBillFeeInfo.setHouseCodeNew(houseCodeNew);
        acLastBillFeeInfo.setAccountType(type);
        //关联的上月欠费总额表
        acLastBillFeeInfoMapper.updateByHouseCodeAndAccountType(acLastBillFeeInfo);

        AcCurrentChargeDetail acCurrentChargeDetail = acCurrentChargeDetailMapper.selectChargeDetail(houseCodeNew,type);

        AcCurrentChargeDetail paramAcCurrentChargeDetail = new AcCurrentChargeDetail();

        paramAcCurrentChargeDetail.setChargeAmount((acCurrentChargeDetail.getChargeAmount()).subtract(diffrentAmount));
        paramAcCurrentChargeDetail.setCurrenctArreas((acCurrentChargeDetail.getCurrenctArreas()).subtract(diffrentAmount));
        paramAcCurrentChargeDetail.setPayableAmount((acCurrentChargeDetail.getPayableAmount()).subtract(diffrentAmount));
        paramAcCurrentChargeDetail.setUpdateTime(new Date());
        paramAcCurrentChargeDetail.setId(acCurrentChargeDetail.getId());
        acCurrentChargeDetailMapper.updateById(paramAcCurrentChargeDetail);
    }


}
