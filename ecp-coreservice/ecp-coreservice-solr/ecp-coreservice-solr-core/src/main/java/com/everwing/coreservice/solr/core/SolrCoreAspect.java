package com.everwing.coreservice.solr.core;/**
 * Created by wust on 2018/6/6.
 */

import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * Function:服务层接口实现类方法切面
 * Reason:用于切换solr数据源
 * Date:2018/6/8
 * @author wusongti@lii.com.cn
 */
@Aspect
@Order(1)
@Component
public class SolrCoreAspect {
    static Logger logger = LogManager.getLogger(SolrCoreAspect.class);



    @Around("execution(* com.everwing.coreservice.solr.core.service.impl..*.*(..))")
    public Object methodAspect(ProceedingJoinPoint jp) throws Throwable{
        if(logger.isInfoEnabled()){
            logger.info("Solr切换数据源开始。。。");
        }

        Object[] args = jp.getArgs();
        if(args == null || StringUtils.isBlank(CommonUtils.null2String(args[0]))){
            logger.error("请将companyId作为参数传入service层的方法。");
            throw new ECPBusinessException("切换数据源失败，请查看日志。");
        }


        String companyId = (String)args[0];
        if(logger.isInfoEnabled()){
            logger.info("当前公司ID为{}",companyId);
        }

        String collection = PropertiesHelper.getInstance("config/solr.properties").getValue("collectionKey"+companyId);
        if(StringUtils.isBlank(CommonUtils.null2String(collection))){
            logger.error("请在ecp-coreservice-solr-core项目下面的solr.properties配置公司与集合的映射关系。");
            throw new ECPBusinessException("切换数据源失败，请查看日志。");
        }

        if(logger.isInfoEnabled()){
            logger.info("当前获取到的集合是{}",collection);
        }

        CollectionContext.getContext().setCompanyId(companyId);
        CollectionContext.getContext().setCollection(collection);

        if(logger.isInfoEnabled()){
            logger.info("Solr切换数据源结束。。。");
        }
        return jp.proceed(args);
    }
}
