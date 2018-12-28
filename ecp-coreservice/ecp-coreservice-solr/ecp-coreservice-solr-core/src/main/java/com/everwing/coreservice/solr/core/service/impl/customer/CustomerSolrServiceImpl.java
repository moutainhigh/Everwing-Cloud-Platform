package com.everwing.coreservice.solr.core.service.impl.customer;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.Page;
import com.everwing.coreservice.common.solr.entity.customer.*;
import com.everwing.coreservice.common.solr.service.customer.CustomerSolrService;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.solr.core.service.impl.SolrDefaultServiceImpl;
import com.everwing.coreservice.solr.dao.SolrDefaultDaoImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author shiny
 **/
@Service("customerSolrServiceImpl")
public class CustomerSolrServiceImpl extends SolrDefaultServiceImpl implements CustomerSolrService {

    @Autowired
    private SolrDefaultDaoImpl solrDefaultDaoImpl;

    @Override
    public BaseDto listPageBuilding(String companyId, BuildingSearch building) {
        BaseDto baseDto = new BaseDto();
        MessageMap messageMap = new MessageMap();
        Page page = building.getPage();

        String keyWord = CommonUtils.null2String(building.getKeyWord());
        SolrQuery query = new SolrQuery();
        StringBuilder stringBuilder = new StringBuilder();
        if(StringUtils.isNotEmpty(keyWord)) {
            stringBuilder.append("(id:*"+keyWord+"* OR ");
            stringBuilder.append("building_code:*"+keyWord+"* OR ");
            stringBuilder.append("building_full_name:*"+keyWord+"* OR ");
            stringBuilder.append("house_code_new:*"+keyWord+"*)");
            stringBuilder.append(" AND data_source : building");
        }else {
            stringBuilder.append("*:*");
        }
        query.setQuery(stringBuilder.toString());
        query.set("collection", super.getCollection());
        query.setHighlight(true); // 设置高亮
        query.addHighlightField("building_full_name"); // 设置高亮的字段
        query.setHighlightSimplePre("<font color='red'>"); // 设置高亮的样式
        query.setHighlightSimplePost("</font>");

        List<QueryResponse> queryResponseList = new ArrayList<>(1);
        solrDefaultDaoImpl.listPage(httpSolrClient,page,query,queryResponseList);
        if(CollectionUtils.isNotEmpty(queryResponseList)){
            QueryResponse queryResponse = queryResponseList.get(0);
            if(CollectionUtils.isNotEmpty(queryResponse.getResults())){
                Map<String, Map<String, List<String>>> mapMap = queryResponse.getHighlighting();    // 高亮结果
                List<BuildingSearch> springDataSolrDemos = queryResponse.getBeans(BuildingSearch.class);
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
    public BaseDto listPagePersonCust(String companyId, PersonCustSearch personCustSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap messageMap = new MessageMap();
        Page page = personCustSearch.getPage();

        String keyWord = CommonUtils.null2String(personCustSearch.getKeyWord());
        SolrQuery query = new SolrQuery();
        StringBuilder stringBuilder = new StringBuilder();
        if(StringUtils.isNotEmpty(keyWord)) {
            stringBuilder.append("(cust_id:*"+keyWord+"* OR ");
            stringBuilder.append("name:*"+keyWord+"* OR ");
            stringBuilder.append("register_phone:*"+keyWord+"* OR ");
            stringBuilder.append("card_num:*"+keyWord+"*)");
            stringBuilder.append(" AND data_source : cust");
        }else {
            stringBuilder.append("*:*");
        }
        query.setQuery(stringBuilder.toString());
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
                List<PersonCustSearch> springDataSolrDemos = queryResponse.getBeans(PersonCustSearch.class);
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
    public BaseDto listPageEnterpriseCust(String companyId, EnterpriseCustSearch enterpriseCustSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap messageMap = new MessageMap();
        Page page = enterpriseCustSearch.getPage();

        String keyWord = CommonUtils.null2String(enterpriseCustSearch.getKeyWord());
        SolrQuery query = new SolrQuery();
        StringBuilder stringBuilder = new StringBuilder();
        if(StringUtils.isNotEmpty(keyWord)) {
            stringBuilder.append("(enterprise_id:*"+keyWord+"* OR ");
            stringBuilder.append("enterprise_name:*"+keyWord+"* OR ");
            stringBuilder.append("address:*"+keyWord+"* OR ");
            stringBuilder.append("representative:*"+keyWord+"* OR ");
            stringBuilder.append("office_phone:*"+keyWord+"* OR ");
            stringBuilder.append("trading_number:*"+keyWord+"*)");
            stringBuilder.append(" AND data_source : enterprise");
        }else {
            stringBuilder.append("*:*");
        }
        query.setQuery(stringBuilder.toString());
        query.set("collection", super.getCollection());
        query.setHighlight(true); // 设置高亮
        query.addHighlightField("enterprise_name"); // 设置高亮的字段
        query.setHighlightSimplePre("<font color='red'>"); // 设置高亮的样式
        query.setHighlightSimplePost("</font>");

        List<QueryResponse> queryResponseList = new ArrayList<>(1);
        solrDefaultDaoImpl.listPage(httpSolrClient,page,query,queryResponseList);
        if(CollectionUtils.isNotEmpty(queryResponseList)){
            QueryResponse queryResponse = queryResponseList.get(0);
            if(CollectionUtils.isNotEmpty(queryResponse.getResults())){
                Map<String, Map<String, List<String>>> mapMap = queryResponse.getHighlighting();    // 高亮结果
                List<EnterpriseCustSearch> springDataSolrDemos = queryResponse.getBeans(EnterpriseCustSearch.class);
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
    public BaseDto listPageAsset(String companyId, AssetSearch assetSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap messageMap = new MessageMap();
        Page page = assetSearch.getPage();

        String keyWord = CommonUtils.null2String(assetSearch.getKeyWord());
        SolrQuery query = new SolrQuery();
        StringBuilder stringBuilder = new StringBuilder();
        if(StringUtils.isNotEmpty(keyWord)) {
            stringBuilder.append("(id:*"+keyWord+"* OR ");
            stringBuilder.append("building_code:*"+keyWord+"* OR ");
            stringBuilder.append("building_full_name:*"+keyWord+"* OR ");
            stringBuilder.append("house_code_new:*"+keyWord+"* OR ");
            stringBuilder.append("address:*"+keyWord+"*)");
            stringBuilder.append("AND is_hold : N");
            stringBuilder.append(" AND data_source : asset");
        }else {
            stringBuilder.append("*:*");
        }
        query.setQuery(stringBuilder.toString());
        query.set("collection", super.getCollection());
        query.setHighlight(true); // 设置高亮
        query.addHighlightField("building_name"); // 设置高亮的字段
        query.setHighlightSimplePre("<font color='red'>"); // 设置高亮的样式
        query.setHighlightSimplePost("</font>");

        List<QueryResponse> queryResponseList = new ArrayList<>(1);
        solrDefaultDaoImpl.listPage(httpSolrClient,page,query,queryResponseList);
        if(CollectionUtils.isNotEmpty(queryResponseList)){
            QueryResponse queryResponse = queryResponseList.get(0);
            if(CollectionUtils.isNotEmpty(queryResponse.getResults())){
                Map<String, Map<String, List<String>>> mapMap = queryResponse.getHighlighting();    // 高亮结果
                List<AssetSearch> springDataSolrDemos = queryResponse.getBeans(AssetSearch.class);
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
    public BaseDto listPagePersonConstruction(String companyId, ConstructionSearch constructionSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap messageMap = new MessageMap();
        Page page = constructionSearch.getPage();

        String keyWord = CommonUtils.null2String(constructionSearch.getKeyWord());
        SolrQuery query = new SolrQuery();
        StringBuilder stringBuilder = new StringBuilder();
        if(StringUtils.isNotEmpty(keyWord)) {
            stringBuilder.append("(id:*"+keyWord+"* OR ");
            stringBuilder.append("house_code_new:*"+keyWord+"* OR ");
            stringBuilder.append("construction_addr:*"+keyWord+"* OR ");
            stringBuilder.append("engineering_name:*"+keyWord+"*)");
            stringBuilder.append(" AND data_source : construction");
        }else {
            stringBuilder.append("*:*");
        }
        query.setQuery(stringBuilder.toString());
        query.set("collection", super.getCollection());
        query.setHighlight(true); // 设置高亮
        query.addHighlightField("costruction_addr"); // 设置高亮的字段
        query.setHighlightSimplePre("<font color='red'>"); // 设置高亮的样式
        query.setHighlightSimplePost("</font>");

        List<QueryResponse> queryResponseList = new ArrayList<>(1);
        solrDefaultDaoImpl.listPage(httpSolrClient,page,query,queryResponseList);
        if(CollectionUtils.isNotEmpty(queryResponseList)){
            QueryResponse queryResponse = queryResponseList.get(0);
            if(CollectionUtils.isNotEmpty(queryResponse.getResults())){
                Map<String, Map<String, List<String>>> mapMap = queryResponse.getHighlighting();    // 高亮结果
                List<ConstructionSearch> springDataSolrDemos = queryResponse.getBeans(ConstructionSearch.class);
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
    public BaseDto listPagePublicRental(String companyId, PublicRentalSearch publicRentalSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap messageMap = new MessageMap();
        Page page = publicRentalSearch.getPage();

        String keyWord = CommonUtils.null2String(publicRentalSearch.getKeyWord());
        SolrQuery query = new SolrQuery();
        StringBuilder stringBuilder = new StringBuilder();
        if(StringUtils.isNotEmpty(keyWord)) {
            stringBuilder.append("(id:*"+keyWord+"* OR ");
            stringBuilder.append("building_code:*"+keyWord+"* OR ");
            stringBuilder.append("building_full_name:*"+keyWord+"* OR ");
            stringBuilder.append("house_code_new:*"+keyWord+"* OR ");
            stringBuilder.append("address:*"+keyWord+"*)");
            stringBuilder.append(" AND is_hold : Y");
            stringBuilder.append(" AND data_source : publicRental");
        }else {
            stringBuilder.append("*:*");
        }
        query.setQuery(stringBuilder.toString());
        query.set("collection", super.getCollection());
        query.setHighlight(true); // 设置高亮
        query.addHighlightField("public_name"); // 设置高亮的字段
        query.setHighlightSimplePre("<font color='red'>"); // 设置高亮的样式
        query.setHighlightSimplePost("</font>");

        List<QueryResponse> queryResponseList = new ArrayList<>(1);
        solrDefaultDaoImpl.listPage(httpSolrClient,page,query,queryResponseList);
        if(CollectionUtils.isNotEmpty(queryResponseList)){
            QueryResponse queryResponse = queryResponseList.get(0);
            if(CollectionUtils.isNotEmpty(queryResponse.getResults())){
                Map<String, Map<String, List<String>>> mapMap = queryResponse.getHighlighting();    // 高亮结果
                List<PublicRentalSearch> springDataSolrDemos = queryResponse.getBeans(PublicRentalSearch.class);
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

}
