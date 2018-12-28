package com.everwing.coreservice.solr.api.demo;/**
 * Created by wust on 2018/6/4.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.solr.entity.demo.SpringDataSolrDemo;
import com.everwing.coreservice.common.solr.entity.demo.SpringDataSolrDemoSearch;
import com.everwing.coreservice.common.solr.service.demo.SpringDataSolrDemoService;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/6/4
 * @author wusongti@lii.com.cn
 */
@Component
public class SpringDataSolrDemoApi {
    @Autowired
    private SpringDataSolrDemoService springDataSolrDemoService;

    public RemoteModelResult<UpdateResponse> addBean(String companyId, SpringDataSolrDemo springDataSolrDemo){
        RemoteModelResult<UpdateResponse> remoteModelResult = new RemoteModelResult<>();
        UpdateResponse updateResponse = springDataSolrDemoService.addBean(companyId,springDataSolrDemo);
        remoteModelResult.setModel(updateResponse);
        return remoteModelResult;
    }

    public RemoteModelResult<UpdateResponse> addBeans(String companyId, List<SpringDataSolrDemo> springDataSolrDemos){
        RemoteModelResult<UpdateResponse> remoteModelResult = new RemoteModelResult<>();
        UpdateResponse updateResponse = springDataSolrDemoService.addBeans(companyId,springDataSolrDemos);
        remoteModelResult.setModel(updateResponse);
        return remoteModelResult;
    }

    public RemoteModelResult<UpdateResponse> deleteById(String companyId, String id){
        RemoteModelResult<UpdateResponse> remoteModelResult = new RemoteModelResult<>();
        UpdateResponse updateResponse = springDataSolrDemoService.deleteById(companyId,id);
        remoteModelResult.setModel(updateResponse);
        return remoteModelResult;
    }

    public RemoteModelResult<BaseDto> listPageDemo(String companyId, SpringDataSolrDemoSearch springDataSolrDemoSearch){
        RemoteModelResult<BaseDto> remoteModelResult = new RemoteModelResult<>();
        BaseDto baseDto = springDataSolrDemoService.listPageDemo(companyId,springDataSolrDemoSearch);
        remoteModelResult.setModel(baseDto);
        return remoteModelResult;
    }
}
