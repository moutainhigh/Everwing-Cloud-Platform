package com.everwing.coreservice.wy.api.business.readingtask;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.service.business.readingtask.TcReadingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TcReadingTaskApi {

    @Autowired
    private TcReadingTaskService tcReadingTaskService;

/*	public RemoteModelResult<BaseDto> getWaterTaskDetailById(String companyId,String id) {
		return new RemoteModelResult<BaseDto>(this.tcReadingTaskService.getWaterTaskDetailById(companyId,id));
	}*/

    public RemoteModelResult<BaseDto> listPage(String companyId, TcReadingTask entity) {
        return new RemoteModelResult<BaseDto>(this.tcReadingTaskService.listPageSearchResult(companyId, entity));
    }

    public RemoteModelResult<BaseDto> listPageTasksToAudit(String companyId, TcReadingTask task) {
        return new RemoteModelResult<BaseDto>(this.tcReadingTaskService.listPageTasksToAudit(companyId, task));
    }

    public RemoteModelResult<BaseDto> batchAudit(String companyId, List<TcReadingTask> tasks, Integer auditStatus) {
        return new RemoteModelResult<BaseDto>(this.tcReadingTaskService.batchAudit(companyId, tasks, auditStatus));
    }

    public RemoteModelResult<BaseDto> batchCompelete(String companyId, List<TcReadingTask> tasks) {
        return new RemoteModelResult<BaseDto>(this.tcReadingTaskService.batchCompelete(companyId, tasks));
    }

    public RemoteModelResult<BaseDto> findByScheduleId(String companyId, String scheduleId) {
        return new RemoteModelResult<BaseDto>(this.tcReadingTaskService.findByScheduleId(companyId, scheduleId));
    }

    /** -----------------------wyapp---------------*/
    public RemoteModelResult<List<TcReadingTask>> queryCurrentByAccountId(String companyId, String accountId, int meterType, int pageNo, int pageSize) {
        RemoteModelResult remoteModelResult = new RemoteModelResult();
        List<TcReadingTask> readingTask = tcReadingTaskService.queryCurrentByAccountId(companyId, accountId, meterType, pageNo, pageSize);
        remoteModelResult.setModel(readingTask);
        return remoteModelResult;
    }

    public RemoteModelResult<List<TcReadingTask>> queryHistoryByAccountId(String companyId, String accountId, int meterType, int pageNo, int pageSize) {
        RemoteModelResult remoteModelResult = new RemoteModelResult();
        List<TcReadingTask> readingTask = tcReadingTaskService.queryHistoryByAccountId(companyId, accountId, meterType, pageNo, pageSize);
        remoteModelResult.setModel(readingTask);
        return remoteModelResult;
    }
}