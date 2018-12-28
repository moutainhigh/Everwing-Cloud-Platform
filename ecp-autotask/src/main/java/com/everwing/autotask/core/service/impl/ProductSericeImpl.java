package com.everwing.autotask.core.service.impl;/**
 * Created by wust on 2018/12/18.
 */

import com.everwing.autotask.core.dao.TProductMapper;
import com.everwing.autotask.core.dao.TProductOrderDetailMapper;
import com.everwing.autotask.core.service.ProductService;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.product.*;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2018/12/18
 * @author wusongti@lii.com.cn
 */
@Service
public class ProductSericeImpl implements ProductService{

    @Autowired
    private TProductMapper tProductMapper;

    @Autowired
    private TProductOrderDetailMapper tProductOrderDetailMapper;

    /**
     * 找到已售并且失效的固定车位订单，修改为空置
     * @param companyId
     * @return
     */
    @Override
    public MessageMap fixedCarParkSalesStatusScheduleTask(String companyId) {
        // 根据产品编码集合获取产品信息，并将垂直结构的数据转换为横向结构
        TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
        tProductDetailSearch.setProductDetailTableId("productdetail_fixedCarPark");
        List<TProductDetailList> tProductDetailLists = tProductMapper.findProductDetailByCondition(tProductDetailSearch);
        if(CollectionUtils.isNotEmpty(tProductDetailLists)){
            for (TProductDetailList tProductDetailList : tProductDetailLists) {
                String projectId = tProductDetailList.getProjectId();
                String batchNo = tProductDetailList.getBatchNo();
                String productCode = tProductDetailList.getCode();
                String fieldName = tProductDetailList.getFieldName();
                if(ProductConstant.PRODUCTDETAIL_COLUMN_MARKET_STATE.equalsIgnoreCase(fieldName)){
                    String value = tProductDetailList.getFieldValue();
                    if(ProductConstant.MARKET_STATE_002.equals(value)){
                        List<TProductOrderDetailList> tProductOrderDetailLists = tProductOrderDetailMapper.findRecentProductOrderByProductCode(productCode);
                        if(CollectionUtils.isNotEmpty(tProductOrderDetailLists)){
                            TProductOrderDetailList tProductOrderDetailList = tProductOrderDetailLists.get(0);

                            DateTime buyBeginDateForDateTime = new DateTime(tProductOrderDetailList.getBuyBeginDate());
                            DateTime buyEndDateForDateTime = new DateTime(tProductOrderDetailList.getBuyEndDate()).plusDays(1);

                            Interval interval = new Interval(buyBeginDateForDateTime, buyEndDateForDateTime);
                            boolean contained = interval.contains(new DateTime());
                            if(contained) {// 在有效期范围
                                continue;
                            }

                            tProductDetailSearch.setCode(productCode);
                            List<TProductDetailList> productDetailLists = tProductMapper.findProductDetailByCondition(tProductDetailSearch);
                            if(CollectionUtils.isNotEmpty(productDetailLists)){
                                TProductDetailModify tProductDetailModify = new TProductDetailModify();
                                Map<String,String> fieldMap = new HashMap<>();
                                for (TProductDetailList productDetailList : productDetailLists) {
                                    if(ProductConstant.PRODUCTDETAIL_COLUMN_MARKET_STATE.equalsIgnoreCase(productDetailList.getFieldName())){
                                        fieldMap.put(productDetailList.getFieldId(),ProductConstant.MARKET_STATE_003); // 空置
                                    }else{
                                        fieldMap.put(productDetailList.getFieldId(),productDetailList.getFieldValue());
                                    }
                                }
                                fieldMap.put("productdetail_fixedCarPark_modify_time",new DateTime().toString());
                                tProductDetailModify.setFieldMap(fieldMap);
                                tProductDetailModify.setProjectId(projectId);
                                tProductDetailModify.setBatchNo(batchNo);
                                tProductDetailModify.setCode(productCode);
                                tProductMapper.modifyProductDetail(tProductDetailModify);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
