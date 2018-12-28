
package com.everwing.coreservice.wy.core.service.impl.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.BigDecimalUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.SettlementEnum;
import com.everwing.coreservice.common.wy.entity.delivery.TJgAccountReceivable;
import com.everwing.coreservice.common.wy.entity.delivery.TJgGiveAccountDetail;
import com.everwing.coreservice.common.wy.entity.delivery.TJgTotalCalculation;
import com.everwing.coreservice.common.wy.service.delivery.TJgTotalCalculationService;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgAccountReceivableMapper;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgDepositReceiptMapper;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgGiveAccountDetailMapper;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgTotalCalculationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * @describe 总结算表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Service("tJgTotalCalculationServiceImpl")
public class TJgTotalCalculationServiceImpl implements TJgTotalCalculationService{

	
	@Autowired
	private TJgTotalCalculationMapper tJgTotalCalculationMapper;
	@Autowired
	private TJgAccountReceivableMapper tJgAccountReceivableMapper;
	@Autowired
	private TJgGiveAccountDetailMapper tJgGiveAccountDetailMapper;
	@Autowired
	private TJgDepositReceiptMapper tJgDepositReceiptMapper;
	
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap addTotalCalculation(String companyId, TJgTotalCalculation entity) {
		// TODO 汇总单个角色的所有明细，生成一条总结算数据
		if(CommonUtils.isEmpty(entity)) {
			return new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空!");
		}
		//1.组装数据，金额类数据能用从前台传过来的值，后台获取
		TJgTotalCalculation totalCalculatino=assembleTotalCalculation(entity);
		if(CommonUtils.isEmpty(totalCalculatino)) return new MessageMap(MessageMap.INFOR_ERROR, "没有可用的结算数据");
		
		//2.组装完后修改收账信息表中数据为已结算,回写total_id
		TJgAccountReceivable accountR=new TJgAccountReceivable();
		accountR.setOprId(entity.getOprId());
		accountR.setProjectId(entity.getProjectId());
		accountR.setTotalId(entity.getId());
		tJgAccountReceivableMapper.updateStasusAndTotalId(accountR);
		
		//3.回写交账明细中的total_id
		TJgGiveAccountDetail giveAccount=new TJgGiveAccountDetail();
		giveAccount.setOprId(entity.getOprId());
		giveAccount.setProjectId(entity.getProjectId());
		giveAccount.setTotalId(entity.getId());
		tJgGiveAccountDetailMapper.updateTotalId(giveAccount);
		
		this.tJgTotalCalculationMapper.addTotalCalculation(entity);
		
		MessageMap msg=new MessageMap(MessageMap.INFOR_SUCCESS,"结算成功!");
//		this.tJgTotalCalculationMapper.addTotalCalculation(entity);
		return msg;
	}
	
	
	private TJgTotalCalculation assembleTotalCalculation(TJgTotalCalculation entity) {

		entity.setId(CommonUtils.getUUID());
		TJgAccountReceivable accountR=new TJgAccountReceivable();
		String totalNum=TJgAccountReceivableServiceImpl.getTradNo(entity.getTotalNum(),SettlementEnum.BUSINESS_ACCOUNT_TOTAL.getIntValue().toString());
		entity.setTotalNum(totalNum);
		accountR.setOprId(entity.getOprId());
		accountR.setProjectId(entity.getProjectId());
		Map<String, Double> result=tJgAccountReceivableMapper.sumNotGavenAmountInfo(accountR);//金额的汇总信息--来自收账
		
		//支出一样需要结算
//		double sumMoney=result.get("payWxTotal") + result.get("payUnionTotal") + result.get("payCashTotal") + result.get("bankReceipts") 
//							- result.get("givenAmount") - result.get("paybackAmount");
//		if(sumMoney <= 0) return null;
		
		//现金(涉及到现金的地方：收钱，交账，支出)


		//后面做出了修改，所有的收费方式都会涉及到支出，也就是交账要全部考虑支出的情况,而不只是现金才有支出了   2018-11-22
		double sumCashMoney = BigDecimalUtils.add(result.get("totalCashIncome"), result.get("otherCashTotal"));//收入的总金额
		//现金总计 = 收入  - 支出
		sumCashMoney = BigDecimalUtils.sub(sumCashMoney,result.get("totalCashPayBack"));
		
		entity.setCashTotal(sumCashMoney);
		entity.setCashGaven(result.get("givenAmount") + result.get("otherCashTotal") );
		entity.setCashNotGive(sumCashMoney - result.get("givenAmount") - result.get("otherCashTotal"));
		//wx
		entity.setWxTotal(result.get("totalWxIncome")- result.get("totalWxPayBack"));
		entity.setWxGaven(0.0);
		entity.setWxNotGive(result.get("totalWxIncome")- result.get("totalWxPayBack"));
		//union
		entity.setUnionTotal(result.get("totalUnionIncome")- result.get("totalUnionPayBack"));
		entity.setUnionGaven(0.0);
		entity.setUnionNotGive(result.get("totalUnionIncome")- result.get("totalUnionPayBack"));
		//银行收款
		entity.setBankReceiptsTotal(result.get("totalBankIncome")- result.get("totalBankPayBack"));
		entity.setBankReceiptsGaven(0.0);
		entity.setBankReceiptsNotGive(result.get("totalBankIncome")- result.get("totalBankPayBack"));
		//支付宝
		entity.setAlipayTotal(result.get("totalAlipayIncome")- result.get("totalAlipayBack"));
		entity.setAlipayGaven(0.0);
		entity.setAlipayNotGive(result.get("totalAlipayIncome")- result.get("totalAlipayBack"));
		return entity;
		
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageTotalCaculation(String companyId, TJgTotalCalculation entity) {
		// 查询结算明细
		BaseDto baseDto=new BaseDto<>();
		List<TJgTotalCalculation> resutlList=this.tJgTotalCalculationMapper.listPageTotalCaculation(entity);
		baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		baseDto.setLstDto(resutlList);
		baseDto.setPage(entity.getPage());
		return baseDto;
	}


	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap returnOrConfirmTotalInfo(String companyId, TJgTotalCalculation entity, String ids) {
		// 审核结算信息
		if(CommonUtils.isEmpty(entity.getProjectId()) || CommonUtils.isEmpty(entity.getReceiveId()) || CommonUtils.isEmpty(entity.getStatus())) {
			return new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空");
		}
		if(CommonUtils.isEmpty(ids)) {
			return new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空");
		}

		List<String> list= CommonUtils.str2List(ids, ",");
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("status", entity.getStatus());
		paramMap.put("receiveId", entity.getReceiveId());
		paramMap.put("projectId", entity.getProjectId());
		paramMap.put("list", list);
		//退回和收账的操作是不同的
		if( 2 == entity.getStatus()) {
			//1.修改相关的收账信息为未结算   2修改结算账单的状态为退回，可进行重新结算
			this.tJgAccountReceivableMapper.updateStatusBatch(list);
			//2.置空交账记录的total——id
			this.tJgGiveAccountDetailMapper.updateStatusBatchsForReturn(list);
			this.tJgTotalCalculationMapper.returnOrConfirmTotalInfo(paramMap);
			return new MessageMap(MessageMap.INFOR_SUCCESS,"退回操作成功");
		}else if(3 == entity.getStatus()) {
			//1.修改相关的现金收账信息为已确认      2，修改结算账单信息为已确认
			this.tJgGiveAccountDetailMapper.updateStatusBatchs(list);
			this.tJgTotalCalculationMapper.returnOrConfirmTotalInfo(paramMap);
			return new MessageMap(MessageMap.INFOR_SUCCESS,"确认操作成功");
		}
		return new MessageMap(MessageMap.INFOR_ERROR,"操作异常");
	}


	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto summaryAccountForInfo(String companyId, TJgTotalCalculation entity) {
		// 查询可用于进行交账操作的结算汇总信息
		if(CommonUtils.isEmpty(entity) || CommonUtils.isEmpty(entity.getReceiveId()) || CommonUtils.isEmpty(entity.getProjectId())) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空"));
		}
		Map<String, Double> resultMap=this.tJgTotalCalculationMapper.summaryAccountForInfo(entity);
		
		//这里的现金总额包含了系统和其他。这里把两者都区分出来
		double otherCash  = this.tJgTotalCalculationMapper.getSysCashInfo(entity);
		resultMap.put("otherCash",otherCash);
		resultMap.put("cashSys", resultMap.get("cashTotal") - otherCash );
		
		BaseDto baseDto=new BaseDto<>();
		baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"确认操作成功"));
		baseDto.setObj(resultMap);
		return baseDto;
	}


	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto listPageSelfTotalInfo(String companyId, TJgTotalCalculation entity) {
		// 查询自己的交账汇总信息
		if(CommonUtils.isEmpty(entity) || CommonUtils.isEmpty(entity.getOprId()) || CommonUtils.isEmpty(entity.getProjectId())) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空"));
		}
		List<TJgTotalCalculation> resultList=this.tJgTotalCalculationMapper.listPageTotalCaculationForSelf(entity);
		BaseDto baseDto=new BaseDto<>();
		baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		baseDto.setPage(entity.getPage());
		baseDto.setObj(resultList);
		return baseDto;
	}


    /***
     * 这里收银组长和出纳共用一个交账方法  type 1:收银组长   2：出纳
     * 收银员进行结算是 汇总   收账列表数据  审核通过的
     * 收银组长的汇总则来自各个收银员的结算汇总数据   审核通过的
     * 1.找出所有收银员审核通过的汇总数据，进行聚合一条total数据
     * 2.修改存单号的total——id
     * 3.修改关联汇总单条数据的total——id
     * 4.修改每个收银员收账记录的next——total——id（此步暂时不做，和之前设计有调整）
     * 5.插入total表
     */
	@Override
	public MessageMap giveAnccountSummarForLeader(String companyId, TJgTotalCalculation entity,int type) {
		// 收银组长进行交账汇总操作

		TJgTotalCalculation totalCalculatino=getTotalCalculation(entity);
		
		if(CommonUtils.isEmpty(totalCalculatino)) return new MessageMap(MessageMap.INFOR_ERROR, "没有可用的交账数据");
		
		if(1 == type) {
			//如果是组长则需要回写存单的total——id
			this.tJgDepositReceiptMapper.updateTotalId(entity.getId(),entity.getOprId(),entity.getProjectId());
		}
		
		//这里出纳会有一个问题是：两个项目是同一个出纳但是不是同一个会计，增加项目限制
		this.tJgTotalCalculationMapper.updateTotalCaculation(entity.getId(), entity.getOprId(),entity.getProjectId());
		
		this.tJgTotalCalculationMapper.addTotalCalculation(entity);
		
		MessageMap msg=new MessageMap(MessageMap.INFOR_SUCCESS,"结算成功!");
		
		return msg;
	}

	
	private TJgTotalCalculation getTotalCalculation(TJgTotalCalculation entity) {

		entity.setId(CommonUtils.getUUID());
		String totalNum=TJgAccountReceivableServiceImpl.getTradNo(entity.getTotalNum(),SettlementEnum.BUSINESS_ACCOUNT_TOTAL_ZZ.getIntValue().toString());
		entity.setTotalNum(totalNum);
		
		
		Map<String, Double> result=this.tJgTotalCalculationMapper.summaryAccountForInfo(entity);//金额的汇总信息--来自每个收银员的汇总
		double sumMoney=result.get("cashTotal") + result.get("wxTotal") + result.get("unionTotal") - result.get("cashGaven");
		
		//现金
		entity.setCashTotal(result.get("cashTotal"));
		entity.setCashGaven(result.get("cashGaven"));
		entity.setCashNotGive(result.get("cashTotal") - result.get("cashGaven"));
		//wx
		entity.setWxTotal(result.get("wxTotal"));
		entity.setWxGaven(0.0);
		entity.setWxNotGive(result.get("wxTotal"));
		//union
		entity.setUnionTotal(result.get("unionTotal"));
		entity.setUnionGaven(0.0);
		entity.setUnionNotGive(result.get("unionTotal"));
		
		//支付宝
		entity.setAlipayTotal(result.get("alipayTotal"));
		entity.setAlipayGaven(0.0);
		entity.setAlipayNotGive(result.get("alipayTotal"));
		
		//银行收款
		entity.setBankReceiptsTotal(result.get("bankReceiptsTotal"));
		entity.setBankReceiptsGaven(0.0);
		entity.setBankReceiptsNotGive(result.get("bankReceiptsTotal"));
		
		return entity;
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageSettlementDetails(String companyId, TJgTotalCalculation entity,String type) {
		// 交账后根据totalid查询所属的结算集合
		if(CommonUtils.isEmpty(entity) || CommonUtils.isEmpty(type)) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空"));
		}
		List<TJgTotalCalculation> resultList=new ArrayList<>();
		if("1".equals(type)) {
			//交账后查看结算集合信息,使用totalId
			resultList=this.tJgTotalCalculationMapper.listPageSettlementDetails(entity);
		}else if("2".equals(type)) {
			resultList=this.tJgTotalCalculationMapper.listPageSettlementDetailsByOpr(entity);
		}
		BaseDto baseDto=new BaseDto<>(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
        baseDto.setLstDto(resultList);
        baseDto.setPage(entity.getPage());
        return baseDto;
	}

	/**
	 * 出纳进行退回操作和组长不同，需要涉及的表不同
	 */
	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap returnOrConfirmTotalInfoCN(String companyId, TJgTotalCalculation entity, String ids) {
		// 审核结算信息
		if(CommonUtils.isEmpty(entity.getProjectId()) || CommonUtils.isEmpty(entity.getReceiveId()) || CommonUtils.isEmpty(entity.getStatus())) {
			return new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空");
		}
		if(CommonUtils.isEmpty(ids)) {
			return new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空");
		}

		List<String> list= CommonUtils.str2List(ids, ",");
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("status", entity.getStatus());
		paramMap.put("receiveId", entity.getReceiveId());
		paramMap.put("projectId", entity.getProjectId());
		paramMap.put("list", list);
		//退回和收账的操作是不同的
		if( 2 == entity.getStatus()) {
			//修改收银组长下收银员的结算信息为未交账   置空total——id   
			this.tJgTotalCalculationMapper.changeStatusByTotalId(list);
			//修改存单表中关联total——id为这些单的关联关系
			this.tJgDepositReceiptMapper.updateTotalIdById(list);
			//修改组长的总交账信息为退回
			this.tJgTotalCalculationMapper.returnOrConfirmTotalInfo(paramMap);
			return new MessageMap(MessageMap.INFOR_SUCCESS,"退回操作成功");
		}else if(3 == entity.getStatus()) {
			//直接修改汇总交账数据为已确认
			this.tJgTotalCalculationMapper.returnOrConfirmTotalInfo(paramMap);
			return new MessageMap(MessageMap.INFOR_SUCCESS,"确认操作成功");
		}
		return new MessageMap(MessageMap.INFOR_ERROR,"操作异常");
	}


	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap returnOrConfirmTotalInfoKJ(String companyId, TJgTotalCalculation entity, String ids) {
		// 会计审核出纳的交账
		// 审核结算信息
		if(CommonUtils.isEmpty(entity.getProjectId()) || CommonUtils.isEmpty(entity.getReceiveId()) || CommonUtils.isEmpty(entity.getStatus())) {
			return new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空");
		}
		if(CommonUtils.isEmpty(ids)) {
			return new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空");
		}

		List<String> list= CommonUtils.str2List(ids, ",");
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("status", entity.getStatus());
		paramMap.put("receiveId", entity.getReceiveId());
		paramMap.put("projectId", entity.getProjectId());
		paramMap.put("list", list);
		//退回和收账的操作是不同的
		if( 2 == entity.getStatus()) {
			//修改收银组长下收银员的结算信息为未交账   置空total——id   
			this.tJgTotalCalculationMapper.changeStatusByTotalId(list);
			//修改组长的总交账信息为退回
			this.tJgTotalCalculationMapper.returnOrConfirmTotalInfo(paramMap);
			return new MessageMap(MessageMap.INFOR_SUCCESS,"退回操作成功");
		}else if(3 == entity.getStatus()) {
			//直接修改汇总交账数据为已确认
			this.tJgTotalCalculationMapper.returnOrConfirmTotalInfo(paramMap);
			return new MessageMap(MessageMap.INFOR_SUCCESS,"确认操作成功");
		}
		return new MessageMap(MessageMap.INFOR_ERROR,"操作异常");
	}
	
	
	
}
