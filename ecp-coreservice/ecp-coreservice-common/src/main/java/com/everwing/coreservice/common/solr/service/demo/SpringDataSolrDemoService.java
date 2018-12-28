package com.everwing.coreservice.common.solr.service.demo;/**
 * Created by wust on 2018/6/4.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.solr.entity.demo.SpringDataSolrDemoSearch;
import com.everwing.coreservice.common.solr.service.SolrDefaultService;

/**
 *
 * Function:
 * Reason:
 * Date:2018/6/4
 * @author wusongti@lii.com.cn
 */
public interface SpringDataSolrDemoService extends SolrDefaultService {
    /**
     * 分页查询
     * @param companyId
     * @param springDataSolrDemoSearch
     * @return
     */
    BaseDto listPageDemo(String companyId, SpringDataSolrDemoSearch springDataSolrDemoSearch);


    /**
     * 根据SolrParams参数查询数据
     * @param companyId 公司id
     * @param springDataSolrDemoSearch 参数
     * @return BaseDto
     */
    BaseDto querySpringDataSolrDemoByParams(String companyId, SpringDataSolrDemoSearch springDataSolrDemoSearch);
}
