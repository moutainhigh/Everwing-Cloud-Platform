package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.OperationLog;
import com.everwing.coreservice.platform.api.util.ServiceResources;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@NoExceptionProxy
public class LogApi extends ServiceResources{
	private ThreadLocal logThreadLocal = new ThreadLocal();

	
	/**
	 * 临时存储日志
	 */
	@NoExceptionProxy
	public void storeLog(OperationLog log) {
		logThreadLocal.set(log);
	}
	
	/**
	 * 临时存储日志
	 */
	@NoExceptionProxy
	public void storeLog(String logDesc, Dict logType) {
		OperationLog log = initLogObject(logDesc, logType);
		logThreadLocal.set(log);
	}
	
	/**
	 * 写入临时存储的日志,并设置结果
	 */
	@NoExceptionProxy
	public void insertStoredLogWithIssuccess(boolean isSuccess) {
		Object log = logThreadLocal.get();
		if(log != null){
			((OperationLog)log).setIsSuccess(isSuccess);
			commonService.insertSelective(log);
		}
		//释放资源
		logThreadLocal.set(null);
	}
	
	/**
	 * 直接写入一个日志
	 */
	@NoExceptionProxy
	public void insertLog(OperationLog log) {
		commonService.insertSelective(log);
	}
	
	/**
	 * 插入日志
	 */
	@NoExceptionProxy
	public void insertLog(String logDesc, Dict logType) {
		OperationLog log = initLogObject(logDesc, logType);
		commonService.insertSelective(log);
	}

	@NoExceptionProxy
	private OperationLog initLogObject(String logDesc, Dict logType) {
		OperationLog log = new OperationLog();
		log.setOperationLogId(UUID.randomUUID().toString());
		log.setProjectId("");
		log.setModelName("");
		log.setBusinessName("");
		log.setCreateTime(new Date());
		// TODO 添加admin用户名设置
		log.setOperationRoleName("");
		log.setOperationUserName("");
		log.setOperationType(logType.getIntValue());
		log.setLogSourceType("");
		log.setOperationDescription(logDesc);
		return log;
	}
}
