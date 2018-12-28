package com.everwing.coreservice.wy.core.service.impl.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.SettlementEnum;
import com.everwing.coreservice.common.wy.entity.delivery.TJgGiveAccountDetail;
import com.everwing.coreservice.common.wy.service.delivery.TJgGiveAccountDetailService;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgGiveAccountDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * @describe 银账交割交账明细表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Service("tJgGiveAccountDetailServiceImpl")
public class TJgGiveAccountDetailServiceImpl implements TJgGiveAccountDetailService{
	
	@Autowired
	private TJgGiveAccountDetailMapper tJgGiveAccountDetailMapper;

	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap addGiveAccountDetail(String companyId, TJgGiveAccountDetail entity) {
		// TODO 插入一条交账信息
		MessageMap msg=new MessageMap();
		if(CommonUtils.isEmpty(entity)) {
			msg.setFlag(MessageMap.INFOR_ERROR);
			msg.setMessage("请求参数不能为空");
			return msg;
		}
		entity.setId(CommonUtils.getUUID());
		//获取交易编号
		String oprNum= TJgAccountReceivableServiceImpl.getTradNo(entity.getOprNum(), String.valueOf(SettlementEnum.BUSINESS_ACCOUNT_GIVE.getIntValue()));
		entity.setOprNum(oprNum);
		this.tJgGiveAccountDetailMapper.addGiveAccountDetail(entity);
		msg.setFlag(MessageMap.INFOR_SUCCESS);
		msg.setMessage("交账成功");
		return msg;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageGiveCashInfo(String companyId,TJgGiveAccountDetail entity) {
		// 查询交账记录
		MessageMap msg=new MessageMap(); 
		List<TJgGiveAccountDetail> resultList=this.tJgGiveAccountDetailMapper.listPageGiveCashInfo(entity);
		BaseDto baseDto=new BaseDto<>();
		baseDto.setLstDto(resultList);
		msg.setFlag(MessageMap.INFOR_SUCCESS);
		baseDto.setMessageMap(msg);
		baseDto.setPage(entity.getPage());
		return baseDto; 
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageReceiveCashInfo(String companyId, TJgGiveAccountDetail entity) {
		// 查询交账记录
				MessageMap msg=new MessageMap(); 
				List<TJgGiveAccountDetail> resultList= new ArrayList<>();
				resultList = this.tJgGiveAccountDetailMapper.listPageReceiveCashInfo(entity);
				BaseDto baseDto=new BaseDto<>();
				baseDto.setLstDto(resultList);
				msg.setFlag(MessageMap.INFOR_SUCCESS);
				baseDto.setMessageMap(msg);
				baseDto.setPage(entity.getPage());
				return baseDto; 
	}

	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap returnOrConfirmGiveInfo(String companyId, TJgGiveAccountDetail entity, String ids) {
		// 审核交账信息
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
		this.tJgGiveAccountDetailMapper.returnOrConfirmGiveInfo(paramMap);
		if( 2 == entity.getStatus()) {
			return new MessageMap(MessageMap.INFOR_SUCCESS,"退回操作成功");
		}else if(3 == entity.getStatus()) {
			return new MessageMap(MessageMap.INFOR_SUCCESS,"确认操作成功");
		}
		return new MessageMap(MessageMap.INFOR_ERROR,"操作异常");
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageGiveAccountByTotalId(String companyId, TJgGiveAccountDetail entity) {
		//查询单条结算总单的交账明细
		if(CommonUtils.isEmpty(entity) || CommonUtils.isEmpty(entity.getTotalId())) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空"));
		}
		List<TJgGiveAccountDetail> resultList=this.tJgGiveAccountDetailMapper.listPageGiveAccountByTotalId(entity);
		BaseDto baseDto=new BaseDto<>();
		baseDto.setLstDto(resultList);
		baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		baseDto.setPage(entity.getPage());
		return baseDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageAccountReceivables(String companyId, TJgGiveAccountDetail entity,String type) {
		// 出纳查询收账记录
		if(CommonUtils.isEmpty(entity) || CommonUtils.isEmpty(entity.getTotalId()) || CommonUtils.isEmpty(type)) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空"));
		}
		List<TJgGiveAccountDetail> resultList=new ArrayList<>();
		if("1".equals(type)) {
			//交账后查看结算集合信息,使用totalId
			resultList=this.tJgGiveAccountDetailMapper.listPageAccountReceivables(entity);
		}else if("2".equals(type)) {
			resultList=this.tJgGiveAccountDetailMapper.listPageAccountReceivableByOpr(entity);
		}
		BaseDto baseDto=new BaseDto<>(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		baseDto.setLstDto(resultList);
		baseDto.setPage(entity.getPage());
		return baseDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageGiveAccountForCN(String companyId, TJgGiveAccountDetail entity) {
		// 出纳查询自己待交账的现金收账信息
		if(CommonUtils.isEmpty(entity) || CommonUtils.isEmpty(entity.getReceiveId()) || CommonUtils.isEmpty(entity.getProjectId())) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空"));
		}
		List<TJgGiveAccountDetail> resultList=this.tJgGiveAccountDetailMapper.listPageGiveAccountForCN(entity);
		BaseDto baseDto=new BaseDto<>(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		baseDto.setLstDto(resultList);
		baseDto.setPage(entity.getPage());
		return baseDto;
	}
	
	
	
	
	
	
}
