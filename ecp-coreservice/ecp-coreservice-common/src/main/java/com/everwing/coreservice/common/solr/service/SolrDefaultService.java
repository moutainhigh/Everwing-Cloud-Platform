package com.everwing.coreservice.common.solr.service;/**
 * Created by wust on 2018/6/8.
 */

import org.apache.solr.client.solrj.response.UpdateResponse;

import java.util.Collection;
import java.util.List;

/**
 *
 * Function:默认接口
 * Reason:
 * Date:2018/6/8
 * @author wusongti@lii.com.cn
 */
public interface SolrDefaultService {
    /**
     * @see org.apache.solr.client.solrj.SolrClient's addBean(String collection, Object obj)
     * @param companyId
     * @param obj
     * @return
     */
    UpdateResponse addBean(String companyId,Object obj);


    /**
     * @see org.apache.solr.client.solrj.SolrClient's addBean(String collection, Object obj, int commitWithinMs)
     * @param companyId
     * @param obj
     * @param commitWithinMs
     * @return
     */
    UpdateResponse addBean(String companyId,Object obj, int commitWithinMs);


    /**
     * @see org.apache.solr.client.solrj.SolrClient's addBeans(String collection, Collection<?> beans)
     * @param companyId
     * @param beans
     * @return
     */
    UpdateResponse addBeans(String companyId, Collection<?> beans);

    /**
     * @see org.apache.solr.client.solrj.SolrClient's addBeans(String collection, Collection<?> beans, int commitWithinMs)
     * @param companyId
     * @param beans
     * @return
     */
    UpdateResponse addBeans(String companyId, Collection<?> beans, int commitWithinMs);

    /**
     * @see org.apache.solr.client.solrj.SolrClient's deleteById(String collection, String id)
     * @param companyId
     * @param id
     * @return
     */
    UpdateResponse deleteById(String companyId, String id);

    /**
     * @see org.apache.solr.client.solrj.SolrClient's deleteById(String collection, String id, int commitWithinMs)
     * @param companyId
     * @param id
     * @return
     */
    UpdateResponse deleteById(String companyId,String id, int commitWithinMs);

    /**
     * @see org.apache.solr.client.solrj.SolrClient's deleteById(String collection, String ids)
     * @param companyId
     * @param ids
     * @return
     */
    UpdateResponse deleteById(String companyId,List<String> ids);

    /**
     * @see org.apache.solr.client.solrj.SolrClient's deleteById(String collection, String ids, int commitWithinMs)
     * @param companyId
     * @param ids
     * @return
     */
    UpdateResponse deleteById(String companyId,List<String> ids, int commitWithinMs);
}
