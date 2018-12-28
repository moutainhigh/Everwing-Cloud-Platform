package com.everwing.autotask.core.dao;


import java.util.List;
import java.util.Map;

import com.everwing.coreservice.common.wy.entity.configuration.payinfo.PushPayInfoToFinance;
import org.springframework.stereotype.Repository;

/**
 * @describe （物业，本体，水费，电费）方案相关
 * @author QHC
 *
 */
@Repository
public interface PushPayInfoToFinanceMapper {
	
	int addPushPayInfo(PushPayInfoToFinance entity);
	
	int updataPushPayInfo(PushPayInfoToFinance entity);
	
	PushPayInfoToFinance getLastPushInfo(Map<String, String> paraMap);
	
	int batchInsert(List<PushPayInfoToFinance> list);
	
	List<Map<String, String>> getSchemeTaxRate(String projectId);

	List<Map<String, Double>> getSchemeTaxRateDouble(String projectId);

	Map<String,Integer> getPushDataCount(String projectId);
	
}
