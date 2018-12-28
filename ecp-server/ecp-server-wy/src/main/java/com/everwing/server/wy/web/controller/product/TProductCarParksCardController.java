package com.everwing.server.wy.web.controller.product;/**
 * Created by wust on 2017/10/23.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.utils.generator.WyCodeGenerator;
import com.everwing.coreservice.common.wy.entity.common.attachment.Attachment;
import com.everwing.coreservice.common.wy.entity.product.ProductConstant;
import com.everwing.coreservice.common.wy.entity.product.TProductDetailInsert;
import com.everwing.coreservice.common.wy.entity.product.TProductDetailModify;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 *
 * Function:停车优惠卡产品
 * Reason:
 * Date:2017/10/23
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/TProductCarParksCardController")
public class TProductCarParksCardController extends TProductController{

    /**
     * 创建停车优惠卡产品
     * @param parameters
     * @return
     */
    @RequestMapping(value = "/createProductCarParksCard",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap createProductCarParksCard(@RequestBody Map<String,String> parameters){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        // 当前时间
        String nowDatetime = new DateTime().toString("yyyy-MM-dd HH:mm:ss");

        String projectId = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_PROJECT_ID));

        String batchNo =  WyCodeGenerator.genProductCode().replace("PD","");

        String name = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_NAME));

        String description = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_DESCRIPTION));

        String startTime = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_START_TIME));

        String endTime = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_END_TIME));

        String averagePrice = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE));

        String deposit = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT));

        String cardFee = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE));

        String isAssociatedBuyer = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER));

        String isAssociatedBuyerVehicle = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER_VEHICLE));

        String associateVehicleNumber = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_ASSOCIATE_VEHICLE_NUMBER));

        String preferentialType = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_PREFERENTIAL_TYPE));

        String taxrate = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_TAXRATE));

        String stock = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_STOCK));

        String productCode = WyCodeGenerator.genProductCode();


        List<Attachment> productAttachments = null;
        String fileStr = CommonUtils.null2String(parameters.get("fileStr"));
        if(StringUtils.isNotBlank(fileStr)){
            String[] files = fileStr.split(",");

            productAttachments = new ArrayList<>(files.length);

            for (String file : files) {
                String fileId = file.split("=")[0];
                String fileType = file.split("=")[1];

                Attachment tProductAttachment = new Attachment();
                tProductAttachment.setTableId("t_product_detail");
                tProductAttachment.setRelationId(productCode);
                tProductAttachment.setCreaterId(ctx.getUserId());
                tProductAttachment.setCreaterName(ctx.getStaffName());
                tProductAttachment.setAttachmentKey(fileId);
                tProductAttachment.setAttachmentType(fileType);
                productAttachments.add(tProductAttachment);
            }
        }



        /**
         * 产品详细字段值信息
         */
        List<Map<String,String>> productDetailList = new ArrayList<>(20);
        Map<String,String> productDetailFieldMap = new HashMap<>(20);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_PROJECT_ID,projectId);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_BATCH_NO,batchNo);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_CODE,productCode);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_NAME,"停车优惠卡" + DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(),"preferentialType",preferentialType) + (StringUtils.isNotBlank(name) ? "-" + name : ""));
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_DESCRIPTION,description);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_START_TIME,startTime);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_END_TIME,endTime);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE,StringUtils.isBlank(averagePrice) ? "0" : averagePrice);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT,StringUtils.isBlank(deposit) ? "0" : deposit);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE,StringUtils.isBlank(cardFee) ? "0" : cardFee);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER,isAssociatedBuyer);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER_VEHICLE,isAssociatedBuyerVehicle);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_ASSOCIATE_VEHICLE_NUMBER,associateVehicleNumber);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_PREFERENTIAL_TYPE,preferentialType);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_TAXRATE,taxrate);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_STOCK,stock);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_CREATER_ID,ctx.getUserId());
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_CREATER_NAME,ctx.getStaffName());
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_CREATE_TIME,nowDatetime);
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_ID,"");
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_NAME,"");
        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_TIME,"");
        productDetailList.add(productDetailFieldMap);


        TProductDetailInsert tProductInsert = new TProductDetailInsert();
        tProductInsert.setProductDetailTableId(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD);
        tProductInsert.setProjectId(projectId);
        tProductInsert.setBatchNo(batchNo);
        tProductInsert.setProductDetailList(productDetailList);
        tProductInsert.setProductAttachmentsCreates(productAttachments);

        RemoteModelResult<MessageMap> result = tProductApi.batchInsertProduct(ctx,tProductInsert);
        if(!result.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }

    /**
     * 修改产品信息
     * @param parameters
     * @return
     */
    @RequestMapping(value = "/modifyProductCarParksCard",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap modifyProductCarParksCard(@RequestBody Map<String,String> parameters) {
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String projectId = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_PROJECT_ID));

        String batchNo = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_BATCH_NO));

        String code = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_CODE));

        String fileStr = CommonUtils.null2String(parameters.get("fileStr"));

        /**
         * 处理上传的文件
         */
        List<Attachment>  productAttachments = null;
        if(StringUtils.isNotBlank(fileStr)){
            String[] files = fileStr.split(",");
            productAttachments = new ArrayList<>(files.length);
            for (String file : files) {
                String fileId = file.split("=")[0];
                String fileType = file.split("=")[1];

                Attachment tProductAttachment = new Attachment();
                tProductAttachment.setTableId("t_product_detail");
                tProductAttachment.setRelationId(code);
                tProductAttachment.setCreaterId(ctx.getUserId());
                tProductAttachment.setCreaterName(ctx.getStaffName());
                tProductAttachment.setAttachmentKey(fileId);
                tProductAttachment.setAttachmentType(fileType);
                productAttachments.add(tProductAttachment);
            }
        }

        // 更新字段值
        Map<String,String> fieldMap = new HashMap<>(parameters.size());
        Set<String> keys = parameters.keySet();
        for (String key : keys) {
            fieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + key,parameters.get(key));
        }
        fieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_ID,ctx.getUserId());
        fieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_NAME,ctx.getStaffName());
        fieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_CARPARKSCARD + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_TIME,new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

        TProductDetailModify tProductDetailModify = new TProductDetailModify();
        tProductDetailModify.setProjectId(projectId);
        tProductDetailModify.setBatchNo(batchNo);
        tProductDetailModify.setCode(code);
        tProductDetailModify.setFieldMap(fieldMap);
        tProductDetailModify.setProductAttachments(productAttachments);

        RemoteModelResult<MessageMap> remoteModelResult = tProductApi.modifyProductDetail(ctx,tProductDetailModify);
        if(!remoteModelResult.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(remoteModelResult.getMsg());
        }
        return mm;
    }
}
