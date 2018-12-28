package com.everwing.coreservice.wy.dao.mapper.delivery;

import com.everwing.coreservice.common.wy.entity.delivery.TJgGiveAccountDetail;

import java.util.List;
import java.util.Map;

/***
 * @describe 银账交割交账明细表接口
 * @author qhc
 * @ date 2017-08-31 
 */
public interface TJgGiveAccountDetailMapper{
	
	int addGiveAccountDetail(TJgGiveAccountDetail entity);
	
	List<TJgGiveAccountDetail> listPageGiveCashInfo(TJgGiveAccountDetail entity);
	
	List<TJgGiveAccountDetail> listPageReceiveCashInfo(TJgGiveAccountDetail entity);
	
	int updateTotalId(TJgGiveAccountDetail entity);
	
	int returnOrConfirmGiveInfo(Map<String, Object> paramMap);
	
	List<TJgGiveAccountDetail> listPageGiveAccountByTotalId(TJgGiveAccountDetail entity);
	
	int updateStatusBatchs(List<String> list);
	
	int updateStatusBatchsForReturn(List<String> list);
	
	List<TJgGiveAccountDetail> listPageAccountReceivables(TJgGiveAccountDetail entity);
	
	List<TJgGiveAccountDetail> listPageAccountReceivableByOpr(TJgGiveAccountDetail entity);
	
	List<TJgGiveAccountDetail> listPageGiveAccountForCN(TJgGiveAccountDetail entity);
	
}
