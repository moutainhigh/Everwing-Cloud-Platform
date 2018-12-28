package com.everwing.coreservice.wy.dao.mapper.delivery;

import com.everwing.coreservice.common.wy.entity.delivery.TJgAccountReceivable;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/***
 * @describe 交账明细表接口
 * @author qhc
 * @ date 2017-08-31 
 */
public interface TJgAccountReceivableMapper{
	
	int addAccountReceivable(TJgAccountReceivable entity) throws DataAccessException;
	 
	List<TJgAccountReceivable>  listPageAcountReceiveInfo(TJgAccountReceivable entity);
	
	Map<String, Double> sumPaymentInfo(TJgAccountReceivable entity);

	Map<String, Double> sumPaymentInfoForPush(Map<String, String> paramMap);

	Map<String, Double> sumProductfoForPush(Map<String, String> paramMap);
	
	Map<String, Double> sumNotGavenAmountInfo(TJgAccountReceivable entity);
	
	double getNotGivenCashAmount(String userId,String projectId);
	
	List<Map<String, String>> listPageNotGivenInfos(TJgAccountReceivable entity);
	
	int updateStasusAndTotalId(TJgAccountReceivable entity) throws DataAccessException;
	
	int updateStatusBatch(List<String> list) throws DataAccessException;
	
	List<TJgAccountReceivable> listPageReceiveByTotalId(TJgAccountReceivable entity);
	
	List<TJgAccountReceivable> listPageAccountReceiveForKJ(TJgAccountReceivable entity);

	int batchInsert(List<TJgAccountReceivable> insertTars) throws DataAccessException;

	TJgAccountReceivable findById(String id);
	
	int deleteByRelationid(String relationId);

	int modify(TJgAccountReceivable tar);

	int deleteById(String id);
	
	double sumPaymentInfoCashIn(TJgAccountReceivable entity);
	
	double sumPaymentInfoCashOut(TJgAccountReceivable entity);
	
	//这里新增两个接口给产品和物业用来判断是否可以做退回操作
	//产品用：根据relation_id查询
	int getIsGiveBackProduct(String id);
	
	List<TJgAccountReceivable> getPushInfoByFinece(String projectId);
	
	
	
}
