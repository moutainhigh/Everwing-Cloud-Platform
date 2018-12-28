package com.everwing.coreservice.wy.dao.mapper.delivery;

import com.everwing.coreservice.common.wy.entity.delivery.TJgDepositReceipt;

import java.util.List;

/***
 * @describe 存单表接口
 * @author qhc
 * @ date 2017-08-31 
 */
public interface TJgDepositReceiptMapper{
	
	int addDepositReceipt(TJgDepositReceipt entity);
	
	List<TJgDepositReceipt> getDepositReceiptInfo(String totalId);
	
	List<TJgDepositReceipt> getDpositReceipInfoForGive(String projectId,String oprId);
	
	int delDpositReceipInfoById(String id);
	
	int updateTotalId(String totalId,String oprId,String projectId);
	
	int updateTotalIdById(List<String> list);
	
	List<TJgDepositReceipt> listPageDepositsInfo(TJgDepositReceipt entity);
	
	List<TJgDepositReceipt> listPageDepositsInfoKJ(TJgDepositReceipt entity);
	
}
