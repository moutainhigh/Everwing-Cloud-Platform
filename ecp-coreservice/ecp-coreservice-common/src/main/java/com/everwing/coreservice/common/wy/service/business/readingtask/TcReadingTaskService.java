package com.everwing.coreservice.common.wy.service.business.readingtask;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;

import java.util.List;

public interface TcReadingTaskService {

//	BaseDto getWaterTaskDetailById(String companyId, String id);

	BaseDto listPageSearchResult(String companyId, TcReadingTask entity);

	BaseDto listPageTasksToAudit(String companyId, TcReadingTask task);

	BaseDto batchAudit(String companyId, List<TcReadingTask> tasks,Integer auditStatus);

	BaseDto batchCompelete(String companyId, List<TcReadingTask> tasks);

	BaseDto findByScheduleId(String companyId, String scheduleId);


	/*------------------物业APP Service接口--------------------*/

    List<TcReadingTask> queryCurrentByAccountId(String companyId,String accountId,int meterType,int pageNo,int pageSize);

    List<TcReadingTask> queryHistoryByAccountId(String companyId,String accountId,int meterType,int pageNo,int pageSize);
}
