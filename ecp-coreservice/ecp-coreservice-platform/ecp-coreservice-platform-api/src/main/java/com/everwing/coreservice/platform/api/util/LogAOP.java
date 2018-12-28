package com.everwing.coreservice.platform.api.util;

import com.everwing.coreservice.platform.api.LogApi;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author MonKong
 * @date 2017年7月20日
 */
@Aspect
@Component
public class LogAOP {
	@Autowired
	LogApi logApi;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Pointcut("@annotation(com.everwing.coreservice.platform.api.util.LogProxy)")
	private void logProxy() {}

	@Around("logProxy()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result =null;
		try {
			System.err.println("LogAOP.around()-1");
			result = joinPoint.proceed();
			System.err.println("LogAOP.around()-2");
			logApi.insertStoredLogWithIssuccess(true);
		} catch (Throwable e) {
			System.err.println("LogAOP.around()-exception");
			logApi.insertStoredLogWithIssuccess(false);
			logger.error(e.getMessage());
		}
		return result;
	}
}
