package com.everwing.autotask.core.service.impl.bill;

import com.everwing.autotask.core.dao.*;
import com.everwing.autotask.core.datasource.DBContextHolder;
import com.everwing.autotask.core.service.bill.LateFeeService;
import com.everwing.coreservice.common.ThreadPool.ThreadPoolUtils;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.CollectionEnum;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.TBcCollectionTotal;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.constant.LatefeeBusinessTypeEnum;
import com.everwing.coreservice.common.wy.fee.entity.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 违约金
 *
 * @author DELL shiny
 * @create 2018/6/4
 */
@Log4j2
@Service
public class LateFeeServiceImpl implements LateFeeService{

    @Autowired
    private TBcProjectMapper tBcProjectMapper;

    @Autowired
    private TBcCollectionMapper tBcCollectionMapper;
    
    @Autowired
    private AcDelayAccountMapper acDelayAccountMapper;
    
    @Autowired
    private AcLateFeeStreamMapper acLateFeeStreamMapper;
    
    @Autowired
    private AcCurrentChargeDetailMapper acCurrentChargeDetailMapper;
    
    @Autowired
    private AcAccountMapper acAccountMapper;
    
    @Autowired
    private AcLateFeeBillInfoMapper acLateFeeBillInfoMapper;
    
    @Autowired 
    private AcLastBillFeeInfoMapper acLastBillFeeInfoMapper;
    

    /**
     * 新的违约金计算方式:
     * 		违约金现在只和对应的违约金账户已经流水有关系，其他不涉及，违约金单独独立出来了
     */
    @Override
    public void billLateFee(final String companyId, TBsProject project) {

        boolean isCollectioning = false;   //是否在托收期间中
        boolean isJrl = false;    //是否为金融联托收
        String totalId = null;    //计费总账单id
        final TBcProject tBcProject = tBcProjectMapper.findByProjectId(project.getProjectId());
        if(tBcProject != null && tBcProject.getStatus() .equals(CollectionEnum.status_on.getV())){
        	//如果项目是开启托收的模式，检查现在是否正在进行托收等待回盘期间，如果是不可以及违约金
            Integer collectionType = null;
            if(collectionType == null && tBcProject.getWyStatus() .equals(CollectionEnum.status_on.getV()) && tBcProject.getWyType() != CollectionEnum.type_off.getV()){
                collectionType = tBcProject.getWyType();
            }else if(collectionType == null && tBcProject.getBtStatus().equals(CollectionEnum.status_on.getV()) && tBcProject.getBtType() != CollectionEnum.type_off.getV()){
                collectionType = tBcProject.getBtType();
            }else if(collectionType == null && tBcProject.getWaterStatus().equals(CollectionEnum.status_on.getV()) && tBcProject.getWaterType() != CollectionEnum.type_off.getV()){
                collectionType = tBcProject.getWaterType();
            }else if(collectionType == null && tBcProject.getElectStatus().equals( CollectionEnum.status_on.getV()) && tBcProject.getElectType() != CollectionEnum.type_off.getV()){
                collectionType = tBcProject.getElectType();
            }

            //得到当前最新托收汇总信息
            TBcCollectionTotal total = tBcCollectionMapper.findRecentTotal(project.getProjectId(), collectionType);
            totalId = total.getId();
            if(CommonUtils.isEquals(Constants.STR_YES, total.getIsWaitBack())){
                //是待回盘状态,判断buildingCodes是否尚处于托收状态
                isCollectioning = true;
                isJrl = collectionType == CollectionEnum.type_jrl.getV();
            }
        }

        for (final ChargingType chargingType:ChargingType.values()) {
        	final int accountType = chargingType.getCode();
        	ThreadPoolUtils.getInstance().executeThread(new Runnable() {
    			
    			@Override
    			public void run() {
    				 DBContextHolder.setDBType(companyId);//切换数据源
    				
    				//根据项目id和账户类型查询出当前系统中需要进行违约金计算的所有违约金账户信息
    				
    				//应该按照欠费的资产进行循环，这样既包含1.本身就欠费违约金   2，新的需要计费违约金的房屋
    				List<AcLastBillFeeInfo> lastBillList = acLastBillFeeInfoMapper.selectByProjectAndAccountType(tBcProject.getProjectId(), accountType);
    				for (AcLastBillFeeInfo lastBillFee : lastBillList) {
    					AcAccount acAccount=acAccountMapper.selectByHouseCodeNew(lastBillFee.getHouseCodeNew());
						//找出最新的一条违约金流水，里面有具体计费信息（有可能是空的）  -- 切换方法了  qhc  2018-08-02
    					//先查询违约金账户是否存在
    					AcDelayAccount delayAccount = acDelayAccountMapper.selectByProjectAndAccountType(lastBillFee.getHouseCodeNew(),tBcProject.getProjectId(), accountType);
    					if( CommonUtils.isEmpty( delayAccount ) ) {
    						//如果还没有违约金账户要进行创建
    						String logStream= UUID.randomUUID().toString();
    						log.info("{}未找到此类型{}的滞纳金账户,开始创建",logStream,chargingType.getDescription());
    						delayAccount=new AcDelayAccount();
    						delayAccount.setAccountId(acAccount.getId());
    						delayAccount.setId(logStream);
    						delayAccount.setAmount(new BigDecimal(0));
    						delayAccount.setProjectId(lastBillFee.getProjectId());
    						delayAccount.setProjectName(lastBillFee.getProjectName());
    						delayAccount.setCreateId("system");
    						delayAccount.setCreateTime(new Date());
    						delayAccount.setHouseCodeNew(lastBillFee.getHouseCodeNew());
    						delayAccount.setModifyTime(new Date());
    			            acDelayAccountMapper.insert(delayAccount);
    			            log.info("{}资产滞纳金账户创建成功!",logStream);
    					}
    					AcLateFeeStream acLateFeeStream = acLateFeeStreamMapper.selectLastOneByDelayAccountId(lastBillFee.getHouseCodeNew(),(int)lastBillFee.getAccountType() );
    					//账户信息,用于汇总使用
    					//这里同时检验是否有新的需要计入违约金计算本金的欠费信息	
    					//这里直接查询每个项目的计费违约金的规则信息
    					AcLateFeeBillInfo acLateFeeBillInfo = acLateFeeBillInfoMapper.getAcLateFeeBillInfoByProject(tBcProject.getProjectId(),accountType);
    					if( CommonUtils.isEmpty( acLateFeeBillInfo ) ) {
    						continue;
    					}
    					int overDays = (int)CommonUtils.null2Double( acLateFeeBillInfo.getOverdueDays() ) ;
    					//计算出：如果今天需要添加到计算违约金本金，推算出对应的审核时间
    					String rightDate = CommonUtils.getDateStr( CommonUtils.addDay(new Date(), (0-overDays)),"yyyy-MM-dd"); 
    					//找出需要添加到计费违约金的本金中（amount就是本金）
    					double amount = acCurrentChargeDetailMapper.selectChargeAmountForLateFee(lastBillFee.getHouseCodeNew(),rightDate,accountType);
    					
    					
    					if( CommonUtils.isEmpty( acLateFeeStream ) ) {
    						acLateFeeStream = new AcLateFeeStream();
    						acLateFeeStream.setPrincipalAccount( BigDecimal.valueOf( 0 ) );
    						acLateFeeStream.setAfterAmount( BigDecimal.valueOf( 0 ) );
    						acLateFeeStream.setBeforeAmount(BigDecimal.valueOf(0));
    					}else {
    						acLateFeeStream.setBeforeAmount(acLateFeeStream.getAfterAmount());
    					}
    					BigDecimal beforAmount = acLateFeeStream.getAfterAmount();
    					
//    					BigDecimal principal = acLateFeeStream.getPrincipalAccount().add( BigDecimal.valueOf( amount ) );//本金
    					BigDecimal afterAmount = acLateFeeStream.getAfterAmount().add( BigDecimal.valueOf( amount ) );//截止现在违约金总和(如果是复利用这个计算)
    					
    					if( afterAmount.doubleValue() <= 0) {
    						log.info("{}房现在不欠费，不需要再计算违约金信息了",lastBillFee.getHouseCodeNew());
    						continue;
    					}
    					
    					//判断本金是否大于0
    					double lateFee = 0.0;
    		            if( acLateFeeBillInfo.getIsSingleinterest().equals(BillingEnum.SCHEME_CLAC_TYPE_SIMPLE.getIntV())){
    		                //单利模式的违约金计算   本金*利率即可, 违约金再累加
    		                lateFee = Math.abs(amount) * ( acLateFeeBillInfo.getRate().doubleValue() / 1000);
//    		                afterAmount = afterAmount.add( BigDecimal.valueOf( lateFee ) ).setScale(2, BigDecimal.ROUND_HALF_UP);
//    		                acLateFeeStream.setBeforeAmount( beforAmount );
    		                acLateFeeStream.setAfterAmount(beforAmount.add( BigDecimal.valueOf( lateFee ) ).setScale(2, BigDecimal.ROUND_HALF_UP));
    		            }else if( acLateFeeBillInfo.getIsSingleinterest().equals(BillingEnum.SCHEME_CLAC_TYPE_CPX.getIntV())){
    		                //复利模式的违约金计算 本利和*利率
    		                lateFee = (afterAmount.doubleValue()) * ( acLateFeeBillInfo.getRate().doubleValue() / 1000 );
//    		                acLateFeeStream.setBeforeAmount( beforAmount );
    		                acLateFeeStream.setAfterAmount(beforAmount.add( BigDecimal.valueOf( lateFee ) ).setScale(2, BigDecimal.ROUND_HALF_UP));
    		            }
    		            
    		            if( lateFee <= 0 ) {
    		            	continue;
    		            }

    		            //进行违约金的增加流水，和汇总违约金总金额
    		            acLateFeeStream.setId(CommonUtils.getUUID());
//    		            acLateFeeStream.setBeforeAmount(beforAmount);
    		            acLateFeeStream.setPrincipalAccount( BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_UP) );
//    		            acLateFeeStream.setAfterAmount(afterAmount);
    		            acLateFeeStream.setChangeAmount( BigDecimal.valueOf( lateFee ).setScale(2, BigDecimal.ROUND_HALF_UP) );
    		            acLateFeeStream.setDelayAccountId( delayAccount.getId() );
    		            acLateFeeStream.setHouseCodeNew(acAccount.getHouseCodeNew());
    		            acLateFeeStream.setProjectId(acAccount.getProjectId());
    		            acLateFeeStream.setProjectName(acAccount.getProjectName());
    		            acLateFeeStream.setOperaId( "system" );
    		            acLateFeeStream.setBusinessType((short) LatefeeBusinessTypeEnum.PRODUCE_LATE_FEE.getCode());
    		            acLateFeeStream.setCreateId("system");
    		            acLateFeeStream.setCreateTime(new Date());
    		            acLateFeeStream.setIsSingleinterest( acLateFeeBillInfo.getIsSingleinterest() );
    		            acLateFeeStream.setOverdueDays( acLateFeeBillInfo.getOverdueDays() );
    		            acLateFeeStream.setRate( acLateFeeBillInfo.getRate() );
    		            acLateFeeStream.setDescription("每天定时任务计算违约金");
    		            
    		            
    		            AcDelayAccount delayAccountUp = new AcDelayAccount();
    		            delayAccountUp.setId( delayAccount.getId() );
    		            delayAccountUp.setAmount( BigDecimal.valueOf( lateFee ) );
    		            
    		            AcAccount accountUp = new AcAccount();
    		            accountUp.setId( acAccount.getId() );
    		            accountUp.setLateFeeAmount( BigDecimal.valueOf( lateFee ) );
    		            
    		            acLateFeeStreamMapper.insert(acLateFeeStream);
    		            acDelayAccountMapper.updateByPrimaryKeySelective(delayAccountUp);
    		            acAccountMapper.updateByPrimaryKeyForAmount(accountUp);
					}
    			}
    		});
		}
    }

}
