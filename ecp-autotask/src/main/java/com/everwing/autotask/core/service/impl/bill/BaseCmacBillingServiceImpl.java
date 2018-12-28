package com.everwing.autotask.core.service.impl.bill;

import com.everwing.autotask.core.dao.*;
import com.everwing.autotask.core.datasource.DBContextHolder;
import com.everwing.autotask.core.mq.MqSender;
import com.everwing.autotask.core.service.bill.BaseCmacService;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.fee.constant.AcChargeDetailBusinessTypeEnum;
import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.constant.LatefeeBusinessTypeEnum;
import com.everwing.coreservice.common.wy.fee.dto.AcChargeDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcCommonAccountDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcLateFeeDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.ContextLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
@Log4j2
@Service
public class BaseCmacBillingServiceImpl extends AbstractCmacServiceImpl{

    @Value("${batch_add_max_count}")
    private int BATCH_ADD_COUNT=500;

    @Value("${batch_update_max_count}")
    private int BATCH_UPDATE_COUNT=500;

    @Autowired
    protected TBsOwedHistoryMapper tBsOwedHistoryMapper;

    @Autowired
    protected TBsAssetAccountMapper tBsAssetAccountMapper;

    @Autowired
    protected TBsAssetAccountStreamMapper tBsAssetAccountStreamMapper;

    @Autowired
    protected TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;
    
    @Autowired
    private TcBuildingMapper tcBuildingMapper;
    
    private static List<AcChargeDetailDto> chargeDetailDtoList  = new ArrayList<>();//本期账户审核后的一系列的修改操作

    @Autowired
    protected MqSender mqSender;

    protected BaseCmacService next;

    protected void goNext(String companyId,TBsProject project){
        if(next!=null) {
            next.invoke(companyId, project);
        }
    }

    protected void addToOwedHistories(TBsAssetAccount account , List<TBsOwedHistory> insertOhList){

        if(CommonUtils.null2Double(account.getAccountBalance()) == 0){ return; }  //欠费金额为0的时候, 不产生欠费流水

        List<TBsOwedHistory> ohs = tBsOwedHistoryMapper.findAllByAccountId(account.getId());
        TBsOwedHistory oh = new TBsOwedHistory(account);
        if(CommonUtils.isEmpty(ohs)){
            oh.setOwedAmount(Math.abs(account.getAccountBalance()));
        }else{
            double currOwed = Math.abs(account.getAccountBalance());
            for(TBsOwedHistory o : ohs){
                currOwed = currOwed - o.getOwedAmount() - o.getTotalLateFee();
            }
            if(currOwed <= 0){
                return;   //如果欠费小于等于0   不再产生流水
            }
            oh.setOwedAmount(currOwed);
        }
        insertOhList.add(oh);
    }

    protected void solveList(	final String companyId, List<TBsAssetAccount> insertAccountList, 
    							List<TBsAssetAccount> updateAccountList, 
    							List<TBsAssetAccountStream> insertStreamList, 
    							List<TBsOwedHistory> insertOwedHistoryList, 
    							List<TBsOwedHistory> updateOwedHistoryList, 
    							List<TBsChargeBillHistory> updateHistories, 
    							final List<AcCommonAccountDetailDto> acCommonAccountDetailDtoList){
        int insertAccountThreadNum=insertAccountList.size()/BATCH_ADD_COUNT+1;
        int insertAccountMod=insertAccountList.size()%BATCH_ADD_COUNT;
        boolean isInsertAccountMod=(insertAccountMod==0);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(16, 64, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                new ThreadPoolExecutor.AbortPolicy());
        for(int i=0;i<insertAccountThreadNum;i++){
            final List<TBsAssetAccount> subList;
            if(i==insertAccountThreadNum-1) {
                if (isInsertAccountMod) {
                    break;
                }
                subList=insertAccountList.subList(i * BATCH_ADD_COUNT,i * BATCH_ADD_COUNT + insertAccountMod);
            }else {
                subList = insertAccountList.subList(i * BATCH_ADD_COUNT, i * BATCH_ADD_COUNT + BATCH_ADD_COUNT);
            }
            executor.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    DBContextHolder.setDBType(companyId);
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                    DataSourceTransactionManager txManager= (DataSourceTransactionManager) ContextLoader.getCurrentWebApplicationContext().getBean("transactionManager");
                    TransactionStatus status=txManager.getTransaction(def);
                    try {
                        batchInsertAccount(subList);
                        txManager.commit(status);
                    } catch (Exception e) {
                        e.printStackTrace();
                        txManager.rollback(status);
                        return false;
                    }
                    return true;
                }
            });
        }
        int updateAccountNum=updateAccountList.size()/BATCH_ADD_COUNT+1;
        int updateAccountMod=updateAccountList.size()%BATCH_ADD_COUNT;
        boolean isUpdateAccountMod=(updateAccountMod==0);
        for(int i=0;i<updateAccountNum;i++){
            final List<TBsAssetAccount> subList;
            if(i==updateAccountNum-1){
                if(isUpdateAccountMod){
                    break;
                }
                subList=updateAccountList.subList(i*BATCH_UPDATE_COUNT,i*BATCH_ADD_COUNT+updateAccountMod);

            }else {
                subList=updateAccountList.subList(i*BATCH_UPDATE_COUNT,i*BATCH_UPDATE_COUNT+BATCH_UPDATE_COUNT);
            }
            executor.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    DBContextHolder.setDBType(companyId);
                    batchUpdateAccount(subList);
                    return true;
                }
            });
        }
        int insertStreamNum=insertStreamList.size()/BATCH_ADD_COUNT+1;
        int insertStreamMod=insertStreamList.size()%BATCH_ADD_COUNT;
        boolean isInsertStreamMod=(insertStreamMod==0);
        for(int i=0;i<insertStreamNum;i++){
            final List<TBsAssetAccountStream> subList;
            if(i==insertStreamNum-1){
                if(isInsertStreamMod){
                    break;
                }
                subList=insertStreamList.subList(i*BATCH_ADD_COUNT,i*BATCH_ADD_COUNT+insertStreamMod);
            }else {
                subList=insertStreamList.subList(i*BATCH_ADD_COUNT,i*BATCH_ADD_COUNT+BATCH_ADD_COUNT);
            }
            executor.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    DBContextHolder.setDBType(companyId);
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                    DataSourceTransactionManager txManager= (DataSourceTransactionManager) ContextLoader.getCurrentWebApplicationContext().getBean("transactionManager");
                    TransactionStatus status=txManager.getTransaction(def);
                    try {
                        batchInsertAccountStream(subList);
                        txManager.commit(status);
                    } catch (Exception e) {
                        e.printStackTrace();
                        txManager.rollback(status);
                        return false;
                    }
                    mqSender.sendAcCommonDetailList(acCommonAccountDetailDtoList);
                    return true;
                }
            });
        }
        int insertOwedHistoryNum=insertOwedHistoryList.size()/BATCH_ADD_COUNT+1;
        int insertOwedHistoryMod=insertOwedHistoryList.size()%BATCH_ADD_COUNT;
        boolean isInsertOwedHistoryMod=(insertOwedHistoryMod==0);
        for(int i=0;i<insertOwedHistoryNum;i++){
            final List<TBsOwedHistory> subList;
            if(i==insertOwedHistoryNum-1){
                if(isInsertOwedHistoryMod){
                    break;
                }
                subList=insertOwedHistoryList.subList(i*BATCH_ADD_COUNT,i*BATCH_ADD_COUNT+insertOwedHistoryMod);
            }else {
                subList=insertOwedHistoryList.subList(i*BATCH_ADD_COUNT,i*BATCH_ADD_COUNT+BATCH_ADD_COUNT);
            }
            executor.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    DBContextHolder.setDBType(companyId);
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                    DataSourceTransactionManager txManager= (DataSourceTransactionManager) ContextLoader.getCurrentWebApplicationContext().getBean("transactionManager");
                    TransactionStatus status=txManager.getTransaction(def);
                    try {
                        batchInsertOwedHistory(subList);
                        txManager.commit(status);
                    } catch (Exception e) {
                        e.printStackTrace();
                        txManager.rollback(status);
                    }
                    return true;
                }
            });
        }
        int updateOwedHistoryNum=updateOwedHistoryList.size()/BATCH_UPDATE_COUNT+1;
        int updateOwedHistoryMod=updateOwedHistoryList.size()%BATCH_UPDATE_COUNT;
        boolean isUpdateOwedHistoryMod=(updateOwedHistoryMod==0);
        for(int i=0;i<updateOwedHistoryNum;i++){
            final List<TBsOwedHistory> subList;
            if(i==updateOwedHistoryNum-1){
                if(isUpdateOwedHistoryMod){
                    break;
                }
                subList=updateOwedHistoryList.subList(i*BATCH_UPDATE_COUNT,i*BATCH_UPDATE_COUNT+updateOwedHistoryMod);
            }else {
                subList=updateOwedHistoryList.subList(i*BATCH_UPDATE_COUNT,i*BATCH_UPDATE_COUNT+BATCH_UPDATE_COUNT);
            }
            executor.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    DBContextHolder.setDBType(companyId);
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                    DataSourceTransactionManager txManager= (DataSourceTransactionManager) ContextLoader.getCurrentWebApplicationContext().getBean("transactionManager");
                    TransactionStatus status=txManager.getTransaction(def);
                    try {
                        batchUpdateOwedHistory(subList);
                        txManager.commit(status);
                    } catch (Exception e) {
                        e.printStackTrace();
                        txManager.rollback(status);
                    }
                    return true;
                }
            });
        }
        int updateHistoriesNum=updateHistories.size()/BATCH_UPDATE_COUNT+1;
        int updateHistoriesMod=updateHistories.size()%BATCH_UPDATE_COUNT;
        boolean isUpdateHistoriesMod=(updateHistoriesMod==0);
        for(int i=0;i<updateHistoriesNum;i++){
            final List<TBsChargeBillHistory> subList;
            if(i==updateHistoriesNum-1){
                if(isUpdateHistoriesMod){
                    break;
                }
                subList=updateHistories.subList(i*BATCH_UPDATE_COUNT,i*BATCH_UPDATE_COUNT+updateHistoriesMod);
            }else {
                subList=updateHistories.subList(i*BATCH_UPDATE_COUNT,i*BATCH_UPDATE_COUNT+BATCH_UPDATE_COUNT);
            }
            executor.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    DBContextHolder.setDBType(companyId);
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                    DataSourceTransactionManager txManager= (DataSourceTransactionManager) ContextLoader.getCurrentWebApplicationContext().getBean("transactionManager");
                    TransactionStatus status=txManager.getTransaction(def);
                    try {
                        batchUpdateChargeBillHistory(subList);
                        txManager.commit(status);
                    } catch (Exception e) {
                        e.printStackTrace();
                        txManager.rollback(status);
                    }
                    return true;
                }
            });
        }
    }

    
    
    /**
     * 这里写一个物业，本体，水电通用的汇总数据，然后发送数据到新版账户的方法，避免在每个实现中都要写同样的代码
     *   之前是以集合的形式，后面坐了调整，以单条的形式，因为本来做通用抵扣的就很少
     * @author qhc
     * @param companyId 公司id
     * @param projectId 项目id
     * @param commonLateFeeAmount  通用账户抵扣违约金 
     * @param commonAmount 通用账户抵扣本金
     * @param type 账户类型
     * @param houseCodeNew 新的房屋表号 
     * @param chargeId  老账户系统的计费id
     * 
     * @date 2018-06-15
     */
    public void sendDataToNewAccountFromCommonBill( 
    												String companyId,
    												String projectId,
    												BigDecimal commonLateFeeAmount,
    												BigDecimal commonAmount,
    												ChargingType type,
    												String houseCodeNew,
    												String chargeId,
    												String projectName ) {
    	
    	//后面对房号进行了修改为house——code的统一操作
    	houseCodeNew = tcBuildingMapper.getHouseCodeByBuildingCode(houseCodeNew);
    	
    	log.info("对房屋{}进行通用账户扣费操作，写入新账户数据的操作",houseCodeNew);
    	// 这里如果 commonLateFeeAmount > 0 说明通用账户余额对违约金进行了抵扣     对新版账户数据进行同步
		AcLateFeeDto lateFeeDto = new AcLateFeeDto();
		AcCommonAccountDetailDto acCommonDetailDto = new AcCommonAccountDetailDto();
		AcChargeDetailDto acChrgeDetailDto =  new AcChargeDetailDto();
		if( commonLateFeeAmount.doubleValue() > 0 ) {
			lateFeeDto.setBusinessType( LatefeeBusinessTypeEnum.COMMON_ACCOUNT_DEDUCTIBLE_LATE_FEE.getCode() );
			lateFeeDto.setCompanyId( companyId );
			lateFeeDto.setDesc( LatefeeBusinessTypeEnum.COMMON_ACCOUNT_DEDUCTIBLE_LATE_FEE.getDesc() );
			lateFeeDto.setHouseCodeNew( houseCodeNew );
			lateFeeDto.setMoney( commonLateFeeAmount );
			lateFeeDto.setOperateDetailId( "system" );
			lateFeeDto.setPrincipal( new BigDecimal( 0 ) );   //本金这里我们给0
			lateFeeDto.setChargingType(ChargingType.getChargingTypeByCode(type.getCode()));
			lateFeeDto.setProjectId( projectId );
			acChrgeDetailDto.setAcLateFeeDto( lateFeeDto );
		}
		
		// 这里如果 commonAmount > 0 说明通用账户使用余额对之前的欠费进行了抵扣      对新版账户数据进行同步
		if( commonAmount.doubleValue()> 0 ) {
			acCommonDetailDto.setCompanyId(companyId);
			acCommonDetailDto.setDeductionDetailId(null);//抵扣明细id，现在提供不了
			acCommonDetailDto.setCommonLateFeeDiKou( commonLateFeeAmount ); 
			acCommonDetailDto.setMoney( commonAmount );     //扣减本金
			acCommonDetailDto.setDesc( "通用账户发生"+ type  + "抵扣" );
			acCommonDetailDto.setBusinessTypeEnum(BusinessType.DEDUCTIBLE);
			acCommonDetailDto.setProjectId( projectId );
			acCommonDetailDto.setHouseCodeNew( houseCodeNew );
		}
		
		//确实发生了抵扣才到新账户去
		if( commonLateFeeAmount.doubleValue() > 0 || commonAmount.doubleValue()> 0 ) {
			//通用账户只有发生了抵扣才调用新账户，不然没有意义
			acChrgeDetailDto.setAcCommonAccountDetailDto(acCommonDetailDto);
			acChrgeDetailDto.setCompanyId(companyId);
			acChrgeDetailDto.setAuditTime(new Date() );
			acChrgeDetailDto.setAcCommonAccountDetailDto( acCommonDetailDto );
			acChrgeDetailDto.setHouseCodeNew( houseCodeNew );
			acChrgeDetailDto.setLastChargeId( chargeId );
			acChrgeDetailDto.setAcLateFeeDto( lateFeeDto );
			acChrgeDetailDto.setBusinessTypeEnum(AcChargeDetailBusinessTypeEnum.COMMON_DK );
			acChrgeDetailDto.setCommonDiKou( commonAmount.add( commonLateFeeAmount ) );
			acChrgeDetailDto.setChargingType(type);
			acChrgeDetailDto.setProjectId( projectId );
			acChrgeDetailDto.setProjectName( projectName );
			chargeDetailDtoList.add(acChrgeDetailDto);
			
			//****************新版账户相关部分结束********************
			//新版账户的数据
//			if(chargeDetailDtoList.size() >= BATCH_ADD_COUNT){
			log.info("批量发送数据{}到消息队列:sendAcCurrentChargeForUpdate**",chargeDetailDtoList.toString());
			mqSender.sendAcCurrentChargeForUpdate( chargeDetailDtoList );
			chargeDetailDtoList.clear();
//			}
		}
    				
    }
    
    /**
     * 解决上面最后一批没法处理的问题
     */
//    public void sendLastDataToMq() {
//    	if(chargeDetailDtoList.size() > 0 ) {
//    		log.info("最后一批数据{}到消息队列:sendAcCurrentChargeForUpdate**");
//			mqSender.sendAcCurrentChargeForUpdate( chargeDetailDtoList );
//			chargeDetailDtoList.clear();
//    	}
//    }

}
