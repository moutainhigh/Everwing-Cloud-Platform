package com.everwing.coreservice.wy.core.service.impl.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.BigDecimalUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.SettlementEnum;
import com.everwing.coreservice.common.wy.entity.delivery.TJgAccountReceivable;
import com.everwing.coreservice.common.wy.service.delivery.TJgAccountReceivableService;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgAccountReceivableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/***
 * @describe 收账明细表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Service("tJgAccountReceivableServiceImpl")
public class TJgAccountReceivableServiceImpl implements TJgAccountReceivableService{
	
	@Autowired
	private TJgAccountReceivableMapper tJgAccountReceivableMapper;

	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap addAccountReceivable(String companyId, TJgAccountReceivable entity) {
		// TODO 新增一条收账明细
		if(CommonUtils.isEmpty(entity)) {
			return new MessageMap(MessageMap.INFOR_ERROR,"请求参数不可为空");
		}
		entity.setId(CommonUtils.getUUID());
		//这里用TradNo存放当前登录用户的statff_number用于组装收账单号
		
		if(CommonUtils.isEmpty( entity.getBusinessType() )) {
			entity.setBusinessType(1);
		}
		
		String type= String.valueOf(SettlementEnum.BUSINESS_ACCOUNT_RECEIVABLE.getIntValue());
		
		if( CommonUtils.isNotEmpty( entity.getBusinessType() ) && 3 == entity.getBusinessType() ) {
			//退押金
			type = String.valueOf(SettlementEnum.BUSINESS_ACCOUNT_TOTAL_DEPOSIT_BACK.getIntValue());
		}
		if( CommonUtils.isNotEmpty( entity.getBusinessType() ) && 2 == entity.getBusinessType() ) {
			//产品
			type = String.valueOf(SettlementEnum.BUSINESS_ACCOUNT_TOTAL_PRODUCT.getIntValue());
		}
		entity.setTradNo(getTradNo(entity.getTradNo(), type));
		MessageMap msg=new MessageMap();
		this.tJgAccountReceivableMapper.addAccountReceivable(entity);
		msg.setFlag(MessageMap.INFOR_SUCCESS);
		msg.setMessage("新增收账明细成功！");
		return msg;
	}
	
	/**
	 * @describe 得到一个收账的交易流水号
	 * @param userId 用户工号
	 * @param type 业务类型,目前有收账和交账
	 * @return
	 */
	public static String getTradNo(String userId,String type) {
		// 当前时间的时分秒 + 用户的staff_number 
		StringBuilder tranNo=new StringBuilder();
		Calendar calendar = Calendar.getInstance();  
	    int year = calendar.get(Calendar.YEAR);  
	    tranNo.append(""+year);
	    int month = calendar.get(Calendar.MONTH) + 1;  
	    if(month < 10) {
	    	tranNo.append("0"+month);
	    }else {
	    	tranNo.append(""+month);
	    }
	    int day = calendar.get(Calendar.DAY_OF_MONTH);  
	    if(day < 10) {
	    	tranNo.append("0"+day);
	    }else {
	    	tranNo.append(""+day);
	    }
	    int hour = calendar.get(Calendar.HOUR_OF_DAY);
	    if(hour < 10) {
	    	tranNo.append("0"+hour);
	    }else {
	    	tranNo.append(""+hour);
	    }
	    int minute = calendar.get(Calendar.MINUTE);  
	    if(minute < 10) {
	    	tranNo.append("0"+minute);
	    }else {
	    	tranNo.append(""+minute);
	    }
	    int second = calendar.get(Calendar.SECOND);  
	    if(second < 10) {
	    	tranNo.append("0"+second);
	    }else {
	    	tranNo.append(""+second);
	    }
	    tranNo.append(type);
	    tranNo.append(userId);
		return tranNo.toString();
	}

	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	@Override
	public BaseDto listPageAcountReceiveInfo(String companyId, TJgAccountReceivable entity) {
		MessageMap msg;
		List<TJgAccountReceivable> resultList=this.tJgAccountReceivableMapper.listPageAcountReceiveInfo(entity);
		BaseDto baseDto=new BaseDto();
		baseDto.setPage(entity.getPage());
		baseDto.setLstDto(resultList);
		msg=new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功");
		return baseDto;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto sumPaymentInfo(String companyId, TJgAccountReceivable entity) {
		//查询汇总信息
		Map<String, Double> resultMap= this.tJgAccountReceivableMapper.sumPaymentInfo(entity);
		//这里添加一个查询支出费用（总额可以按照各种任意组合查询，所以支出额度也需要这样）
//		Double cashIn =  tJgAccountReceivableMapper.sumPaymentInfoCashIn(entity);
//		Double cashOut =  tJgAccountReceivableMapper.sumPaymentInfoCashOut(entity);
//		Double subCash = BigDecimalUtils.sub(cashIn, cashOut);
//		Double totalPayTypeAmount = BigDecimalUtils.add(resultMap.get("payWxToal"),resultMap.get("payUnionTotal"),resultMap.get("bankReceiptsTotal"),resultMap.get("alipayTtotal"));
//		Double total = BigDecimalUtils.add(totalPayTypeAmount, subCash);
//		resultMap.put("subCash", subCash);
//		resultMap.put("totalPayTypeAmount", total);
		double totalAmount = 0.0;
		if(CommonUtils.isNotEmpty(resultMap)){
			totalAmount = BigDecimalUtils.add(
					resultMap.get("wxTotal"),
					resultMap.get("cashTotal"),
					resultMap.get("unionTotal"),
					resultMap.get("alipayTotal"),
					resultMap.get("bankTotal")
			);

		}
		resultMap.put("totalAmount", totalAmount);
		BaseDto baseDto=new BaseDto<>();
		baseDto.setObj(resultMap);
		return baseDto;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto sumNotGavenAmountInfo(String companyId, TJgAccountReceivable entity) {
		// TODO Auto-generated method stub
		Map<String, Double> resultMap= this.tJgAccountReceivableMapper.sumNotGavenAmountInfo(entity);
		//这里修改了直接就可以拿到，不用再计算了
//		if( CommonUtils.isNotEmpty(resultMap) ) {
//			double total = CommonUtils.null2Double( resultMap.get("totalAmount") ) ;
//			double payback = CommonUtils.null2Double( resultMap.get("paybackAmount") ) ;
//			double otherAmount  =  CommonUtils.null2Double( resultMap.get("otherCashTotal") ) ;
//			resultMap.put("totalAmount", BigDecimalUtils.sub(total, payback));
//			resultMap.put("withOtherCashTotal", BigDecimalUtils.add(total, otherAmount));
//		}
		BaseDto baseDto=new BaseDto<>();
		baseDto.setObj(resultMap);
		return baseDto;
	}

	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto getNotGivenCashAmount(String companyId, String userId,String projectId) {
		//查询当前员工所有为交账的现金总额
		Double notGaven=this.tJgAccountReceivableMapper.getNotGivenCashAmount(userId,projectId);
		BaseDto baseDto=new BaseDto<>();
		baseDto.setObj(notGaven);
		baseDto.setMessageMap( new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		return baseDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageNotGivenInfos(String companyId, TJgAccountReceivable entity) {
		// 分页查询本周起还未结算的账单信息
		List<Map<String, String>> resultList=this.tJgAccountReceivableMapper.listPageNotGivenInfos(entity);
		BaseDto baseDto=new BaseDto<>();
		baseDto.setLstDto(resultList);
		baseDto.setPage(entity.getPage());
 		return baseDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageReceiveByTotalId(String companyId, TJgAccountReceivable entity) {
		// 分页查询结算账单的收账明细
		List<TJgAccountReceivable> resultList=this.tJgAccountReceivableMapper.listPageReceiveByTotalId(entity);
		BaseDto baseDto=new BaseDto<>(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		baseDto.setLstDto(resultList);
		baseDto.setPage(entity.getPage());
		return baseDto;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageAccountReceiveForKJ(String companyId, TJgAccountReceivable entity) {
		// 会计查看未交账的信息
		List<TJgAccountReceivable> resultList=this.tJgAccountReceivableMapper.listPageAccountReceiveForKJ(entity);
		BaseDto baseDto=new BaseDto<>(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		baseDto.setLstDto(resultList);
		baseDto.setPage(entity.getPage());
		return baseDto;
	}
	

	
}
