package com.everwing.autotask.core.service.impl.bill;

import com.everwing.autotask.core.dao.TBcProjectMapper;
import com.everwing.autotask.core.dao.TBsChargeBillTotalMapper;
import com.everwing.autotask.core.service.bill.BaseCmacService;
import com.everwing.autotask.core.service.bill.CmacBtService;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.dto.AcCommonAccountDetailDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/6/
 */
@Log4j2
@Service
public class CmacBtServiceImpl extends BaseCmacBillingServiceImpl implements CmacBtService{

    @Autowired
    private TBsChargeBillTotalMapper tBsChargeBillTotalMapper;

    @Autowired
    private TBcProjectMapper tBcProjectMapper;
    

    @Override
    public void setNext(BaseCmacService cmacService) {
        this.next=cmacService;
    }

    @Override
    public void invoke(String companyId, TBsProject project) {
        log.info("通用账户自动扣费 -> 扣费项 [本体基金]");
        //找到该项目下的当前的物业总账单
        TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
        paramTotal.setBillingTime(project.getBillingTime());
        paramTotal.setCmacIsBilling(BillingEnum.common_account_is_not_bill.getIntV());   //通用账户未被扣费
        paramTotal.setProjectId(project.getProjectId());
        paramTotal.setType(BillingEnum.SCHEME_TYPE_BT.getIntV());
        List<TBsChargeBillTotal> totals = tBsChargeBillTotalMapper.findCmacCanbilling(paramTotal);
        if(CommonUtils.isEmpty(totals)){
            log.info("通用账户自动扣费 -> 扣费项 [本体基金] : 未找到可扣费的本周期本体基金总账单. 查询参数: {}, 进入下一计费项. ", paramTotal.toString());
            goNext(companyId, project);
            return;
        }else{

            TBsChargeBillTotal total = totals.get(0);

            List<TBsChargeBillHistory> histories = this.tBsChargeBillHistoryMapper.findAllByTotalId(total.getId());
            if(CommonUtils.isEmpty(histories)){
                log.info("通用账户自动扣费 -> 扣费项 [本体基金] : 未找到可扣费的本周期分账单. 总账单数据: {}, 进入下一计费项. ", total.toString());
                goNext(companyId, project);
                return;
            }

            List<TBsAssetAccount> insertAccountList = new ArrayList<TBsAssetAccount>();
            List<TBsAssetAccount> updateAccountList = new ArrayList<TBsAssetAccount>();
            List<TBsAssetAccountStream> insertStreamList = new ArrayList<TBsAssetAccountStream>();
            List<AcCommonAccountDetailDto> acCommonAccountDetailDtoList=new ArrayList<>();
            List<TBsOwedHistory> insertOwedHistoryList = new ArrayList<TBsOwedHistory>();			//待插入欠费数据集合
            List<TBsOwedHistory> updateOwedHistoryList = new ArrayList<TBsOwedHistory>();			//待更新欠费数据集合
            List<TBsChargeBillHistory> updateHistories = new ArrayList<TBsChargeBillHistory>();		//待更新历史数据集合
            for(TBsChargeBillHistory history : histories){
            	log.info("开始通用抵扣本体循环扣费{}",history.getBuildingCode());
                double zfje = 0.0;
                double zfLateFee = 0.0;
                double zfLateAmout = 0.0;
                //1.找到本分账单对应的 物业费账户 ,
                //1.1  若本体基金账户余额大于等于0, 表示足够扣取本体基金 , 不需要使用通用账户再进行抵扣.
                TBsAssetAccount account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(history.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_BT.getIntV());
                if(null == account || CommonUtils.null2Double(account.getAccountBalance()) >= 0){
                    continue;
                }

                //1.2 若本体基金账户余额为负, 则需要寻找到该户的通用账户进行抵扣.
                TBsAssetAccount commonAccount = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(history.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_COMMON.getIntV());
                //1.2.1 若通用账户不存在 , 则需要创建通用账户, 该账户余额为0, 无法抵扣. 需要将欠费记录进行记录, 以便后期进行违约金计算
                if(null == commonAccount){
                    commonAccount = new TBsAssetAccount();
                    commonAccount.setAccountBalance(0.0);
                    commonAccount.setBuildingCode(history.getBuildingCode());
                    commonAccount.setCreateId(Constants.STR_AUTO_GENER);
                    commonAccount.setCreateName(Constants.STR_AUTO_GENER);
                    commonAccount.setCreateTime(new Date());
                    commonAccount.setFullName(history.getFullName());
                    commonAccount.setId(CommonUtils.getUUID());
                    commonAccount.setModifyId(Constants.STR_AUTO_GENER);
                    commonAccount.setModifyName(Constants.STR_AUTO_GENER);
                    commonAccount.setModifyTime(new Date());
                    commonAccount.setProjectId(project.getProjectId());
                    commonAccount.setProjectName(project.getProjectName());
                    commonAccount.setType(BillingEnum.ACCOUNT_TYPE_COMMON.getIntV());
                    commonAccount.setUseStatus(BillingEnum.IS_USED_USING.getIntV());
                    //应该直接插入,避免通用账户重复插入
                    this.tBsAssetAccountMapper.singleInsert(commonAccount);

                    //插入欠费记录, 欠费日期为本体基金账户的扣费时间,
                    addToOwedHistories(account, insertOwedHistoryList);

                }else{
                    // 1.2.2 若通用账户存在 , 判断该账户是否有余额 ,
                    // 1.2.2.1  若余额为0 , 则不再扣取, 记录欠费信息
                    if(commonAccount.getAccountBalance() <= 0){
                        addToOwedHistories(account, insertOwedHistoryList);
                    }else{

                        //1.2.2.2 余额大于0 , 则开始抵扣该本体基金欠费, 抵扣最大额为commonAccount的余额
                        //先抵扣违约金
                        List<TBsOwedHistory> ohs = this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
                        if(!ohs.isEmpty()){
                            double balance = CommonUtils.null2Double(commonAccount.getAccountBalance());
                            for(TBsOwedHistory oh : ohs){
                                if(balance <= 0) break;
                                double lateFee = oh.getTotalLateFee();
                                oh.setTotalLateFee((balance >= lateFee) ? 0.0 : lateFee - balance);
                                zfLateFee += (balance >= lateFee) ? lateFee : balance;
                                balance -= (balance >= lateFee) ? lateFee : balance;
                            }

                            if(zfLateFee > 0 ) {
                            	//插入违约金扣取流水(一条通用账户的负数流水，一条本体基金账户的正数流水)
								insertStreamList.add(new TBsAssetAccountStream(CommonUtils.getUUID(),commonAccount.getId(),-zfLateAmout,
											new Date(),"system","system","通用账户抵扣本体基金违约金"));
								insertStreamList.add(new TBsAssetAccountStream(CommonUtils.getUUID(),account.getId(), zfLateAmout,
										new Date(),"system","system","通用账户抵扣本体基金违约金"));
                            }
                            
                            if(balance > 0){
                                for(TBsOwedHistory oh : ohs){
                                    if(balance <= 0) break;
                                    double owedAmount = oh.getOwedAmount();

                                    oh.setOwedAmount((balance >= owedAmount) ? 0.0 : owedAmount - balance);
                                    oh.setIsUsed((balance >= owedAmount) ? BillingEnum.IS_USED_STOP.getIntV() : BillingEnum.IS_USED_USING.getIntV());

                                    if(balance >= owedAmount){
                                        oh.setOwedEndTime(new Date());
                                    }

                                    zfLateAmout += (balance >= owedAmount) ? owedAmount : balance;
                                    balance -= (balance >= owedAmount) ? owedAmount : balance;
                                
                                }
                            }

                            if(zfLateAmout > 0) {
                                //插入本金扣取流水(一条通用账户的负数流水，一条本体基金账户的正数流水)
								insertStreamList.add(new TBsAssetAccountStream(CommonUtils.getUUID(),commonAccount.getId(), -zfLateAmout,
										new Date(),"system","system","通用账户抵扣本体基金历史欠费"));
								insertStreamList.add(new TBsAssetAccountStream(CommonUtils.getUUID(),account.getId(), zfLateAmout,
										new Date(),"system","system","通用账户抵扣本体基金历史欠费"));
                            }
                            
                            updateOwedHistoryList.addAll(ohs);
                            commonAccount.setAccountBalance( balance );  //通用账户的余额已经发生变化了
                        }

                        
                        //再抵扣本次账单
                        double currBillAmount = Math.abs(account.getAccountBalance());		//本次应扣
                        double currBalance = commonAccount.getAccountBalance();				//本次可扣
                        account.setAccountBalance((currBalance >= currBillAmount) ? 0.0 : -(currBillAmount - currBalance));
                        commonAccount.setAccountBalance((currBalance >= currBillAmount) ? (currBalance - currBillAmount) : 0.0);

                        //通用账户抵扣额, 判断应扣与可扣大小
                        zfje = (currBalance >= currBillAmount) ? currBillAmount : currBalance;
                        
                        if( zfje > 0 ) {
                        	log.info("通用对本体进行了抵扣{}",account.getId());
                        	//插入本金扣取流水(一条通用账户的负数流水，一条电费账户的正数流水)
							insertStreamList.add(new TBsAssetAccountStream(CommonUtils.getUUID(),commonAccount.getId(),-zfje,
									new Date(),"system","system","通用账户抵扣本体本期欠费"));
							insertStreamList.add(new TBsAssetAccountStream(CommonUtils.getUUID(),account.getId(), zfje,
									new Date(),"system","system","通用账户抵扣本体本期欠费"));
                        }
                        
                        zfLateAmout += zfje;
                        //*************到这里抵扣已经结束，开始准备新账户需要插入的数据S
                        if( zfLateFee > 0 || zfLateAmout > 0 ) {
                        	sendDataToNewAccountFromCommonBill(companyId, history.getProjectId(), 
                        		new BigDecimal(zfLateFee), new BigDecimal( zfLateAmout ) , ChargingType.getChargingTypeByCode(total.getType()), history.getBuildingCode(), history.getId(),project.getProjectName());
                        }
                        //*************到这里抵扣已经结束，开始准备新账户需要插入的数据S
                        //生成欠费数据
                        if(account.getAccountBalance() < 0){
                            addToOwedHistories(account, insertOwedHistoryList);
                        }
                        account.setModifyTime(new Date());
                        commonAccount.setModifyTime(new Date());
                        updateAccountList.add(account);
                        //即时更新通用账户
                        this.tBsAssetAccountMapper.update(commonAccount);
                        tBsAssetAccountMapper.update(account);

                        //本账户流水为正 , 而通用账户流水为负
//						insertStreamList.add(new TBsAssetAccountStream(account.getId(), (currBalance >= currBillAmount) ? currBillAmount : currBalance));

                        //通用账户的抵扣流水
//                        double data = CommonUtils.null2Double((currBalance >= currBillAmount) ? -currBillAmount : -currBalance);
//                        BigDecimal money=new BigDecimal((currBalance >= currBillAmount) ?currBillAmount:currBalance);
//                        AcCommonAccountDetailDto commonAccountDetailDto=new AcCommonAccountDetailDto();
//                        commonAccountDetailDto.setCompanyId(companyId);
//                        commonAccountDetailDto.setProjectId(project.getProjectId());
//                        commonAccountDetailDto.setProjectName(project.getProjectName());
//                        commonAccountDetailDto.setBusinessTypeEnum(BusinessType.DEDUCTIBLE);
//                        commonAccountDetailDto.setDesc("通用账户抵扣定时任务");
//                        commonAccountDetailDto.setHouseCodeNew(history.getBuildingCode());
//                        commonAccountDetailDto.setMoney(money);
//                        commonAccountDetailDto.setOperator("autoTask");
//                        if(data != 0){
//                            TBsAssetAccountStream commonStream=new TBsAssetAccountStream(commonAccount.getId(), data, StreamEnum.purpose_dk_to_bt.getV());
//                            insertStreamList.add(commonStream);  //通用账户的负流水   扣往本体
//                            acCommonAccountDetailDtoList.add(commonAccountDetailDto);
//                            TBsAssetAccountStream dkStream=new TBsAssetAccountStream(account.getId(), Math.abs(data),StreamEnum.purpose_pay_by_common.getV());
//                            insertStreamList.add(dkStream);//本账户的正流水 , 通用账户充入
//                        }
                    }
                }

                
                history.setAccountBalance(account.getAccountBalance()); //本期分账单的账户余额,作历史数据显示用
                double cd = CommonUtils.calKf(history.getCurrentBillFee(), history.getNoCommonDesummoney(), history.getCommonDesummoney(), zfje);
                history.setCommonDesummoney(cd);  //本期账单的通用账户抵扣额
                updateHistories.add(history);

                TBsChargeBillHistory nextHistory = this.tBsChargeBillHistoryMapper.findNextHistory(history);
                if(null != nextHistory){
                    double lp = CommonUtils.calKf(nextHistory.getLastBillFee(), 0.0, nextHistory.getLastPayed(), zfje);
                    nextHistory.setLastPayed(lp);
                    updateHistories.add(nextHistory);
                }
            }

            log.info("本体进行流水的写入步骤");
            solveList(companyId,insertAccountList,updateAccountList,insertStreamList,insertOwedHistoryList,updateOwedHistoryList,updateHistories, acCommonAccountDetailDtoList);

            total.setCmacIsBilling(BillingEnum.common_account_is_billed.getIntV());
            //开始聚合总账单
//			double totalOwedFee = this.tBsChargeBillHistoryMapper.findAllOwedFeeByTotalId(total.getId());
            this.tBsChargeBillTotalMapper.update(total);
            
            //开始对本次的数据投递到托收 投递至TBcCollectionService.genColl

            TBcProject bcProject = this.tBcProjectMapper.findByProjectId(total.getProjectId());

            //这里不再审核后紧接着就托收，因为后面财务会审核托收数据
//			if(false) {//  bcProject.getStatus() == CollectionEnum.status_on.getV()){
//				logger.info("本体基金通用账户扣取: 组装数据, 准备投递至托收文件生成. ");
//				MqEntity me = new MqEntity();
//				me.setProjectId(bcProject.getProjectId());
//				me.setProjectName(bcProject.getProjectName());
//				me.setData(bcProject);
//				me.setSupAttr(total.getId());
//				me.setCompanyId(companyId);
//				me.setOpr(CollectionEnum.common_account_using.getV());
//				this.amqpTemplate.convertAndSend(coll_gen_key, me);
//				logger.info("本体基金通用账户扣取: 组装数据完成, 投递至托收文件生成  完成. routeKey:{}, 数据:{}. ", coll_gen_key, me.toString());
//			}

            //计算本项后 , 进入下一计费项
            goNext(companyId, project);
        }
    }

}
