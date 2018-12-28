package com.everwing.coreservice.solr.dao;/**
 * Created by wust on 2018/6/6.
 */

import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/6/6
 * @author wusongti@lii.com.cn
 */
@Aspect
@Order(1)
@Component
public class SolrDaoAspect {
    static Logger logger = LogManager.getLogger(SolrDaoAspect.class);



    @Around("execution(* com.everwing.coreservice.solr.dao.SolrDefaultDaoImpl.*listPage*(..))")
    public Object methodAspect(ProceedingJoinPoint jp) throws Throwable{
        if(logger.isInfoEnabled()){
            logger.info("Solr非数据库方式分页拦截器开始。。。");
        }

        Object[] args = jp.getArgs();
        if(args == null || args.length < 4){
            logger.error("请正确设置参数：HttpSolrClient，Page，SolrQuery，List<QueryResponse>。");
            throw new ECPBusinessException("请正确设置参数：HttpSolrClient，Page，SolrQuery，List<QueryResponse>。");
        }

        HttpSolrClient httpSolrClient = null;
        if(args[0] instanceof HttpSolrClient){
            httpSolrClient = (HttpSolrClient)args[0];
        }else{
            logger.error("第1个参数必须是HttpSolrClient对象。");
            throw new ECPBusinessException("第1个参数必须是HttpSolrClient对象。");
        }

        Page page = null;
        if(args[1] instanceof Page){
            page = (Page)args[1];
        }else{
            logger.error("第2个参数必须是Page对象。");
            throw new ECPBusinessException("第2个参数必须是Page对象。");
        }

        SolrQuery solrQuery = null;
        if(args[2] instanceof SolrQuery){
            solrQuery = (SolrQuery)args[2];
        }else{
            logger.error("第3个参数必须是SolrQuery对象。");
            throw new ECPBusinessException("第3个参数必须是SolrQuery对象。");
        }

        List<QueryResponse> queryResponseList = null;
        if(args[3] instanceof List){
            queryResponseList = (List<QueryResponse>)args[3];
        }else{
            logger.error("第4个参数必须是List<QueryResponse>对象。");
            throw new ECPBusinessException("第4个参数必须是QueryResponse对象。");
        }

        int currentPage = page.getCurrentPage();
        int pageSize = page.getShowCount();
        int startRow = (currentPage - 1) * pageSize;
        int rows = page.getShowCount();
        solrQuery.setStart(startRow);
        solrQuery.setRows(rows);

        String collectionName = solrQuery.get("collection");
        try {
            QueryResponse response = httpSolrClient.query(collectionName,solrQuery);
            if(response != null){
                queryResponseList.add(response);
                SolrDocumentList solrDocumentList = response.getResults();
                if(solrDocumentList != null){
                    Long totalResult = solrDocumentList.getNumFound();
                    page.setTotalResult(totalResult.intValue());
                }
            }
        } catch (SolrServerException e) {
            logger.error(e);
            throw new ECPBusinessException("搜索引擎出现异常："+e);
        } catch (IOException e) {
            logger.error(e);
            throw new ECPBusinessException("搜索引擎出现异常："+e);
        }


        if(logger.isInfoEnabled()){
            logger.info("Solr非数据库方式分页拦截器结束。。。");
        }


        args[1] = page;
        args[3] = queryResponseList;
        Object retVal = jp.proceed(args);
        return retVal;
    }
}
