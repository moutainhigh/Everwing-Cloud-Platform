package com.everwing.coreservice.wy.core.service.impl.configuration.cmac.invoke;

import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.StreamEnum;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.cmac.service.CmacWaterService;
import com.everwing.coreservice.common.wy.service.configuration.cmac.service.ICmacService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import com.everwing.coreservice.wy.core.utils.BillingUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("cmacWaterService")
public class CmacWaterServiceImpl extends Resources implements CmacWaterService{

	private static final Logger logger = LogManager.getLogger(CmacWaterServiceImpl.class);
	@Value("${queue.wy2wyBilling.wy.manual.key}")
	private String wy_billing_key;		
	
	@Value("${queue.wy2Wy.coll.gen.key}")
	private String coll_gen_key;					//托收文件生成
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void invoke(String companyId, TBsProject project) {

		String cpId = companyId.concat(Constants.STR_UNDERLINE).concat(project.getProjectId());
		if(CommonUtils.isEmpty(handlers.get(cpId))){
			logger.warn("通用账户自动扣费 -> 扣费项 [水费] : 当前未找到可执行的服务, 计费停止.");
			return;
		}
		
		//扣费开始
		logger.info(getLogStr("电费通用账户扣减开始!"));
		//找到该项目下的当前的电费总账单
		TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
		paramTotal.setBillingTime(project.getBillingTime());
		paramTotal.setCmacIsBilling(BillingEnum.common_account_is_not_bill.getIntV());   //通用账户未被扣费
		paramTotal.setProjectId(project.getProjectId());
		paramTotal.setType(BillingEnum.SCHEME_TYPE_WATER.getIntV()); //水费方案
		List<TBsChargeBillTotal> totals = this.tBsChargeBillTotalMapper.findCmacCanbilling(paramTotal);
		if(CommonUtils.isEmpty(totals)){
			logger.info("通用账户自动扣费 -> 扣费项 [水费] : 未找到可扣费的本周期水费总账单. 查询参数: {}, 进入下一计费项. ", paramTotal.toString());
			goNext(cpId, companyId, project);
			return;
		}else{
			TBsChargeBillTotal total = totals.get(0); //总账单
			//分账单
			List<TBsChargeBillHistory> histories = this.tBsChargeBillHistoryMapper.findAllByTotalId(total.getId());
			if(CommonUtils.isEmpty(histories)){
				logger.info("通用账户自动扣费 -> 扣费项 [水费] : 未找到可扣费的本周期分账单. 总账单数据: {}, 进入下一计费项. ", total.toString());
				goNext(cpId, companyId, project);
				return;
			}
			List<TBsAssetAccount> insertAccountList = new ArrayList<TBsAssetAccount>();
			List<TBsAssetAccount> updateAccountList = new ArrayList<TBsAssetAccount>();
			List<TBsAssetAccountStream> insertStreamList = new ArrayList<TBsAssetAccountStream>();
			List<TBsOwedHistory> insertOwedHistoryList = new ArrayList<TBsOwedHistory>();			//待插入欠费数据集合
			List<TBsOwedHistory> updateOwedHistoryList = new ArrayList<TBsOwedHistory>();			//待更新欠费数据集合
			List<TBsChargeBillHistory> updateHistories = new ArrayList<TBsChargeBillHistory>();		//待更新历史数据集合
			for(TBsChargeBillHistory history : histories){
				double zfje = 0.0;
				double zfLateFee = 0.0;
				double zfLateAmout = 0.0;
				
				//1.找到本分账单对应的 电费账户 , 
				//1.1  若水费账户余额大于等于0, 表示足够扣水费 , 不需要使用通用账户再进行抵扣.
				TBsAssetAccount account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(history.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_WATER.getIntV());
				if(null == account || CommonUtils.null2Double(account.getAccountBalance()) >= 0){
					continue;
				}
				//1.2 若水费账户余额为负, 则需要寻找到该户的通用账户进行抵扣.
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
					
					//插入欠费记录, 欠费日期为电费账户的扣费时间, 
					addToOwedHistories(account, insertOwedHistoryList);
				}else{
					// 1.2.2 若通用账户存在 , 判断该账户是否有余额 , 
					// 1.2.2.1  若余额为0 , 则不再扣取, 记录欠费信息
					if(commonAccount.getAccountBalance() <= 0){
						addToOwedHistories(account, insertOwedHistoryList);
					}else{
						//1.2.2.2 余额大于0 , 则开始抵扣该水费欠费, 抵扣最大额为commonAccount的余额
						//先抵扣违约金
						List<TBsOwedHistory> ohs = this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
						if(!ohs.isEmpty()){
							double balance = CommonUtils.null2Double(commonAccount.getAccountBalance());
							for(TBsOwedHistory oh : ohs){
								if(balance <= 0) break;
								double lateFee = oh.getTotalLateFee();
								oh.setTotalLateFee((balance >= lateFee) ? 0.0 : lateFee - balance);
								zfLateFee += (balance >= lateFee) ? lateFee : balance;
								//插入违约金扣取流水
//								insertStreamList.add(new TBsAssetAccountStream(commonAccount.getId(), (balance >= lateFee) ? -lateFee : -balance));
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
									
									//插入电费扣取流水
//									insertStreamList.add(new TBsAssetAccountStream(commonAccount.getId(), (balance >= owedAmount) ? -owedAmount : -balance));
								}
							}
							updateOwedHistoryList.addAll(ohs);
							
						}
						//再抵扣本次账单
						double currBillAmount = Math.abs(account.getAccountBalance());		//本次应扣
						double currBalance = commonAccount.getAccountBalance();				//本次可扣
						account.setAccountBalance((currBalance >= currBillAmount) ? 0.0 : -(currBillAmount - currBalance));
						commonAccount.setAccountBalance((currBalance >= currBillAmount) ? (currBalance - currBillAmount) : 0.0);
						
						//通用账户抵扣额, 判断应扣与可扣大小
						zfje = (currBalance >= currBillAmount) ? currBillAmount : currBalance;
						
						//生成欠费数据
						if(account.getAccountBalance() < 0){
							addToOwedHistories(account, insertOwedHistoryList);
						}
						account.setModifyTime(new Date());
						commonAccount.setModifyTime(new Date());
						updateAccountList.add(account);
						//即时更新通用账户
						this.tBsAssetAccountMapper.update(commonAccount);
						
						//通用账户的抵扣流水
						double data = CommonUtils.null2Double((currBalance >= currBillAmount) ? -currBillAmount : -currBalance);
						if(data != 0){
							insertStreamList.add(new TBsAssetAccountStream(commonAccount.getId(), data, StreamEnum.purpose_dk_to_water.getV()));  //通用账户的负流水 , 抵扣水费
							insertStreamList.add(new TBsAssetAccountStream(account.getId(), Math.abs(data), StreamEnum.purpose_pay_by_common.getV()));//本账户的正流水, 通用账户充入
						}
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
				
				if(insertAccountList.size() >= BATCH_ADD_COUNT){
					BillingUtils.sendInsertList(insertAccountList, wy_billing_key, companyId, amqpTemplate);
				}
				
				if(updateAccountList.size() >= BATCH_UPDATE_COUNT){
					BillingUtils.sendUpdateList(updateAccountList, wy_billing_key, companyId, amqpTemplate);
				}
				if(insertStreamList.size() >= BATCH_ADD_COUNT){
					BillingUtils.sendInsertList(insertStreamList, wy_billing_key, companyId, amqpTemplate);
				}
				if(insertOwedHistoryList.size() >= BATCH_ADD_COUNT){
					BillingUtils.sendInsertList(insertOwedHistoryList, wy_billing_key, companyId, amqpTemplate);
				}
				if(updateOwedHistoryList.size() >= BATCH_UPDATE_COUNT){
					BillingUtils.sendUpdateList(updateOwedHistoryList, wy_billing_key, companyId, amqpTemplate);
				}
				if(updateHistories.size() >= BATCH_UPDATE_COUNT){
					BillingUtils.sendUpdateList(updateHistories, wy_billing_key, companyId, amqpTemplate);
				}
			}
			
			if(!insertAccountList.isEmpty()){
				BillingUtils.sendInsertList(insertAccountList, wy_billing_key, companyId, amqpTemplate);
			}
			
			if(!updateAccountList.isEmpty()){
				BillingUtils.sendUpdateList(updateAccountList, wy_billing_key, companyId, amqpTemplate);
			}
			if(!insertStreamList.isEmpty()){
				BillingUtils.sendInsertList(insertStreamList, wy_billing_key, companyId, amqpTemplate);
			}
			if(!insertOwedHistoryList.isEmpty()){
				BillingUtils.sendInsertList(insertOwedHistoryList, wy_billing_key, companyId, amqpTemplate);
			}
			if(!updateOwedHistoryList.isEmpty()){
				BillingUtils.sendUpdateList(updateOwedHistoryList, wy_billing_key, companyId, amqpTemplate);
			}
			if(!updateHistories.isEmpty()){
				BillingUtils.sendUpdateList(updateHistories, wy_billing_key, companyId, amqpTemplate);
			}
			
			total.setCmacIsBilling(BillingEnum.common_account_is_billed.getIntV());
			
			//开始聚合总账单
			this.tBsChargeBillTotalMapper.update(total);
			
			//开始对本次的数据投递到托收   投递至TBcCollectionService.genColl
			TBcProject bcProject = this.tBcProjectMapper.findByProjectId(total.getProjectId());
//			if(bcProject.getStatus() == CollectionEnum.status_on.getV()){
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
			goNext(cpId, companyId, project);
		}		
		
		
	}
	
	private void goNext(String cpId,String companyId,TBsProject project){
		handlers.get(cpId).remove(0);
		if(CommonUtils.isEmpty(handlers.get(cpId))){
			handlers.remove(cpId);
			return;
		}else{
			ICmacService service =  handlers.get(cpId).get(0);
			if(null != service){
				service.invoke(companyId, project);
			}
		}
	}
	
	private void addToOwedHistories(TBsAssetAccount account , List<TBsOwedHistory> insertOhList){
		
		if(CommonUtils.null2Double(account.getAccountBalance()) == 0) return;   //欠费金额为0的时候, 不产生欠费流水
		
		List<TBsOwedHistory> ohs = this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
		TBsOwedHistory oh = new TBsOwedHistory(account);
		if(CommonUtils.isEmpty(ohs)){
			oh.setOwedAmount(Math.abs(account.getAccountBalance()));
		}else{
			double currOwed = Math.abs(account.getAccountBalance());
			for(TBsOwedHistory o : ohs){
				currOwed = currOwed - o.getOwedAmount() - o.getTotalLateFee();
			}
			oh.setOwedAmount(currOwed);
			if(currOwed <= 0){
				return;   //如果欠费小于等于0   不再产生流水     
			}
		}
		insertOhList.add(oh);
	}
	
	private String getLogStr(String error){
		return String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),error);
	}
}
