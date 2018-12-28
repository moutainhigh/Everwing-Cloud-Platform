package com.everwing.coreservice.wy.fee.core.aspect;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.PropertiesHelper;
import com.everwing.coreservice.common.wy.datasource.DataSourceUtil;
import com.everwing.coreservice.platform.api.CompanyApi;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @TODO 数据源切面
 * @author DELL
 *
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceAspect.class);
	
	@Autowired
	private CompanyApi companyApi;
	
	//环绕通知
	@Around("execution(* com.everwing.coreservice.wy.fee.core.service.impl..*.*(..))")
	public Object methodAspect(ProceedingJoinPoint jp) throws Throwable{
		Object[] args = jp.getArgs();
		String companyId = null;
		MethodSignature msig = null;
		Method currentMethod = null;
		Signature sig = jp.getSignature();
		String clazz = jp.getTarget().getClass().toString();
		if(sig instanceof MethodSignature){
			msig = (MethodSignature) sig;
			currentMethod = jp.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
			if(null != currentMethod) {
				clazz = clazz.substring(clazz.lastIndexOf(".") + 1).concat(".").concat(currentMethod.getName());

				if (PropertiesHelper.getInstance("config/commonConf.properties").getValue("noFilterMethods").contains(currentMethod.getName())) {
					return jp.proceed(jp.getArgs());
				}
			}
		}
		LOG.debug(">>>> 切换数据源开始  <<<<");
		if (args.length>0) {
			//LOG.info("当前请求的类与方法: {} , 参数为: {} ", clazz, JSON.toJSONString(args));
			if (CommonUtils.isEmpty(args[0])) {
				throw new ECPBusinessException(ReturnCode.WY_DS_COMPANY_ID_IS_NULL);
			} else {
				if (args[0] instanceof String) {
					companyId = CommonUtils.null2String(args[0]);
				} else if (args[0] instanceof WyBusinessContext) {
					WyBusinessContext ctx = (WyBusinessContext)args[0];
					companyId = ctx.getCompanyId();
				} else {
					throw new ECPBusinessException(ReturnCode.WY_DS_COMPANY_ID_PARAMS_ERROR);
				}
			} 
		}
		LOG.debug(">>>> 切换数据源 . 公司id为: {} <<<<",companyId);

		if(!Constants.STR_ZERO.equals(companyId)){
			RemoteModelResult<Company> rslt=null;
			rslt = this.companyApi.queryCompany(companyId);
			if(CommonUtils.isNotEmpty(rslt) && CommonUtils.isNotEmpty(rslt.getModel())){
				String dsStr = JSON.toJSONString(rslt.getModel());
				DataSourceUtil.changeDataSource(dsStr);
				LOG.debug(">>>> 切换数据源 . 数据源为: {} <<<<",dsStr);
			}else{
				throw new ECPBusinessException(ReturnCode.WY_DS_COMPANY_ID_IS_NULL);
			}
		}else{
			throw new ECPBusinessException(ReturnCode.WY_DS_COMPANY_ID_IS_NULL);
		}
		LOG.debug(">>>> 切换数据源结束.  <<<<");
		return jp.proceed(jp.getArgs());
	}
}
