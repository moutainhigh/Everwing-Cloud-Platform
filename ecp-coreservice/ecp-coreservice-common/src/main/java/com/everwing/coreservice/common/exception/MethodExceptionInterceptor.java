package com.everwing.coreservice.common.exception;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Created by zhugeruifei on 17/3/24.
 */
@Aspect
@Order
@Component
public class MethodExceptionInterceptor {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Pointcut("within(com.everwing.coreservice.platform.api..*)")
	private void platformApi(){}
	
	@Pointcut("within(com.everwing.coreservice.admin.api..*)")
	private void adminApi(){}
	
	@Pointcut("within(com.everwing.coreservice.wy.api..*)")
	private void wyApi(){}

    @Pointcut("within(com.everwing.coreservice.wy.fee.api..*)")
    private void wyFeeApi(){}

    @Pointcut("within(com.everwing.coreservice.wy.fee.order.api..*)")
    private void wyFeeOrderApi(){}

    @Pointcut("within(com.everwing.coreservice.dynamicreports.api..*)")
    private void reportApi(){}
	
	@Pointcut("@annotation(com.everwing.coreservice.common.exception.ExceptionProxy)")
	private void exceptionProxy(){}
	
    @Around("platformApi() || adminApi() || exceptionProxy() || wyApi() || reportApi()|| wyFeeApi() || wyFeeOrderApi()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();



		logger.debug("---JoinPointMethod: {}.{}()" , joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
		
		//不需要代理的方法和类
		if (signature.getDeclaringType().isAnnotationPresent(NoExceptionProxy.class) || signature.getMethod().isAnnotationPresent(NoExceptionProxy.class)) {
			return joinPoint.proceed();
		}
        

        //

        RemoteModelResult<? extends Object> remoteRslt = null;
        try {
        	logger.debug("MethodExceptionInterceptor.around()-1");
            remoteRslt = (RemoteModelResult<? extends Object>) joinPoint.proceed(joinPoint.getArgs());
            logger.debug("MethodExceptionInterceptor.around()-2");
        } catch (Throwable e) {
            remoteRslt = new RemoteModelResult<Object>();
            if (e instanceof ECPBusinessException) {
                ReturnCodeAware ex = (ReturnCodeAware) e;
                String errCode = ex.getErrorCode();
                String errDesc = ex.getErrorDescription();
                Object[] args = ex.getArgs();
                String errMsg = "";
                if(args == null){
                    errMsg = errDesc;
                }else{
                    errMsg = MessageFormat.format(errDesc, args);/*appContext.getMessage(errCode, args, Locale.CHINA);*/
                }
                remoteRslt.setCode(errCode);
                remoteRslt.setMsg(errMsg);
                logger.error(errMsg);
            } else if (e instanceof DataAccessException){
                remoteRslt.setCode(ReturnCode.SYSTEM_ERROR.getCode());
                remoteRslt.setMsg(ReturnCode.SYSTEM_ERROR.getDescription());
                logger.error(e.getMessage(),e);
            }else{
                remoteRslt.setCode(ReturnCode.SYSTEM_ERROR.getCode());
                remoteRslt.setMsg(ReturnCode.SYSTEM_ERROR.getDescription());
                logger.error(e.getMessage(),e);
            }
        }
        return remoteRslt;
    }
    

}
