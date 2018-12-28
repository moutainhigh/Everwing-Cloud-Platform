package com.everwing.coreservice.wy.core.service.impl.delivery;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.delivery.TJgDepositReceipt;
import com.everwing.coreservice.common.wy.service.delivery.TJgDepositReceiptService;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgDepositReceiptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/***
 * @describe 存单表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Service("tJgDepositReceiptServiceImple")
public class TJgDepositReceiptServiceImple implements TJgDepositReceiptService{

	@Autowired
	private TJgDepositReceiptMapper tJgDepositReceiptMapper;
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap addDepositReceipt(String companyId, TJgDepositReceipt entity) {
		// TODO 插入一条存单信息
		MessageMap msg=new MessageMap();
		entity.setId(CommonUtils.getUUID());
		this.tJgDepositReceiptMapper.addDepositReceipt(entity);
		msg.setFlag(MessageMap.INFOR_SUCCESS);
		msg.setMessage("新增存单成功");
		return msg;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto getDepositReceiptInfo(String companyId, String totalId) {
		// 查询存单信息
		if(CommonUtils.isEmpty(totalId)) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR, "请求参数不能为空"));
		}
		List<TJgDepositReceipt> resultList=this.tJgDepositReceiptMapper.getDepositReceiptInfo(totalId);
		BaseDto baseDto=new BaseDto<>(new MessageMap(MessageMap.INFOR_SUCCESS, "查询成功"));
		baseDto.setLstDto(resultList);
		return baseDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto getDepositReceiptInfoForGive(String companyId, String projectId, String oprId) {
		// 查询传单信息，交账之前
		if(CommonUtils.isEmpty(projectId) || CommonUtils.isEmpty(oprId)) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空"));
		}
		List<TJgDepositReceipt> resultList=this.tJgDepositReceiptMapper.getDpositReceipInfoForGive(projectId, oprId);
		BaseDto baseDto=new BaseDto<>(new MessageMap(MessageMap.INFOR_SUCCESS, "查询成功"));
		baseDto.setLstDto(resultList);
		return baseDto;
	}

	@Override
	public MessageMap delDepositReceiptInfo(String companyId, String id) {
		// 单个删除存单操作，交账前是允许这样操作的
		int num=this.tJgDepositReceiptMapper.delDpositReceipInfoById(id);
		if(num >0 ) {
			return new MessageMap(MessageMap.INFOR_SUCCESS,"操作成功");
		}else {
			return new MessageMap(MessageMap.INFOR_ERROR,"操作异常");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageDepositsInfo(String companyId, TJgDepositReceipt entity, int type) {
		// 这里聚合出纳和财务两个角色查看存单信息
		/*if(CommonUtils.isEmpty(entity) || CommonUtils.isEmpty(entity.getProjectId()) || CommonUtils.isEmpty(entity.getOprId())) {
			return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空"));
		}*/
		List<TJgDepositReceipt> resultList=new ArrayList<>();
		//出纳
		if(1 == type) {
			resultList=this.tJgDepositReceiptMapper.listPageDepositsInfo(entity);
		}else if(2 == type) {
			//财务
			resultList=this.tJgDepositReceiptMapper.listPageDepositsInfoKJ(entity);
		}
		BaseDto baseDto=new BaseDto<>(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		baseDto.setLstDto(resultList);
		baseDto.setPage(entity.getPage());
		return baseDto;
	}
	
	
}
