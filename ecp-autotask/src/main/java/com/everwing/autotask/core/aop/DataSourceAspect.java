package com.everwing.autotask.core.aop;

import com.everwing.autotask.core.datasource.DBContextHolder;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 数据源切换
 *
 * @author DELL shiny
 * @create 2018/5/8
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect {

    private static final Logger logger= LogManager.getLogger(DataSourceAspect.class);

    public DataSourceAspect(){
        logger.info("*********************aop*****************************");
    }

    @Around("execution(* com.everwing.autotask.core.service..*.*(..))")
    public Object methodAspect(ProceedingJoinPoint jp) throws Throwable{
        Object[] args = jp.getArgs();
        String companyId = null;
        logger.info(">>>> 切换数据源开始  <<<<");
        if (args.length>0) {
            if (args[0] instanceof String) {
                companyId = CommonUtils.null2String(args[0]);
            }
        }
        logger.info(">>>> 切换数据源 . 公司id为: {} <<<<",companyId);

        if(!Constants.STR_ZERO.equals(companyId)){
            DBContextHolder.setDBType(companyId);
        }else{
            DBContextHolder.setDBType("dataSource");
        }
        logger.info(">>>> 切换数据源结束.  <<<<");
        return jp.proceed(jp.getArgs());
    }
}
