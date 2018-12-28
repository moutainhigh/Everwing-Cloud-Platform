package com.everwing.coreservice.wy.core.service.impl.configuration.cmac.single.invoke;

import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.StreamEnum;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.cmac.single.servie.ISingleCmacService;
import com.everwing.coreservice.common.wy.service.configuration.cmac.single.servie.SingleCmacElectService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("singleCmacElectService")
public class SingleCmacElectServiceImpl extends Resources implements SingleCmacElectService {

	private static final Logger logger = LogManager.getLogger(SingleCmacElectServiceImpl.class);
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void invoke(String companyId, TBsProject project,String buildCode) {
		String cpId = companyId.concat(Constants.STR_UNDERLINE).concat(project.getProjectId());
		if(CommonUtils.isEmpty(signleHandlers.get(cpId))){
			logger.warn("通用账户自动扣费 -> 扣费项 [电费] : 当前未找到可执行的服务, 计费停止.");
			return;
		}
		
		//扣费开始
		logger.info(CommonUtils.log("电费通用账户扣减开始!"));
		//找到该项目下的当前的电费总账单
		TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
		paramTotal.setBillingTime(project.getBillingTime());
		paramTotal.setCmacIsBilling(BillingEnum.common_account_is_not_bill.getIntV());   //通用账户未被扣费
		paramTotal.setProjectId(project.getProjectId());
		paramTotal.setType(BillingEnum.SCHEME_TYPE_ELECT.getIntV()); //电费方案
		List<TBsChargeBillTotal> totals = this.tBsChargeBillTotalMapper.findCmacCanbilling(paramTotal);
		if(CommonUtils.isEmpty(totals)){
			logger.info("通用账户自动扣费 -> 扣费项 [电费] : 未找到可扣费的本周期电费总账单. 查询参数: {}, 进入下一计费项. ", paramTotal.toString());
			goNext(cpId, companyId, project,buildCode);
			return;
		}else{
			TBsChargeBillTotal total = totals.get(0); //总账单
			//分账单
			TBsChargeBillHistory tbsHistory = this.tBsChargeBillHistoryMapper.getBytotalIdBuildCode(total.getId(), buildCode);
			if(CommonUtils.isEmpty(tbsHistory)){
				logger.info("通用账户自动扣费 -> 扣费项 [电费] : 未找到可扣费的本周期分账单. 总账单数据: {}, 进入下一计费项. ", total.toString());
				goNext(cpId, companyId, project,buildCode);
				return;
			}
			List<TBsAssetAccount> insertAccountList = new ArrayList<TBsAssetAccount>();
			List<TBsAssetAccount> updateAccountList = new ArrayList<TBsAssetAccount>();
			List<TBsAssetAccountStream> insertStreamList = new ArrayList<TBsAssetAccountStream>();
			List<TBsOwedHistory> insertOwedHistoryList = new ArrayList<TBsOwedHistory>();			//待插入欠费数据集合
			List<TBsOwedHistory> updateOwedHistoryList = new ArrayList<TBsOwedHistory>();			//待更新欠费数据集合
			List<TBsChargeBillHistory> updateHistories = new ArrayList<TBsChargeBillHistory>();		//待更新历史数据集合
			
			double zfje = 0.0;
			double zfLateFee = 0.0;
			double zfLateAmout = 0.0;
			//1.找到本分账单对应的电费账户 , 
			//1.1  若电费账户余额大于等于0, 表示足够扣取电费 , 不需要使用通用账户再进行抵扣. 
			TBsAssetAccount account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(tbsHistory.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_BT.getIntV());
			if(account.getAccountBalance() >= 0){
				return;
			}
			
			//1.2 若电费账户余额为负, 则需要寻找到该户的通用账户进行抵扣.
			TBsAssetAccount commonAccount = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(tbsHistory.getBuildingCode(), BillingEnum.ACCOUNT_TYPE_COMMON.getIntV());
			//1.2.1 若通用账户不存在 , 则需要创建通用账户, 该账户余额为0, 无法抵扣. 需要将欠费记录进行记录, 以便后期进行违约金计算
			if(null == commonAccount){
				commonAccount = new TBsAssetAccount();
				commonAccount.setAccountBalance(0.0);
				commonAccount.setBuildingCode(tbsHistory.getBuildingCode());
				commonAccount.setCreateId(Constants.STR_AUTO_GENER);
				commonAccount.setCreateName(Constants.STR_AUTO_GENER);
				commonAccount.setCreateTime(new Date());
				commonAccount.setFullName(tbsHistory.getFullName());
				commonAccount.setId(CommonUtils.getUUID());
				commonAccount.setModifyId(Constants.STR_AUTO_GENER);
				commonAccount.setModifyName(Constants.STR_AUTO_GENER);
				commonAccount.setModifyTime(new Date());
				commonAccount.setProjectId(project.getProjectId());
				commonAccount.setProjectName(project.getProjectName());
				commonAccount.setType(BillingEnum.ACCOUNT_TYPE_COMMON.getIntV());
				commonAccount.setUseStatus(BillingEnum.IS_USED_USING.getIntV());
				insertAccountList.add(commonAccount);
				//插入欠费记录, 欠费日期为电费账户的扣费时间, 
				addToOwedHistories(account, insertOwedHistoryList);
			}else{
				// 1.2.2 若通用账户存在 , 判断该账户是否有余额 , 
				// 1.2.2.1  若余额为0 , 则不再扣取, 记录欠费信息
				if(commonAccount.getAccountBalance() <= 0){
					addToOwedHistories(account, insertOwedHistoryList);
				}else{
					
					//1.2.2.2 余额大于0 , 则开始抵扣电费欠费, 抵扣最大额为commonAccount的余额
					//先抵扣违约金
					List<TBsOwedHistory> ohs = this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
					if(!ohs.isEmpty()){
						for(TBsOwedHistory oh : ohs){
							double balance = commonAccount.getAccountBalance();
							double lateFee = oh.getTotalLateFee();
							if(balance <= 0) break;
							oh.setTotalLateFee((balance >= lateFee) ? 0.0 : lateFee - balance);
							commonAccount.setAccountBalance( (balance >= lateFee) ? balance - lateFee : 0.0);
							
							zfLateFee += (balance >= lateFee) ? lateFee : balance;
							
							//插入违约金扣取流水
//							insertStreamList.add(new TBsAssetAccountStream(account.getId(), (balance >= lateFee) ? -lateFee : -balance));
						}

						if(commonAccount.getAccountBalance() > 0){
							for(TBsOwedHistory oh : ohs){
								double balance = commonAccount.getAccountBalance();
								double owedAmount = oh.getOwedAmount();
								
								commonAccount.setAccountBalance((balance >= owedAmount) ? balance - owedAmount : 0.0);
								oh.setOwedAmount((balance >= owedAmount) ? 0.0 : owedAmount - balance);
								oh.setIsUsed((balance >= owedAmount) ? BillingEnum.IS_USED_STOP.getIntV() : BillingEnum.IS_USED_USING.getIntV());
								
								if(balance >= owedAmount){
									oh.setOwedEndTime(new Date());
								}
								
								zfLateAmout += (balance >= owedAmount) ? owedAmount : balance;
								
								//插入本金扣取流水
//								insertStreamList.add(new TBsAssetAccountStream(account.getId(), (balance >= owedAmount) ? -owedAmount : -balance));
							}
						}
						
						updateOwedHistoryList.addAll(ohs);
					}
					
					//再抵扣本次账单
					double currBillAmount = Math.abs(account.getAccountBalance());		//本次应扣
					double currBalance = commonAccount.getAccountBalance();				//本次可扣
					account.setAccountBalance((currBalance >= currBillAmount) ? 0.0 : -(currBillAmount - currBalance));
					commonAccount.setAccountBalance((currBalance >= currBillAmount) ? currBalance - currBillAmount : 0.0);
					
					//通用账户抵扣额, 判断应扣与可扣大小
					zfje = (currBalance >= currBillAmount) ?currBillAmount : currBalance;
					
					//生成欠费数据
					if(account.getAccountBalance() < 0){
						addToOwedHistories(account, insertOwedHistoryList);
					}
					account.setModifyTime(new Date());
					commonAccount.setModifyTime(new Date());
					updateAccountList.add(account);
					
					this.tBsAssetAccountMapper.update(commonAccount);
					
//					updateAccountList.add(commonAccount);
					
					//通用账户的抵扣流水
//					insertStreamList.add(new TBsAssetAccountStream(commonAccount.getId(), (currBalance >= currBillAmount) ? -currBillAmount : -currBalance));
					
					//通用账户的抵扣流水
					double data = CommonUtils.null2Double((currBalance >= currBillAmount) ? -currBillAmount : -currBalance);
					if(data != 0){
						insertStreamList.add(new TBsAssetAccountStream(commonAccount.getId(), data, StreamEnum.purpose_dk_to_elect.getV()));  //通用账户的负流水, 抵扣电费
						insertStreamList.add(new TBsAssetAccountStream(account.getId(), Math.abs(data), StreamEnum.purpose_pay_by_common.getV()));//本账户的正流水 , 通用账户充入
					}
				}
			}
			
			tbsHistory.setAccountBalance(account.getAccountBalance()); //本期分账单的账户余额,作历史数据显示用
			double cd = CommonUtils.calKf(tbsHistory.getCurrentBillFee(), tbsHistory.getNoCommonDesummoney(), tbsHistory.getCommonDesummoney(), zfje);
			tbsHistory.setCommonDesummoney(cd);  //本期账单的通用账户抵扣额
			updateHistories.add(tbsHistory);

			TBsChargeBillHistory nextHistory = this.tBsChargeBillHistoryMapper.findNextHistory(tbsHistory);
			if(null != nextHistory){
				double lp = CommonUtils.calKf(nextHistory.getLastBillFee(), 0.0, nextHistory.getLastPayed(), zfje);
				nextHistory.setLastPayed(lp);
				updateHistories.add(nextHistory);
			}
			
			
			
			if(insertAccountList.size() >= 0){
				//账户新增
				this.tBsAssetAccountMapper.batchInsert(insertAccountList);
			}
			if(updateAccountList.size() >= 0){
				//账户修改
				this.tBsAssetAccountMapper.batchUpdate(updateAccountList);
			}
			if(insertStreamList.size() >= 0){
				//账户流水新增
				this.tBsAssetAccountStreamMapper.batchInsert(insertStreamList);
			}
			if(insertOwedHistoryList.size() >= 0){
				//新增欠费记录
				this.tBsOwedHistoryMapper.batchInsert(insertOwedHistoryList);
			}
			if(updateOwedHistoryList.size() >= 0){
				//修改欠费记录
				this.tBsOwedHistoryMapper.batchUpdate(updateOwedHistoryList);
			}
			if(updateHistories.size() >= 0){
				//修改计费明细
				this.tBsChargeBillHistoryMapper.batchInsert(updateHistories);
			}
		}

	}
	
	private void goNext(String cpId,String companyId,TBsProject project,String buildCode){
		signleHandlers.get(cpId).remove(0);
		if(CommonUtils.isEmpty(signleHandlers.get(cpId))){
			signleHandlers.remove(cpId);
			return;
		}else{
			ISingleCmacService service =  signleHandlers.get(cpId).get(0);
			if(null != service){
				service.invoke(companyId, project,buildCode);
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
			if(currOwed <= 0){
				return;   //如果欠费小于等于0   不再产生流水     
			}
			oh.setOwedAmount(currOwed);
		}
		insertOhList.add(oh);
	}

}
