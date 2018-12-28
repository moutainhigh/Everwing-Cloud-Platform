package com.everwing.coreservice.wy.core.dsaspect;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.PropertiesHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Order(1)
@Component
public class DataSourceAspect {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceAspect.class);
	
	//环绕通知
	@Around("execution(* com.everwing.coreservice.wy.core.service.impl..*.*(..))")
	public Object methodAspect(ProceedingJoinPoint jp) throws Throwable{
		Object[] args = jp.getArgs();
		Signature sig = jp.getSignature();
		if(sig instanceof MethodSignature){
			MethodSignature msig = (MethodSignature) sig;
			Method currentMethod = jp.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
			if(null != currentMethod) {
				if (PropertiesHelper.getInstance("config/commonConf.properties").getValue("noFilterMethods").contains(currentMethod.getName())) {
					return jp.proceed(jp.getArgs());
				}
			}
		}
		if (args.length>0) {
			if (CommonUtils.isEmpty(args[0])) {
				LOG.error("切换数据源失败，service层的方法第一个参数务必是WyBusinessContext对象或者是String类型的公司编码");
				throw new ECPBusinessException(ReturnCode.WY_DS_COMPANY_ID_IS_NULL);
			} else {
				if (args[0] instanceof String) {
					String companyId = CommonUtils.null2String(args[0]);
					WyBusinessContext.getContext().setCompanyId(companyId);
				} else if (args[0] instanceof WyBusinessContext) {
					WyBusinessContext ctx = (WyBusinessContext)args[0];
					WyBusinessContext.getContext().setCompanyId(ctx.getCompanyId());
					WyBusinessContext.getContext().setCompanyCode(ctx.getCompanyCode());
					WyBusinessContext.getContext().setCompanyName(ctx.getCompanyName());
					WyBusinessContext.getContext().setProjectId(ctx.getProjectId());
					WyBusinessContext.getContext().setProjectCode(ctx.getProjectCode());
					WyBusinessContext.getContext().setProjectName(ctx.getProjectName());
					WyBusinessContext.getContext().setLoginName(ctx.getLoginName());
					WyBusinessContext.getContext().setUserId(ctx.getUserId());
					WyBusinessContext.getContext().setStaffNumber(ctx.getStaffNumber());
					WyBusinessContext.getContext().setStaffName(ctx.getStaffName());
					WyBusinessContext.getContext().setLan(ctx.getLan());
				} else {
					LOG.error("切换数据源失败，service层的方法第一个参数务必是WyBusinessContext对象或者是String类型的公司编码");
					throw new ECPBusinessException(ReturnCode.WY_DS_COMPANY_ID_PARAMS_ERROR);
				}
			} 
		}
		return jp.proceed(jp.getArgs());
	}
}
