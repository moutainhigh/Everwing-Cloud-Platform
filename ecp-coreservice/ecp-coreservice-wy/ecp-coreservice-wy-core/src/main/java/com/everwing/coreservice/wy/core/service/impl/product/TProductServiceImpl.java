package com.everwing.coreservice.wy.core.service.impl.product;/**
 * Created by wust on 2017/8/30.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.entity.common.attachment.Attachment;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditor;
import com.everwing.coreservice.common.wy.entity.product.*;
import com.everwing.coreservice.common.wy.service.product.TProductService;
import com.everwing.coreservice.wy.dao.mapper.common.AttachmentMapper;
import com.everwing.coreservice.wy.dao.mapper.common.TQuillEditorMapper;
import com.everwing.coreservice.wy.dao.mapper.product.TProductMapper;
import com.everwing.coreservice.wy.dao.mapper.product.TProductOrderDetailMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/30
 * @author wusongti@lii.com.cn
 */
@Service("tProductServiceImpl")
public class TProductServiceImpl implements TProductService {
    @Autowired
    private TProductMapper tProductMapper;


    @Autowired
    private AttachmentMapper tProductAttachmentMapper;

    @Autowired
    private TQuillEditorMapper tQuillEditorMapper;

    @Autowired
    private TProductOrderDetailMapper tProductOrderDetailMapper;



    @Override
    public BaseDto listPageProductDetail(WyBusinessContext ctx, TProductDetailSearch tProductDetailSearch) {
        BaseDto baseDto = new BaseDto<>();

        List<String> productCodeList = null;
        List<TProductDetailList> list = tProductMapper.listPageProductDetail(tProductDetailSearch);
        if(CollectionUtils.isNotEmpty(list)){
            // 得到分页结果的产品编码
            productCodeList = new ArrayList<>(list.size());
            for (TProductDetailList tProductDetailList : list) {
                productCodeList.add(tProductDetailList.getCode());
            }

            // 根据产品编码集合获取产品信息，并将垂直结构的数据转换为横向结构
            TProductDetailSearch tProductDetailSearch1 = new TProductDetailSearch();
            tProductDetailSearch1.setProductCodeList(productCodeList);
            List<TProductDetailList> tProductDetailLists = tProductMapper.findProductDetailByCondition(tProductDetailSearch1);
            if(CollectionUtils.isNotEmpty(tProductDetailLists)){
                String preKey = "";
                ArrayList<JSONObject> jsonObjects = new ArrayList<>(20);
                JSONObject jsonObject = null;
                for (TProductDetailList tProductDetailList : tProductDetailLists) {
                    String curKey = tProductDetailList.getProjectId() + tProductDetailList.getBatchNo() + tProductDetailList.getCode();
                    if(StringUtils.isBlank(preKey)){
                        preKey = curKey;
                        jsonObject = new JSONObject();
                    }else if(!preKey.equals(curKey)){
                        // 设置购买、上下架和编辑状态
                        jsonObject.putAll(TProductDetailList.setProductSalesAuthorization(jsonObject));
                        jsonObject.putAll(TProductDetailList.setProductModifyAuthorization(jsonObject));
                        jsonObjects.add(jsonObject);

                        preKey = curKey;
                        jsonObject = new JSONObject();
                    }

                    if(!jsonObject.containsKey(tProductDetailList.getFieldName())){
                        jsonObject.put(tProductDetailList.getFieldName(), tProductDetailList.getFieldValue());
                    }

                    /**
                     * 获取销售状态label
                     */
                    if(ProductConstant.PRODUCTDETAIL_COLUMN_MARKET_STATE.equalsIgnoreCase(tProductDetailList.getFieldName())) {
                        String fixedCarParkSalesStatusName = DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(), "fixedCarParkSalesStatus", tProductDetailList.getFieldValue());
                        jsonObject.put("market_state_name", fixedCarParkSalesStatusName);
                    }
                }

                // 别漏了最后行数据噢
                if(jsonObject != null && jsonObject.size() > 0){
                    // 设置购买、上下架和编辑状态
                    jsonObject.putAll(TProductDetailList.setProductSalesAuthorization(jsonObject));
                    jsonObject.putAll(TProductDetailList.setProductModifyAuthorization(jsonObject));
                    jsonObjects.add(jsonObject);
                }

                baseDto.setLstDto(jsonObjects);
            }
        }

        // 设置分页信息
        baseDto.setPage(tProductDetailSearch.getPage());
        return baseDto ;
    }


    @Override
    public List<TProductDetailList> findProductDetailByCondition(WyBusinessContext ctx, TProductDetailSearch tProductDetailSearch) {
        return tProductMapper.findProductDetailByCondition(tProductDetailSearch);
    }

    @Override
    public BaseDto listPageProductSaleHistory(WyBusinessContext ctx, ProductSaleHistorySearch productSaleHistorySearch) {
        List<ProductSaleHistoryList> productSaleHistoryLists = tProductMapper.listPageProductSaleHistory(productSaleHistorySearch);
        BaseDto baseDto = new BaseDto();
        baseDto.setPage(productSaleHistorySearch.getPage());
        baseDto.setLstDto(productSaleHistoryLists);
        return baseDto;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap batchInsertProduct(WyBusinessContext ctx, TProductDetailInsert tProductInsert) {
        MessageMap mm = new MessageMap();

        // 附件
        List<Attachment> tProductAttachments = tProductInsert.getProductAttachmentsCreates();
        if(CollectionUtils.isNotEmpty(tProductAttachments)){
            tProductAttachmentMapper.batchInsert(tProductAttachments);
        }

        // 富文本
        if(CollectionUtils.isNotEmpty(tProductInsert.gettQuillEditors())){
            tQuillEditorMapper.batchInsert(tProductInsert.gettQuillEditors());
        }

        // 检验产品是否已经存在
        if(ProductConstant.TABLE_ID_PRODUCT_DETAIL_BUILDINGLEASE.equalsIgnoreCase(tProductInsert.getProductDetailTableId())){
            // 建筑租赁
            List<Map<String, String>> list = tProductInsert.getProductDetailList();
            for (Map<String, String> map : list) {
                String buildingCode = map.get("productdetail_buildingLease_building_code");
                TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
                tProductDetailSearch.setProjectId(tProductInsert.getProjectId());
                tProductDetailSearch.setProductDetailTableId(tProductInsert.getProductDetailTableId());
                tProductDetailSearch.setFieldId("productdetail_buildingLease_building_code");
                tProductDetailSearch.setFieldValue(buildingCode);
                List<TProductDetailList> tProductDetailLists = tProductMapper.findProductDetailByCondition(tProductDetailSearch);
                if(CollectionUtils.isNotEmpty(tProductDetailLists)){
                    throw new ECPBusinessException("该产品已经存在");
                }
            }
        }else if(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD.equalsIgnoreCase(tProductInsert.getProductDetailTableId())){
            // 停车优惠卡
            List<Map<String, String>> list = tProductInsert.getProductDetailList();
            for (Map<String, String> map : list) {
                String productName = map.get("productdetail_carParksCard_name");
                TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
                tProductDetailSearch.setProjectId(tProductInsert.getProjectId());
                tProductDetailSearch.setProductDetailTableId(tProductInsert.getProductDetailTableId());
                tProductDetailSearch.setFieldId("productdetail_carParksCard_name");
                tProductDetailSearch.setFieldValue(productName);
                List<TProductDetailList> tProductDetailLists = tProductMapper.findProductDetailByCondition(tProductDetailSearch);
                if(CollectionUtils.isNotEmpty(tProductDetailLists)){
                    throw new ECPBusinessException("该产品已经存在");
                }
            }
        }else if(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK.equalsIgnoreCase(tProductInsert.getProductDetailTableId())){
            // 固定车位
            List<Map<String, String>> list = tProductInsert.getProductDetailList();
            for (Map<String, String> map : list) {
                String buildingCode = map.get("productdetail_fixedCarPark_building_code");
                TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
                tProductDetailSearch.setProjectId(tProductInsert.getProjectId());
                tProductDetailSearch.setProductDetailTableId(tProductInsert.getProductDetailTableId());
                tProductDetailSearch.setFieldId("productdetail_fixedCarPark_building_code");
                tProductDetailSearch.setFieldValue(buildingCode);
                List<TProductDetailList> tProductDetailLists = tProductMapper.findProductDetailByCondition(tProductDetailSearch);
                if(CollectionUtils.isNotEmpty(tProductDetailLists)){
                    throw new ECPBusinessException("该产品已经存在");
                }
            }
        }else if(ProductConstant.TABLE_ID_PRODUCT_DETAIL_ENTRANCEGUARDCARD.equalsIgnoreCase(tProductInsert.getProductDetailTableId())){
            // 门禁卡
            List<Map<String, String>> list = tProductInsert.getProductDetailList();
            for (Map<String, String> map : list) {
                String productName = map.get("productdetail_entranceGuardCard_name");
                TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
                tProductDetailSearch.setProjectId(tProductInsert.getProjectId());
                tProductDetailSearch.setProductDetailTableId(tProductInsert.getProductDetailTableId());
                tProductDetailSearch.setFieldId("productdetail_entranceGuardCard_name");
                tProductDetailSearch.setFieldValue(productName);
                List<TProductDetailList> tProductDetailLists = tProductMapper.findProductDetailByCondition(tProductDetailSearch);
                if(CollectionUtils.isNotEmpty(tProductDetailLists)){
                    throw new ECPBusinessException("该产品已经存在");
                }
            }
        }else if(ProductConstant.PRODUCTTYPE_COMMONSERVICE.equalsIgnoreCase(tProductInsert.getProductDetailTableId())){
            // 普通服务
            List<Map<String, String>> list = tProductInsert.getProductDetailList();
            for (Map<String, String> map : list) {
                String productName = map.get("productdetail_commonService_name");
                TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
                tProductDetailSearch.setProjectId(tProductInsert.getProjectId());
                tProductDetailSearch.setProductDetailTableId(tProductInsert.getProductDetailTableId());
                tProductDetailSearch.setFieldId("productdetail_commonService_name");
                tProductDetailSearch.setFieldValue(productName);
                List<TProductDetailList> tProductDetailLists = tProductMapper.findProductDetailByCondition(tProductDetailSearch);
                if(CollectionUtils.isNotEmpty(tProductDetailLists)){
                    throw new ECPBusinessException("该产品已经存在");
                }
            }
        }

        // 产品明细
        if(CollectionUtils.isNotEmpty(tProductInsert.getProductDetailList())){
            tProductMapper.batchInsertProductDetail(tProductInsert);
        }


        return mm;
    }



    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap modifyProductDetail(WyBusinessContext ctx, TProductDetailModify tProductDetailModify) {
        MessageMap mm = new MessageMap();

        // 更新附件
        if(CollectionUtils.isNotEmpty(tProductDetailModify.getProductAttachments())){
            Attachment attachment = new Attachment();
            attachment.setTableId("t_product_detail");
            attachment.setRelationId(tProductDetailModify.getCode());
            tProductAttachmentMapper.delete(attachment);
            tProductAttachmentMapper.batchInsert(tProductDetailModify.getProductAttachments());
        }

        // 更新富文本
        if(CollectionUtils.isNotEmpty(tProductDetailModify.gettQuillEditors())){
            TQuillEditor tQuillEditor = new TQuillEditor();
            tQuillEditor.setTableId("t_product_detail");
            tQuillEditor.setRelationId(tProductDetailModify.getCode());
            tQuillEditorMapper.delete(tQuillEditor);
            tQuillEditorMapper.batchInsert(tProductDetailModify.gettQuillEditors());
        }

        tProductMapper.modifyProductDetail(tProductDetailModify);
        return mm;
    }

    @Override
    public MessageMap shelve(WyBusinessContext ctx, TProductDetailModify tProductDetailModify) {
        MessageMap mm = new MessageMap();

        TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
        tProductDetailSearch.setProjectId(tProductDetailModify.getProjectId());
        tProductDetailSearch.setBatchNo(tProductDetailModify.getBatchNo());
        tProductDetailSearch.setCode(tProductDetailModify.getCode());
        List<TProductDetailList> tProductDetailLists = tProductMapper.findProductDetailByCondition(tProductDetailSearch);
        if(CollectionUtils.isNotEmpty(tProductDetailLists)){
            tProductMapper.modifyProductDetail(tProductDetailModify);
        }
        return mm;
    }

    @Override
    public MessageMap normalOffShelve(WyBusinessContext ctx, TProductDetailModify tProductDetailModify) {
        MessageMap mm = new MessageMap();

        TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
        tProductDetailSearch.setProjectId(tProductDetailModify.getProjectId());
        tProductDetailSearch.setBatchNo(tProductDetailModify.getBatchNo());
        tProductDetailSearch.setCode(tProductDetailModify.getCode());
        List<TProductDetailList> tProductDetailLists = tProductMapper.findProductDetailByCondition(tProductDetailSearch);
        if(CollectionUtils.isNotEmpty(tProductDetailLists)){
            tProductMapper.modifyProductDetail(tProductDetailModify);
        }
        return mm;
    }

    @Override
    public MessageMap deleteProductDetailByProductCode(WyBusinessContext ctx, String projectId, String batchNo, String productCode) {
        MessageMap mm = new MessageMap();

        TProductOrderDetailSearch tProductOrderDetailSearch = new TProductOrderDetailSearch();
        tProductOrderDetailSearch.setProductCode(productCode);
        List<TProductOrderDetailList> tProductOrderDetailLists = tProductOrderDetailMapper.findByCondition(tProductOrderDetailSearch);
        if(CollectionUtils.isEmpty(tProductOrderDetailLists)){
            tProductMapper.deleteProductDetailByProductCode(projectId,batchNo,productCode);

            // 删除附件表对应的数据
            Attachment attachment = new Attachment();
            attachment.setTableId("t_product_detail");
            attachment.setRelationId(productCode);
            tProductAttachmentMapper.delete(attachment);


            // 删除富文本表对应的数据
            TQuillEditor tQuillEditor = new TQuillEditor();
            tQuillEditor.setTableId("t_product_detail");
            tQuillEditor.setRelationId(productCode);
            tQuillEditorMapper.delete(tQuillEditor);
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("该产品有销售记录，不允许删除。");
        }
        return mm;
    }
}
