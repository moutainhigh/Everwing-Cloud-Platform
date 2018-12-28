package com.everwing.coreservice.solr.core.service.impl;/**
 * Created by wust on 2018/6/8.
 */

import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.solr.service.SolrDefaultService;
import com.everwing.coreservice.solr.core.CollectionContext;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 *
 * Function:默认接口实现
 * Reason:通用增删方法
 * Date:2018/6/8
 * @author wusongti@lii.com.cn
 */
public class SolrDefaultServiceImpl implements SolrDefaultService {
    @Autowired
    protected HttpSolrClient httpSolrClient;

    @Override
    public UpdateResponse addBean(String companyId, Object o) {
        UpdateResponse updateResponse = null;
        try {
            updateResponse = httpSolrClient.addBean(getCollection(),o);
            httpSolrClient.commit(getCollection());
        } catch (IOException e) {
            throw new ECPBusinessException(e);
        } catch (SolrServerException e) {
            throw new ECPBusinessException(e);
        }
        return updateResponse;
    }

    @Override
    public UpdateResponse addBean(String companyId, Object obj, int commitWithinMs) {
        UpdateResponse updateResponse = null;
        try {
            updateResponse = httpSolrClient.addBean(getCollection(),obj,commitWithinMs);
            httpSolrClient.commit(getCollection());
        } catch (IOException e) {
            throw new ECPBusinessException(e);
        } catch (SolrServerException e) {
            throw new ECPBusinessException(e);
        }
        return updateResponse;
    }

    @Override
    public UpdateResponse addBeans(String companyId, Collection<?> beans) {
        UpdateResponse updateResponse = null;
        try {
            updateResponse = httpSolrClient.addBeans(getCollection(),beans);
            httpSolrClient.commit(getCollection());
        } catch (IOException e) {
            throw new ECPBusinessException(e);
        } catch (SolrServerException e) {
            throw new ECPBusinessException(e);
        }
        return updateResponse;
    }

    @Override
    public UpdateResponse addBeans(String companyId, Collection<?> beans, int commitWithinMs) {
        UpdateResponse updateResponse = null;
        try {
            updateResponse = httpSolrClient.addBeans(getCollection(),beans,commitWithinMs);
            httpSolrClient.commit(getCollection());
        } catch (IOException e) {
            throw new ECPBusinessException(e);
        } catch (SolrServerException e) {
            throw new ECPBusinessException(e);
        }
        return updateResponse;
    }

    @Override
    public UpdateResponse deleteById(String companyId, String id) {
        UpdateResponse updateResponse = null;
        try {
            updateResponse = httpSolrClient.deleteById(getCollection(),id);
            httpSolrClient.commit(getCollection());
        } catch (IOException e) {
            throw new ECPBusinessException(e);
        } catch (SolrServerException e) {
            throw new ECPBusinessException(e);
        }
        return updateResponse;
    }



    @Override
    public UpdateResponse deleteById(String companyId, String id, int commitWithinMs) {
        UpdateResponse updateResponse = null;
        try {
            updateResponse = httpSolrClient.deleteById(getCollection(),id,commitWithinMs);
            httpSolrClient.commit(getCollection());
        } catch (IOException e) {
            throw new ECPBusinessException(e);
        } catch (SolrServerException e) {
            throw new ECPBusinessException(e);
        }
        return updateResponse;
    }

    @Override
    public UpdateResponse deleteById(String companyId, List<String> ids) {
        UpdateResponse updateResponse = null;
        try {
            updateResponse = httpSolrClient.deleteById(getCollection(),ids);
            httpSolrClient.commit(getCollection());
        } catch (IOException e) {
            throw new ECPBusinessException(e);
        } catch (SolrServerException e) {
            throw new ECPBusinessException(e);
        }
        return updateResponse;
    }

    @Override
    public UpdateResponse deleteById(String companyId, List<String> ids, int commitWithinMs) {
        UpdateResponse updateResponse = null;
        try {
            updateResponse = httpSolrClient.deleteById(getCollection(),ids,commitWithinMs);
            httpSolrClient.commit(getCollection());
        } catch (IOException e) {
            throw new ECPBusinessException(e);
        } catch (SolrServerException e) {
            throw new ECPBusinessException(e);
        }
        return updateResponse;
    }

    protected String getCollection(){
        return CollectionContext.getContext().getCollection();
    }
}
