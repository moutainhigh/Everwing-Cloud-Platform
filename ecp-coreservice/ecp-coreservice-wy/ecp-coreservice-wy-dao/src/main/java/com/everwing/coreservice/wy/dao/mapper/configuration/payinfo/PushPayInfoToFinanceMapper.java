package com.everwing.coreservice.wy.dao.mapper.configuration.payinfo;


import com.everwing.coreservice.common.wy.entity.configuration.payinfo.PushPayInfoToFinance;

import java.util.List;
import java.util.Map;
/**
 * @describe （物业，本体，水费，电费）方案相关
 * @author QHC
 *
 */
public interface PushPayInfoToFinanceMapper {
	
	int addPushPayInfo(PushPayInfoToFinance entity);
	
	int updataPushPayInfo(PushPayInfoToFinance entity);
	
	PushPayInfoToFinance getLastPushInfo(Map<String, String> paraMap);
	
	int batchInsert(List<PushPayInfoToFinance> list);
	
	List<Map<String, String>> getSchemeTaxRate(String projectId);

	List<Map<String, Double>> getSchemeTaxRateDouble(String projectId);
	
}
