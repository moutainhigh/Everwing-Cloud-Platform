package com.everwing.coreservice.solr.dao;/**
 * Created by wust on 2018/6/6.
 */

import com.everwing.coreservice.common.Page;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/6/6
 * @author wusongti@lii.com.cn
 */
@Component
public class SolrDefaultDaoImpl {
    public void listPage(HttpSolrClient httpSolrClient, Page page, SolrQuery query, List<QueryResponse> queryResponseList) {

    }
}
