package com.everwing.coreservice.solr.core.service.impl.demo;/**
 * Created by wust on 2018/6/4.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.solr.entity.demo.SpringDataSolrDemo;
import com.everwing.coreservice.common.solr.entity.demo.SpringDataSolrDemoSearch;
import com.everwing.coreservice.common.solr.service.demo.SpringDataSolrDemoService;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.solr.core.service.impl.SolrDefaultServiceImpl;
import com.everwing.coreservice.solr.dao.SolrDefaultDaoImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2018/6/4
 * @author wusongti@lii.com.cn
 */
@Service("springDataSolrDemoServiceImpl")
public class SpringDataSolrDemoServiceImpl extends SolrDefaultServiceImpl implements SpringDataSolrDemoService {

    @Autowired
    private SolrDefaultDaoImpl solrDefaultDaoImpl;


    @Override
    public BaseDto listPageDemo(String companyId, SpringDataSolrDemoSearch springDataSolrDemoSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap messageMap = new MessageMap();
        Page page = springDataSolrDemoSearch.getPage();

        String keyWord = CommonUtils.null2String(springDataSolrDemoSearch.getKeyWord());
        SolrQuery query = new SolrQuery();
        query.setQuery("productName:*"+keyWord+"* OR createrName:*"+keyWord+"* OR modifyName:*"+keyWord+"* OR batchNo:*"+keyWord+"*");
        query.set("collection", super.getCollection());
        query.setHighlight(true); // 设置高亮
        query.addHighlightField("name"); // 设置高亮的字段
        query.setHighlightSimplePre("<font color='red'>"); // 设置高亮的样式
        query.setHighlightSimplePost("</font>");

        List<QueryResponse> queryResponseList = new ArrayList<>(1);
        solrDefaultDaoImpl.listPage(httpSolrClient,page,query,queryResponseList);
        if(CollectionUtils.isNotEmpty(queryResponseList)){
            QueryResponse queryResponse = queryResponseList.get(0);
            if(CollectionUtils.isNotEmpty(queryResponse.getResults())){
                Map<String, Map<String, List<String>>> mapMap = queryResponse.getHighlighting();    // 高亮结果
                List<SpringDataSolrDemo> springDataSolrDemos = queryResponse.getBeans(SpringDataSolrDemo.class);
                baseDto.setLstDto(springDataSolrDemos);
                baseDto.setE(mapMap);
                baseDto.setPage(page);
            }else {
                messageMap.setFlag(MessageMap.INFOR_WARNING);
                messageMap.setMessage("没有匹配到数据。");
                baseDto.setMessageMap(messageMap);
            }
        }

        return baseDto;
    }



    @Override
    public BaseDto querySpringDataSolrDemoByParams(String companyId, SpringDataSolrDemoSearch springDataSolrDemoSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap messageMap = new MessageMap();
        try {
            SolrParams solrParams = new SolrQuery();
            QueryResponse queryResponse = httpSolrClient.query(this.getCollection(),solrParams);
            List<SpringDataSolrDemo> springDataSolrDemos = queryResponse.getBeans(SpringDataSolrDemo.class);
            baseDto.setLstDto(springDataSolrDemos);
        } catch (SolrServerException e) {
            messageMap.setFlag(MessageMap.INFOR_ERROR);
            messageMap.setObj(e.getMessage());
        } catch (IOException e) {
            messageMap.setFlag(MessageMap.INFOR_ERROR);
            messageMap.setObj(e.getMessage());
        }
        baseDto.setMessageMap(messageMap);
        return baseDto;
    }
}

