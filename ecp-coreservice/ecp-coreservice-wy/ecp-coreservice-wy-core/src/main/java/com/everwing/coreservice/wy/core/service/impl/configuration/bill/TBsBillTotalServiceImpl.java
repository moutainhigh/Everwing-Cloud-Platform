package com.everwing.coreservice.wy.core.service.impl.configuration.bill;

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.CollectionEnum;
import com.everwing.coreservice.common.wy.common.enums.StreamEnum;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.fee.constant.AcChargeDetailBusinessTypeEnum;
import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.constant.LatefeeBusinessTypeEnum;
import com.everwing.coreservice.common.wy.fee.dto.AcChargeDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcLateFeeDto;
import com.everwing.coreservice.common.wy.fee.dto.AcSpecialDetailDto;
import com.everwing.coreservice.common.wy.service.configuration.bill.TBsBillTotalService;
import com.everwing.coreservice.wy.core.mq.MqSender;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import com.everwing.coreservice.wy.core.utils.BillingUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("tBsBillTotalService")
public class TBsBillTotalServiceImpl extends Resources implements TBsBillTotalService{
	
	private static final Logger logger = LogManager.getLogger(TBsBillTotalServiceImpl.class);

	@Value("${queue.wy2wy.wy.billing.koufei.key}")
	private String route_key_koufei_after_audit;
	
	@Value("${queue.wy2wyBilling.wy.manual.key}")
	private String wy_billing_key;					//消息队列 对BillingSupEntity里的数据进行处理
	
	@Value("${queue.wy2Wy.coll.gen.key}")
	private String coll_gen_key;
	
    @Autowired
    private SpringRedisTools springRedisTools;

    @Autowired
    private MqSender mqSender;
	
	private static final String AUTO_GENER = "system";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto findDataPerYear(String companyId,TBsChargeBillTotal entity) {
		return new BaseDto(assembleList(entity), null);
	}
	
	private List<Map<String,Object>> assembleList(TBsChargeBillTotal entity){
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		dataList.add(tBsChargeBillTotalMapper.selectIdPerYear(entity));
		dataList.add(tBsChargeBillTotalMapper.selectCurrentFeePerYear(entity));
//		dataList.add(tBsChargeBillTotalMapper.selectBillingTimePerYear(entity));
		dataList.add(tBsChargeBillTotalMapper.selectLastOwedFeePerYear(entity));
		dataList.add(tBsChargeBillTotalMapper.selectTotalFeePerYear(entity));
		dataList.add(tBsChargeBillTotalMapper.selectChargeTypePerYear(entity));
		dataList.add(tBsChargeBillTotalMapper.selectIsRebillingPerYear(entity));
		dataList.add(tBsChargeBillTotalMapper.selectAuditStatusPerYear(entity));
		return dataList;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto findById(String companyId, String id) {
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(this.tBsChargeBillTotalMapper.selectById(id));
		return returnDto;
	}

	
	/**
	 * @TODO 审核并扣费.
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto audit(String companyId, TBsChargeBillTotal entity) {
		MessageMap msgMap = new MessageMap(MessageMap.INFOR_WARNING, null);
		if(CommonUtils.isEmpty(entity.getId())){
			msgMap.setMessage(" 传入参数为空, 无法继续审核. ");
			logger.warn("总账单审核 : {} ",msgMap.getMessage());
			return new BaseDto(msgMap);
		}
		//这里先去查询总单，看总单的汇总状态，如果是未汇总的状态，则不能做审核
		//1. 判断当前的总账单是否处于待审核状态
		TBsChargeBillTotal currTotal = this.tBsChargeBillTotalMapper.selectById(entity.getId());
		if(BillingEnum.AUDIT_STATUS_COMPELTE.getIntV() .equals(currTotal.getAuditStatus())){
			msgMap.setMessage(" 当前账单处于审核完成状态,无法继续审核");
			logger.warn("总账单审核 : {} ",msgMap.getMessage());
			return new BaseDto(msgMap);
		}
		if(currTotal.getSunStatus().equals(0)){
			msgMap.setMessage(" 当前账单处于未汇总状态,无法继续审核");
			logger.warn("总账单审核 : {} ",msgMap.getMessage());
			return new BaseDto(msgMap);
		}
		
		//2. 起消息队列, 对总账单下的所有账户进行一一扣取
		MqEntity e = new MqEntity();
		e.setData(currTotal);
		e.setCompanyId(companyId);
		logger.info("总账单审核: 将总账单传入消息队列,对总账单下所有的账户进行一一扣取. 数据: {}, route_key: {}",e.toString(),route_key_koufei_after_audit);
		this.amqpTemplate.convertAndSend(route_key_koufei_after_audit, e);   //koufei()
		
		
		//3. 审核总账单
		currTotal.setAuditStatus(BillingEnum.AUDIT_STATUS_COMPELTE.getIntV());
		currTotal.setModifyTime(new Date());
		this.tBsChargeBillTotalMapper.update(currTotal);
		
		//将审核完成的状态放入redis, 保持24小时
		springRedisTools.addData(currTotal.getId(), BillingEnum.AUDIT_STATUS_COMPELTE.getIntV().toString(),60 * 60 * 24,TimeUnit.SECONDS);
		
		//4. 返回响应
		msgMap.setFlag(MessageMap.INFOR_SUCCESS);
		msgMap.setMessage(" 审核完成,后台正在异步对各账户进行扣取,请稍后查看扣费信息. ");
		return new BaseDto(msgMap);
	}

	/**
	 * @TODO 扣费功能:
	 * 新增对新版账户数据的插入和修改
	 * 这里主要需要做几个操作    1.修改收费结果明细表的数据
	 * 				   2 如果发生了专项账户的抵扣， 插入专线抵扣的数据
	 * 				   3.修改当月收费总金额数据表
	 * 				   4.修改上月欠费总金额数据表
	 * 				   5.如果发生了对违约金的抵扣，需要写入滞纳金的抵扣流水,修改滞纳金信息表
	 * 				   6.修改资产账户表
	 *
	 * 	分析：目前系统是不支持专项账户有钱，却又欠费违约金和本金账户欠费的
	 */
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void koufei(String companyId, TBsChargeBillTotal total) {

		if(total == null){
			logger.warn("账户扣费: 传入总账单数据为空,扣费失败.");
			return;
		}
		
		//找到总账单对应的项目信息
		TBsProject paramProject = new TBsProject();
		paramProject.setBillingTime(total.getBillingTime());
		paramProject.setProjectId(total.getProjectId());
		paramProject.setCommonStatus(BillingEnum.STATUS_START.getIntV());
		TBsProject currProject = this.tBsProjectMapper.findByObj(paramProject);
		
		//找到下期的总账单
		TBsChargeBillTotal nextTotal = this.tBsChargeBillTotalMapper.findNextBillTotal(total.getId()); 
		if(CommonUtils.isEmpty(nextTotal)){
			logger.warn("账户扣费: 未找到下期总账单, 扣费失败. 本期账单数据: {}",total.toString());
		}
		
		//找出当前总账单下所有的历史账单
		List<TBsChargeBillHistory> histories = this.tBsChargeBillHistoryMapper.findAllByTotalId(total.getId());
		
		if(CommonUtils.isEmpty(histories)){
			logger.warn("账户扣费: 未找到分账单信息, 扣费失败. 总账单数据:{}",total.toString());
			return;
		}
		
		List<TBsAssetAccount> insertAccountList = new ArrayList<TBsAssetAccount>();					//带插入的账户表
		List<TBsAssetAccount> updateAccountList = new ArrayList<TBsAssetAccount>();					//待更新的账户集合
		List<TBsAssetAccountStream> insertStreamList = new ArrayList<TBsAssetAccountStream>();		//带插入的账户流水集合
		List<TBsOwedHistory> updateOwdedHistories = new ArrayList<TBsOwedHistory>();				//待更新的欠费数据集合
		List<TBsOwedHistory> insertOwedHistories = new ArrayList<TBsOwedHistory>();
		List<TBsChargeBillHistory> updateHistories = new ArrayList<TBsChargeBillHistory>();			//待更新的账单集合
		//新版账户的
		/*List<AcSpecialDetailDto> specialDetailList = new ArrayList<AcSpecialDetailDto>();           //专项账户抵扣流水明细
		List<AcLateFeeDto> lateFeeList = new ArrayList<AcLateFeeDto>();   							//违约金流水
*/		List<AcChargeDetailDto> chargeDetailDtoList  = new ArrayList<>();							//本期账户审核后的一系列的修改操作

		//逐条进行费用的扣减
		double lastOwedFee=0.00;
		for(TBsChargeBillHistory history : histories){
			
			//过滤审核完成状态
			if(history.getAduitStatus() == BillingEnum.AUDIT_STATUS_COMPELTE.getIntV() || null == history.getCurrentBillFee()){
				continue;
			}
			
			TBsChargeBillHistory nextHistory = this.tBsChargeBillHistoryMapper.findNextHistory(history);
			
			//找到当前账单对应的账户,然后从账户内扣减
			TBsAssetAccount account = this.tBsAssetAccountMapper.lookupByBuildCodeAndType(history.getBuildingCode(), total.getType());//已经统一 , 总单的type与账户的type类型一致
			boolean accountIsInsert = false;

			if(account == null){
				//该户不存在账户,直接跳过? 还是创建一个账户?
				account = new TBsAssetAccount();
				account.setId(CommonUtils.getUUID());
				account.setAccountBalance(0.0);
				account.setBuildingCode(history.getBuildingCode());
				account.setCreateId(AUTO_GENER);
				account.setCreateTime(new Date());
				account.setFullName(history.getFullName());
				account.setModifyId(AUTO_GENER);
				account.setModifyTime(new Date());
				account.setProjectId(history.getProjectId());
				account.setType(total.getType());		//已经统一 , 总单的type与账户的type类型一致
				account.setUseStatus(0);
				
				accountIsInsert = true;
			}
			
			double balance = (account.getAccountBalance() >= 0) ? account.getAccountBalance() : 0; 
			
			//本期总金额 = 本期计费 + 分摊金额
			double sumAmount = CommonUtils.null2Double(history.getCurrentFee()) + (history.getShareFee() == null ? 0.0 : history.getShareFee());	//本期费用与本期分摊是必定要扣的
			double canKqAmount = CommonUtils.null2Double(history.getCurrentFee()) + CommonUtils.null2Double(history.getShareFee());  //本期费用 + 本期分摊费用
			double lastOwed = history.getLastBillFee() - history.getLastPayed();	//上期欠费, 要看本期在欠费内扣除了多少
			
			//若上月欠费100 到本月计费时产生违约金本利和 为 105 , 则截止计费时间 , 违约金本利和则为105
			//时间		欠费		 	违约金
			//1月		100			0
			//2月		200			115 (100+15)
			//3月		200			225 (200+25) + 135(100 + 30)
			//4月		200			225 (200+25) + 255(200 + 55) + 160 (100 + 60)
			
			//截止到4月 总共为  200+225+255+160 = 840
			//若缴费 600	则先抵扣违约金  25+ 55 + 60 = 140
			//剩余 460 再从1月开始扣取本金 , 100 余360	本月欠费记录无效
			//扣除2月本金	200		余160			本月欠费记录无效
			//扣除3月本金 200  欠40					本月欠费记录改为40
			//4月在五月开始记录4月的违约金本利和 225

			//获取本账户的所有欠费数据
			List<TBsOwedHistory> ohs = this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
			double kqAmount = 0.0;	//扣取的违约金 + 扣取的账户欠费
			double kqLateFee = 0.0;  //抵扣的总违约金金额
			double kqLateAmount = 0.0;
			double specilLateAmount = 0.0 ;//抵扣违约金的金额
			double specilAmount = 0.0 ;//抵扣本金的金额
			if(balance > 0 && !ohs.isEmpty()){
				//专项账户的扣减之前版本是没有流水的，这里增加进来
				//又做了调整，专项账户已经不存在抵扣了

				double bnc = CommonUtils.null2Double(account.getAccountBalance());  //账户余额
				//首选抵扣违约金
				for(TBsOwedHistory oh : ohs){
					if(bnc <= 0){
						break;
					}
					if(CommonUtils.isEmpty(oh.getOwedAmount()) && CommonUtils.isEmpty(oh.getTotalLateFee())) continue;
					double lateFee = oh.getTotalLateFee();    //单条的违约金扣减值
					//账户余额小于本条数据的违约金, 只扣除余额部分的违约金
					oh.setTotalLateFee((bnc >= lateFee) ? 0.0 : lateFee - bnc);
					kqLateFee += (bnc >= lateFee) ? lateFee : bnc;
					specilLateAmount += (bnc >= lateFee) ? lateFee : bnc;  //抵扣违约金的金额
					bnc -= (bnc >= lateFee) ? lateFee : bnc; //抵扣了违约金，对账户进行扣减

				}

				if( specilLateAmount > 0 ) {
					//插入违约金抵扣
					insertStreamList.add(new TBsAssetAccountStream(CommonUtils.getUUID(),account.getId(),
							-specilLateAmount,new Date(),"system","system","专项账户扣减违约金"));
				}


				//这种情况理论不存在
				if(bnc > 0){
					for(TBsOwedHistory oh : ohs){
						//这里理论上修改状态就行了，不知道为什么要把金额都设置为0，后期查都没法查，这里先不动
						if(bnc <= 0) {break;}
						if(CommonUtils.isEmpty(oh.getOwedAmount()) && CommonUtils.isEmpty(oh.getTotalLateFee())){ continue;}
						double owedAmount = oh.getOwedAmount();
						oh.setIsUsed( (bnc >= owedAmount) ? BillingEnum.IS_USED_STOP.getIntV() : BillingEnum.IS_USED_USING.getIntV());
						//完全够抵扣才设置结束时间
						if(bnc >= owedAmount){
							oh.setOwedEndTime(new Date());
						}
						oh.setOwedAmount( (bnc >= owedAmount) ? 0.0 : owedAmount - bnc );
						kqLateAmount += (bnc >= owedAmount) ? owedAmount : bnc;
//						specilAmount += (bnc >= owedAmount) ? owedAmount : bnc;  //抵扣本金的金额
						bnc -= (bnc >= owedAmount) ? owedAmount : bnc;
						//插入本金扣取流水

					}

//					if( specilAmount > 0 ) {
//						insertStreamList.add(new TBsAssetAccountStream(CommonUtils.getUUID(),account.getId(),-specilAmount,
//								new Date(),"system","system","专项账户扣欠费流水"));
//					}

				}
				updateOwdedHistories.addAll(ohs);
			}
			
//			account.setAccountBalance( CommonUtils.null2Double(account.getAccountBalance()) - kqLateAmount );//扣减完之后体现在账户
//					CommonUtils.null2Double(account.getAccountBalance()) -= kqLateAmount );
			// 这里如果 kqLateFee > 0 说明此账户使用余额对违约金进行了抵扣     对新版账户数据进行******************
			AcLateFeeDto lateFeeDto = new AcLateFeeDto();
			if( specilLateAmount > 0 ) {
				lateFeeDto.setBusinessType(LatefeeBusinessTypeEnum.SPECIAL_ACCOUNT_DEDUCTIBLE_LATE_FEE.getCode() );
				lateFeeDto.setCompanyId( companyId );
				lateFeeDto.setDesc( LatefeeBusinessTypeEnum.SPECIAL_ACCOUNT_DEDUCTIBLE_LATE_FEE.getDesc() );
				lateFeeDto.setHouseCodeNew( account.getBuildingCode() );
				lateFeeDto.setMoney( BigDecimal.valueOf( specilLateAmount ) );
				lateFeeDto.setOperateDetailId( "system" );
				lateFeeDto.setPrincipal( BigDecimal.valueOf( 0 ) );   //本金这里我们给0
				lateFeeDto.setChargingType(ChargingType.getChargingTypeByCode( total.getType() ));
				lateFeeDto.setProjectId( total.getProjectId() );
				lateFeeDto.setProjectName( CommonUtils.isEmpty(currProject) ? "" : currProject.getProjectName() );
			}

			lastOwed -= kqLateAmount;	//上期欠费总共抵扣了多少
			sumAmount += lastOwed + (CommonUtils.null2Double(history.getLateFee()) - kqLateFee);	//现在 : 本期费用 + 本期分摊 + 上期欠费去掉了扣除的 + 违约金扣除剩下的

			//2018-03-01 wsw 在计算违约金的时候 , 已经将违约金从账户中扣除了, 此时审核的时候不需要在扣违约金,否则会造成违约金双倍扣除
//			canKqAmount += (CommonUtils.null2Double(history.getLateFee()));
			kqAmount = (account.getAccountBalance() >= canKqAmount) ? canKqAmount : (account.getAccountBalance() >= 0) ? account.getAccountBalance() : 0;
			if( account.getAccountBalance() > 0 ) {
				specilAmount += account.getAccountBalance() > canKqAmount ? canKqAmount : account.getAccountBalance() ;
			}
			account.setAccountBalance(account.getAccountBalance() - canKqAmount);		//违约金已经在上方扣取部分 , 此处只需要扣取本期账单部分(已经计算了违约金)即可-- 扣减本月
			history.setCurrentKqAmount(canKqAmount);

			//这里对本次计费的抵扣，在新账户应该出现两条数据（一条本次计费的流水-- 计费的时候已经有了，一条专项账户的抵扣流水）
			// 这里如果 specilAmount > 0 说明此账户使用余额对之前的欠费进行了抵扣      对新版账户数据进行******************
			//这里调用专项账户抵扣的消息队列，主要操作是修改专项账户的余额，新增专项账户的抵扣流水(主要需要信息：code，变动金额，业务类型，项目信息，描述,操作人)
			AcSpecialDetailDto specialDetailDto = new AcSpecialDetailDto();
			if( specilAmount > 0 ) {
				//如果发生了抵扣要留下记录
				insertStreamList.add(new TBsAssetAccountStream(account.getId(), specilAmount, "专项账户发生抵扣"));	//扣费
				
				specialDetailDto.setCompanyId(companyId);
//				specialDetailDto.setDeductionDetailId(null);//抵扣明细id，现在提供不了
				specialDetailDto.setLateFeeDeductible( BigDecimal.valueOf( specilLateAmount ) );  //扣减违约金
				specialDetailDto.setMoneyPrincipal( BigDecimal.valueOf( specilAmount ) );     //扣减本金
				specialDetailDto.setDesc( "专项账户发生"+ChargingType.getChargingTypeByCode( total.getType() ) + "抵扣" );
				specialDetailDto.setBusinessTypeEnum( BusinessType.getBusinessTypeByCode(2) );
				specialDetailDto.setProjectId( total.getProjectId() );
				specialDetailDto.setProjectName( CommonUtils.isEmpty(currProject) ? "" : currProject.getProjectName() );
				specialDetailDto.setHouseCodeNew( account.getBuildingCode() );
			}

			//(前面分别插入了违约金和专用账户抵扣的流水)修改本期收费结果明细表
			AcChargeDetailDto acChrgeDetailDto =  new AcChargeDetailDto();
			//新的收费结果明细表
			acChrgeDetailDto.setCompanyId(companyId);
			acChrgeDetailDto.setAuditTime(new Date() );
			acChrgeDetailDto.setSpecialDiKou( BigDecimal.valueOf( specilAmount+specilLateAmount ));//专项账户抵扣金额
			//acChrgeDetailDto.setHouseCodeNew( buildingCodeModifyHoustCode(account.getBuildingCode()));
			acChrgeDetailDto.setLastChargeId( history.getId() );
			acChrgeDetailDto.setSpecialDetailDto( specialDetailDto );
			acChrgeDetailDto.setAcLateFeeDto( lateFeeDto );
			acChrgeDetailDto.setBusinessTypeEnum( AcChargeDetailBusinessTypeEnum.SPECIAL_DK );
			acChrgeDetailDto.setChargingType(ChargingType.getChargingTypeByCode(total.getType()));
			specialDetailDto.setProjectId( total.getProjectId() );
			specialDetailDto.setProjectName( CommonUtils.isEmpty(currProject) ? "" : currProject.getProjectName() );
			chargeDetailDtoList.add(acChrgeDetailDto);
			//****************新版账户相关部分结束********************
			
			//若本项目未开启通用账户扣费, 则在此处就直接生成本期欠费信息
			if(currProject == null && account.getAccountBalance() < 0){
				//若扣除之后少于0 , 则需要记录本期欠费信息 ,
				List<TBsOwedHistory> bohs = this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
				TBsOwedHistory oh = new TBsOwedHistory(account);
				if(CommonUtils.isEmpty(bohs)){
					oh.setOwedAmount(Math.abs(account.getAccountBalance()));
				}else{
					double currOwed = Math.abs(account.getAccountBalance());
					for(TBsOwedHistory o : bohs){
						currOwed = currOwed - o.getOwedAmount() - o.getTotalLateFee();
					}
					oh.setOwedAmount(currOwed);
				}
				insertOwedHistories.add(oh);
			}
			
			history.setAccountBalance(CommonUtils.null2Double(account.getAccountBalance()));
			history.setAduitStatus(BillingEnum.AUDIT_STATUS_COMPELTE.getIntV()); //每一笔扣费完成将分单设置为审核完成状态
			//记录本期的账户扣取  只计算可扣取至0的那一部分
			double ncd = CommonUtils.null2Double(history.getNoCommonDesummoney());
			double cd = CommonUtils.null2Double(history.getCommonDesummoney());
			double cbf = CommonUtils.null2Double(history.getCurrentBillFee());
			ncd = ((cd + ncd + kqAmount) >=  Math.abs(cbf)) ? cbf : (cd + ncd + kqAmount);
			history.setNoCommonDesummoney(ncd);
			
			//下期账单的上期已付
			if(null != nextHistory){
				double lp = CommonUtils.null2Double(nextHistory.getLastPayed());
				double lbf = CommonUtils.null2Double(nextHistory.getLastBillFee());
				lp = ((lp + kqAmount) >= lbf) ? lbf : (lp + kqAmount);
				nextHistory.setLastPayed(lp);
				updateHistories.add(nextHistory);
			}
			
			account.setModifyId(AUTO_GENER);
			account.setModifyTime(new Date());
			account.setModifyName(AUTO_GENER);

			//为防止消息队列异常回滚,账户操作消息队列重复运行,导致重复扣减,现不再批量投递,随主线程操作而操作  20180208 wsw
			if(accountIsInsert){
				insertAccountList.add(account);
			}else{
				updateAccountList.add(account);
			}
			updateHistories.add(history);
//			this.tBsChargeBillHistoryMapper.updateBillHistory(history);
//			this.tBsChargeBillHistoryMapper.updateBillHistory(nextHistory);
			
			
			//账户扣减玩之后，计算总账单还欠费多少记录到下期总账单的上期欠费;只记录账户是负数的情况
			double accountBanlance = account.getAccountBalance();
			if(accountBanlance < 0){
				lastOwedFee = lastOwedFee+accountBanlance;
			}
			nextTotal.setLastOwedFee(Double.parseDouble(String.valueOf(lastOwedFee).replace("-", "")));
			this.tBsChargeBillTotalMapper.update(nextTotal);
			//扣减账户内的余额,扣取多少 就需要计入账户流水
			if(canKqAmount != 0){
				insertStreamList.add(new TBsAssetAccountStream(account.getId(), -canKqAmount, StreamEnum.purpose_billing.getV()));	//扣费
			}
			
			if(updateAccountList.size() >= BATCH_UPDATE_COUNT){
				BillingUtils.sendUpdateList(updateAccountList, wy_billing_key, companyId, amqpTemplate);
			}
			
			if(insertAccountList.size() >= BATCH_ADD_COUNT){
				BillingUtils.sendInsertList(insertAccountList, wy_billing_key, companyId, amqpTemplate);
			}
			
			if(insertStreamList.size() >= BATCH_ADD_COUNT){
				BillingUtils.sendInsertList(insertStreamList, wy_billing_key, companyId, amqpTemplate);
			}
			
			if(updateHistories.size() >= BATCH_UPDATE_COUNT){
				BillingUtils.sendUpdateList(updateHistories, wy_billing_key, companyId, amqpTemplate);
			}
			
			if(updateOwdedHistories.size() >= BATCH_UPDATE_COUNT){
				BillingUtils.sendUpdateList(updateOwdedHistories, wy_billing_key, companyId, amqpTemplate);
			}
			
			if(insertOwedHistories.size() >= BATCH_ADD_COUNT){
				BillingUtils.sendInsertList(insertOwedHistories, wy_billing_key, companyId, amqpTemplate);
			}

			//新版账户的数据
			if(chargeDetailDtoList.size() >= BATCH_ADD_COUNT){
				mqSender.sendAcCurrentChargeForUpdate( chargeDetailDtoList,companyId);
				chargeDetailDtoList.clear();
			}

		}
		

		if(!updateAccountList.isEmpty()){
			BillingUtils.sendUpdateList(updateAccountList, wy_billing_key, companyId, amqpTemplate);
		}
		if(!insertAccountList.isEmpty()){
			BillingUtils.sendInsertList(insertAccountList, wy_billing_key, companyId, amqpTemplate);
		}
		if(!insertStreamList.isEmpty()){
			BillingUtils.sendInsertList(insertStreamList, wy_billing_key, companyId, amqpTemplate);
		}
		if(!updateHistories.isEmpty()){
			BillingUtils.sendUpdateList(updateHistories, wy_billing_key, companyId, amqpTemplate);
		}
		if(!updateOwdedHistories.isEmpty()){
			BillingUtils.sendUpdateList(updateOwdedHistories, wy_billing_key, companyId, amqpTemplate);
		}
		//新版账户的数据
		if(chargeDetailDtoList.size() > 0) {
            mqSender.sendAcCurrentChargeForUpdate(chargeDetailDtoList, companyId);
            chargeDetailDtoList.clear();
        }
		if(!insertOwedHistories.isEmpty()){
			BillingUtils.sendInsertList(insertOwedHistories, wy_billing_key, companyId, amqpTemplate);
		}
		
		
		if(currProject == null){
			//未开启通用账户扣费, 直接生成托收数据
			// genColl(total.getProjectId(), total.getId() , companyId);
		}
		
		
	}

	
	/**
	 * @TODO 按项目进行托收数据生成
	 * @param projectId
	 */
	private void genColl(String projectId, String totalId, String companyId){
		TBcProject bcProject = this.tBcProjectMapper.findByProjectId(projectId);
		
		if(null != bcProject && bcProject.getStatus() == CollectionEnum.status_on.getV()){
			logger.info("本体基金通用账户扣取: 组装数据, 准备投递至托收文件生成. ");
			MqEntity me = new MqEntity();
			me.setProjectId(bcProject.getProjectId());
			me.setProjectName(bcProject.getProjectName());
			me.setData(bcProject);
			me.setSupAttr(totalId);
			me.setCompanyId(companyId);
			me.setOpr(CollectionEnum.common_account_stop.getV());
			this.amqpTemplate.convertAndSend(coll_gen_key, me);
			logger.info("本体基金通用账户扣取: 组装数据完成, 投递至托收文件生成  完成. routeKey:{}, 数据:{}. ", coll_gen_key, me.toString());
			//投递至 TBcCollectionService.genColl
		}
	}

//	/**buildingCode 转化成houseCode
//	 * @param buildingCodes
//	 * @return
//	 */
//	public List<String> modifyHousetCode(List<String> buildingCodes){
//		List<String>houseCodes=new ArrayList<>();
//		for (String buildingCode:buildingCodes){
//			String houseCode=this.tcBuildingMapper.getHouseCodeByBuildingCode(buildingCode);
//			houseCodes.add(houseCode);
//		}
//
//		return houseCodes;
//	}
//
//	/**
//	 * buildingCode 转化成houseCode
//	 * @param buildingCode
//	 * @return
//	 */
//
//
//	public String buildingCodeModifyHoustCode(String buildingCode){
//
//		return this.tcBuildingMapper.getHouseCodeByBuildingCode(buildingCode);
//	}
}
