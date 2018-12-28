package com.everwing.server.wy.web.controller.product;/**
 * Created by wust on 2017/10/23.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.generator.WyCodeGenerator;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.entity.common.attachment.Attachment;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditor;
import com.everwing.coreservice.common.wy.entity.product.*;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.wy.api.building.TcBuildingApi;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 *
 * Function:固定车位产品
 * Reason:
 * Date:2017/10/23
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/TProductFixedCarParkController")
public class TProductFixedCarParkController extends TProductController{

    @Autowired
    private TcBuildingApi tcBuildingApi;


    /**
     * 创建固定车位产品
     * @param parameters
     * @return
     */
    @RequestMapping(value = "/createProductFixedCarPark",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap createProductFixedCarPark(@RequestBody Map<String, String> parameters){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String nowDatetime = new DateTime().toString("yyyy-MM-dd HH:mm:ss");

        String projectId = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_PROJECT_ID));

        String batchNo =  WyCodeGenerator.genProductCode();

        String fixedParkingSpaceJSONStr = CommonUtils.null2String(parameters.get("fixedParkingSpaceJSONStr"));


        JSONArray fixedParkingSpaceJSONArray = JSONArray.parseArray(fixedParkingSpaceJSONStr);
        if(fixedParkingSpaceJSONArray == null || fixedParkingSpaceJSONArray.size() < 1){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("请选择资产。");
            return mm;
        }

        String uploadFileIdJSONStr = CommonUtils.null2String(parameters.get("fileList"));

        // 富文本内容
        String quillEditorText = CommonUtils.null2String(parameters.get("quillEditorText"));

        // 附件
        List<Attachment>  productAttachments = new ArrayList<>(50);

        // 富文本
        List<TQuillEditor>  tQuillEditors = new ArrayList<>(10);

        /**
         * 产品详细字段值信息
         */
        List<Map<String,String>> productDetailList = new ArrayList<>(20);
        for (Object obj : fixedParkingSpaceJSONArray) {
            JSONObject jsonObject = (JSONObject)obj;

            String productCode =  WyCodeGenerator.genProductCode();
            String name = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_NAME));
            String startTime = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_START_TIME));
            String endTime = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_END_TIME));
            String averagePrice = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE));
            String deposit = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT));
            String cardFee = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE));
            String isAssociatedBuyer = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER));
            String isAssociatedBuyerVehicle = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER_VEHICLE));
            String associateVehicleNumber = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_ASSOCIATE_VEHICLE_NUMBER));
            String taxrate = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_TAXRATE));
            String houseCodeNew = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_HOUSE_CODE_NEW));
            String isCalendarMonthSale = CommonUtils.null2String(jsonObject.get(ProductConstant.PRODUCTDETAIL_COLUMN_IS_CALENDAR_MONTH_SALE));

            Map<String,String> productDetailFieldMap = new HashMap<>(20);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_PROJECT_ID,projectId);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_BATCH_NO,batchNo);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_CODE,"");
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_NAME,"");
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_DESCRIPTION,"");
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_START_TIME,startTime);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_END_TIME,endTime);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_HOUSE_CODE,"");
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_HOUSE_CODE_NEW,"");
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_AVERAGE_PRICE,StringUtils.isBlank(averagePrice) ? "0" : averagePrice);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_DEPOSIT,StringUtils.isBlank(deposit) ? "0" : deposit);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_CARD_FEE,StringUtils.isBlank(cardFee) ? "0" : cardFee);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER,isAssociatedBuyer);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_IS_ASSOCIATED_BUYER_VEHICLE,isAssociatedBuyerVehicle);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_ASSOCIATE_VEHICLE_NUMBER,associateVehicleNumber);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_TAXRATE,taxrate);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_CODE,productCode);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_NAME,"固定车位" + name);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_HOUSE_CODE_NEW,houseCodeNew);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_STOCK,"1");
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MARKET_STATE,ProductConstant.MARKET_STATE_001);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MASTER_PICTURE,"");
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_IS_CALENDAR_MONTH_SALE,isCalendarMonthSale);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_CREATER_ID,ctx.getUserId());
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_CREATER_NAME,ctx.getStaffName());
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_CREATE_TIME,nowDatetime);
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_ID,"");
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_NAME,"");
            productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_TIME,"");
            productDetailList.add(productDetailFieldMap);


            // 富文本
            if(StringUtils.isNotBlank(CommonUtils.null2String(quillEditorText))){
                String quillEditorTextTemp = quillEditorText;
                /*List<String> imgURLList = HtmlUtil.extractImg(quillEditorTextTemp);
                if(CollectionUtils.isNotEmpty(imgURLList)){
                    for (String s : imgURLList) {
                        File file = decodeBase64ToImage(s,ctx.getLoginName());
                        RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.uploadFile(file);
                        if(remoteModelResult.isSuccess()){
                            UploadFile uploadFile = remoteModelResult.getModel();
                            if(uploadFile == null){
                                continue;
                            }
                            quillEditorTextTemp = quillEditorTextTemp.replace(s,ctx.getContextPath() + "/CommonController/showImageBySize/" + uploadFile.getUploadFileId() + "/600/500");
                        }
                    }
                    TQuillEditor tQuillEditor = new TQuillEditor();
                    tQuillEditor.setTableId("t_product_detail");
                    tQuillEditor.setRelationId(productCode);
                    tQuillEditor.setHtmlContent(quillEditorTextTemp);
                    tQuillEditor.setCreaterId(ctx.getUserId());
                    tQuillEditor.setCreaterName(ctx.getStaffName());
                    tQuillEditors.add(tQuillEditor);
                }*/
                TQuillEditor tQuillEditor = new TQuillEditor();
                tQuillEditor.setTableId("t_product_detail");
                tQuillEditor.setRelationId(productCode);
                tQuillEditor.setHtmlContent(quillEditorTextTemp);
                tQuillEditor.setCreaterId(ctx.getUserId());
                tQuillEditor.setCreaterName(ctx.getStaffName());
                tQuillEditors.add(tQuillEditor);
            }


            // 附件
            JSONArray uploadFileIdJsonArray = JSONArray.parseArray(uploadFileIdJSONStr);
            if(uploadFileIdJsonArray != null && uploadFileIdJsonArray.size() > 0){
                for(int i=0;i<uploadFileIdJsonArray.size();i++){
                    String uploadFileId = uploadFileIdJsonArray.getString(i);
                    if(StringUtils.isBlank(CommonUtils.null2String(uploadFileId))){
                        continue;
                    }
                    Attachment tProductAttachment = new Attachment();
                    tProductAttachment.setTableId("t_product_detail");
                    tProductAttachment.setRelationId(productCode);
                    tProductAttachment.setCreaterId(ctx.getUserId());
                    tProductAttachment.setCreaterName(ctx.getStaffName());
                    tProductAttachment.setAttachmentKey(uploadFileId);
                    productAttachments.add(tProductAttachment);
                    if(i == 0){ // 设置产品列表展示主图
                        productDetailFieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MASTER_PICTURE,uploadFileId);
                    }
                }
            }
        }



        TProductDetailInsert tProductInsert = new TProductDetailInsert();
        tProductInsert.setProductDetailTableId(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK);
        tProductInsert.setProjectId(projectId);
        tProductInsert.setBatchNo(batchNo);
        tProductInsert.setFieldMap(null);
        tProductInsert.setProductDetailList(productDetailList);
        tProductInsert.setProductAttachmentsCreates(productAttachments);
        tProductInsert.settQuillEditors(tQuillEditors);

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
    @RequestMapping(value = "/modifyProductFixedCarPark",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap modifyProductFixedCarPark(@RequestBody Map<String,String> parameters) {
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        String projectId = CommonUtils.null2String(parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_PROJECT_ID));

        String batchNo = parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_BATCH_NO);

        String code = parameters.get(ProductConstant.PRODUCTDETAIL_COLUMN_CODE);

        // 上传的主图id集合
        String uploadFileIdJSONStr = CommonUtils.null2String(parameters.get("fileList"));

        // 富文本内容
        String quillEditorText = CommonUtils.null2String(parameters.get("quillEditorText"));

        // 附件
        List<Attachment>  productAttachments = new ArrayList<>(5);

        // 富文本
        List<TQuillEditor>  tQuillEditors = new ArrayList<>(10);


        // 更新字段值
        Map<String,String> fieldMap = new HashMap<>(parameters.size());
        Set<String> keys = parameters.keySet();
        for (String key : keys) {
            fieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + key,parameters.get(key));
        }
        fieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_ID,ctx.getUserId());
        fieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_NAME,ctx.getStaffName());
        fieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MODIFY_TIME,new DateTime().toString("yyyy-MM-dd HH:mm:ss"));



        // 富文本
        if(StringUtils.isNotBlank(CommonUtils.null2String(quillEditorText))){
            String quillEditorTextTemp = quillEditorText;
            /*List<String> imgURLList = HtmlUtil.extractImg(quillEditorTextTemp);
            if(CollectionUtils.isNotEmpty(imgURLList)){
                for (String s : imgURLList) {
                    if(!s.toLowerCase().contains("base64")){
                        continue;
                    }
                    File file = decodeBase64ToImage(s,ctx.getLoginName());
                    RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.uploadFile(file);
                    if(remoteModelResult.isSuccess()){
                        UploadFile uploadFile = remoteModelResult.getModel();
                        if(uploadFile != null){
                            quillEditorTextTemp = quillEditorTextTemp.replace(s,ctx.getContextPath() + "/CommonController/showImageBySize/" + uploadFile.getUploadFileId() + "/600/500");
                        }
                    }
                }
                TQuillEditor tQuillEditor = new TQuillEditor();
                tQuillEditor.setTableId("t_product_detail");
                tQuillEditor.setRelationId(code);
                tQuillEditor.setHtmlContent(quillEditorTextTemp);
                tQuillEditor.setCreaterId(ctx.getUserId());
                tQuillEditor.setCreaterName(ctx.getStaffName());
                tQuillEditors.add(tQuillEditor);
            }*/
            TQuillEditor tQuillEditor = new TQuillEditor();
            tQuillEditor.setTableId("t_product_detail");
            tQuillEditor.setRelationId(code);
            tQuillEditor.setHtmlContent(quillEditorTextTemp);
            tQuillEditor.setCreaterId(ctx.getUserId());
            tQuillEditor.setCreaterName(ctx.getStaffName());
            tQuillEditors.add(tQuillEditor);
        }else{
            TQuillEditor tQuillEditor = new TQuillEditor();
            tQuillEditor.setTableId("t_product_detail");
            tQuillEditor.setRelationId(code);
            tQuillEditor.setHtmlContent("");
            tQuillEditor.setCreaterId(ctx.getUserId());
            tQuillEditor.setCreaterName(ctx.getStaffName());
            tQuillEditors.add(tQuillEditor);
        }


        // 附件
        JSONArray uploadFileIdJsonArray = JSONArray.parseArray(uploadFileIdJSONStr);
        if(uploadFileIdJsonArray != null && uploadFileIdJsonArray.size() > 0){
            for(int i=0;i<uploadFileIdJsonArray.size();i++){
                String uploadFileId = uploadFileIdJsonArray.getString(i);
                Attachment tProductAttachment = new Attachment();
                tProductAttachment.setTableId("t_product_detail");
                tProductAttachment.setRelationId(code);
                tProductAttachment.setCreaterId(ctx.getUserId());
                tProductAttachment.setCreaterName(ctx.getStaffName());
                tProductAttachment.setAttachmentKey(uploadFileId);
                productAttachments.add(tProductAttachment);
                if(i == 0){ // 设置产品列表展示主图
                    fieldMap.put(ProductConstant.TABLE_ID_PRODUCT_DETAIL_FIXEDCARPARK + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_MASTER_PICTURE,uploadFileId);
                }
            }
        }


        TProductDetailModify tProductDetailModify = new TProductDetailModify();
        tProductDetailModify.setProjectId(projectId);
        tProductDetailModify.setBatchNo(batchNo);
        tProductDetailModify.setCode(code);
        tProductDetailModify.setFieldMap(fieldMap);
        tProductDetailModify.setProductAttachments(productAttachments);
        tProductDetailModify.settQuillEditors(tQuillEditors);

        RemoteModelResult<MessageMap> remoteModelResult = tProductApi.modifyProductDetail(ctx,tProductDetailModify);
        if(!remoteModelResult.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(remoteModelResult.getMsg());
        }
        return mm;
    }




    /**
     * 一次性获取固定车位
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/getFixedParkingSpaceTree/{projectId}", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto getFixedParkingSpaceTree(@PathVariable String projectId) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        TcBuildingSearch tcBuildingSearch = new TcBuildingSearch();
        tcBuildingSearch.setProjectId(projectId);
        tcBuildingSearch.setBuildingType(LookupItemEnum.buildingType_parkspace.getStringValue());
        tcBuildingSearch.setIsFixedParkingSpaces(LookupItemEnum.yesNo_yes.getStringValue());
        RemoteModelResult<List<TcBuildingList>> remoteModelResultParkSpaceList = this.tcBuildingApi.findAllBuildingNodeByCondition(ctx,tcBuildingSearch);
        if (remoteModelResultParkSpaceList.isSuccess()) {
            List<TcBuildingList> tcBuildingLists = remoteModelResultParkSpaceList.getModel();
            if(CollectionUtils.isNotEmpty(tcBuildingLists)){
                List<TProductDetailList> tProductDetailLists = null;
                TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
                RemoteModelResult<List<TProductDetailList>> remoteModelResult = tProductApi.findProductDetailByCondition(ctx,tProductDetailSearch);
                if(remoteModelResult.isSuccess()){
                    tProductDetailLists = remoteModelResult.getModel();
                }
                JSONArray jsonArray = treeBuilder(tcBuildingLists,tProductDetailLists);
                baseDto.setObj(jsonArray.toJSONString());
            }else{
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("该项目没有任何固定车位数据");
            }
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResultParkSpaceList.getMsg());
        }

        baseDto.setMessageMap(mm);
        return baseDto;
    }


    private JSONArray treeBuilder(final List<TcBuildingList> allList,List<TProductDetailList> existFixedCarParkList){
        JSONArray jsonArray = new JSONArray(allList.size());
        if(CollectionUtils.isNotEmpty(allList)){
            Map<String,String> exist = new HashMap<>(allList.size());
            for (TcBuildingList tcBuildingList : allList) {
                String rootCode = tcBuildingList.getJzjgCode();
                String rootName = tcBuildingList.getJzjgName();
                if(!exist.containsKey(rootCode)){
                    exist.put(rootCode,"");
                    JSONObject rootJsonObject = new JSONObject();
                    rootJsonObject.put("id",rootCode);
                    rootJsonObject.put("pId",null);
                    rootJsonObject.put("name",rootName);
                    rootJsonObject.put("fullname","");
                    rootJsonObject.put("houseCode","");
                    rootJsonObject.put("houseCodeNew","");
                    rootJsonObject.put("buildingType",tcBuildingList.getBuildingType());
                    rootJsonObject.put("isParent",true);
                    rootJsonObject.put("nocheck",true);
                    rootJsonObject.put("open",true);
                    jsonArray.add(rootJsonObject);
                }

                String qiCode = tcBuildingList.getQiCode();
                String qiName = tcBuildingList.getQiName();
                if(!exist.containsKey(qiCode)) {
                    exist.put(qiCode,"");
                    JSONObject qiJsonObject = new JSONObject();
                    qiJsonObject.put("id", qiCode);
                    qiJsonObject.put("pId", rootCode);
                    qiJsonObject.put("name", qiName);
                    qiJsonObject.put("fullname", "");
                    qiJsonObject.put("houseCode", "");
                    qiJsonObject.put("houseCodeNew", "");
                    qiJsonObject.put("buildingType", tcBuildingList.getBuildingType());
                    qiJsonObject.put("isParent", true);
                    qiJsonObject.put("nocheck",true);
                    jsonArray.add(qiJsonObject);
                }

                String quCode = tcBuildingList.getQuCode();
                String quName = tcBuildingList.getQuName();
                if(!exist.containsKey(quCode)) {
                    exist.put(quCode,"");
                    JSONObject quJsonObject = new JSONObject();
                    quJsonObject.put("id", quCode);
                    quJsonObject.put("pId", qiCode);
                    quJsonObject.put("name", quName);
                    quJsonObject.put("fullname", "");
                    quJsonObject.put("houseCode", "");
                    quJsonObject.put("houseCodeNew", "");
                    quJsonObject.put("buildingType", tcBuildingList.getBuildingType());
                    quJsonObject.put("isParent", true);
                    quJsonObject.put("nocheck",true);
                    jsonArray.add(quJsonObject);
                }

                String dongzuoCode = tcBuildingList.getDongzuoCode();
                String dongzuoName = tcBuildingList.getDongzuoName();
                if(!exist.containsKey(dongzuoCode)) {
                    exist.put(dongzuoCode, "");
                    JSONObject dongzuoJsonObject = new JSONObject();
                    dongzuoJsonObject.put("id", dongzuoCode);
                    dongzuoJsonObject.put("pId", quCode);
                    dongzuoJsonObject.put("name", dongzuoName);
                    dongzuoJsonObject.put("fullname", "");
                    dongzuoJsonObject.put("houseCode", "");
                    dongzuoJsonObject.put("houseCodeNew", "");
                    dongzuoJsonObject.put("buildingType", tcBuildingList.getBuildingType());
                    dongzuoJsonObject.put("isParent", true);
                    dongzuoJsonObject.put("nocheck",true);
                    jsonArray.add(dongzuoJsonObject);
                }

                String danyuanrukouCode = tcBuildingList.getDanyuanrukouCode();
                String danyuanrukouName = tcBuildingList.getDanyuanrukouName();
                if(!exist.containsKey(danyuanrukouCode)) {
                    exist.put(danyuanrukouCode, "");
                    JSONObject danyuanrukouJsonObject = new JSONObject();
                    danyuanrukouJsonObject.put("id", danyuanrukouCode);
                    danyuanrukouJsonObject.put("pId", dongzuoCode);
                    danyuanrukouJsonObject.put("name", danyuanrukouName);
                    danyuanrukouJsonObject.put("fullname", "");
                    danyuanrukouJsonObject.put("houseCode", "");
                    danyuanrukouJsonObject.put("houseCodeNew", "");
                    danyuanrukouJsonObject.put("buildingType", tcBuildingList.getBuildingType());
                    danyuanrukouJsonObject.put("isParent", true);
                    danyuanrukouJsonObject.put("nocheck",true);
                    jsonArray.add(danyuanrukouJsonObject);
                }

                String cengCode = tcBuildingList.getCengCode();
                String cengName = tcBuildingList.getCengName();
                JSONObject cengJsonObject = new JSONObject();
                if(!exist.containsKey(cengCode)) {
                    exist.put(cengCode, "");
                    cengJsonObject.put("id", cengCode);
                    cengJsonObject.put("pId", danyuanrukouCode);
                    cengJsonObject.put("name", cengName);
                    cengJsonObject.put("fullname", "");
                    cengJsonObject.put("houseCode", "");
                    cengJsonObject.put("houseCodeNew", "");
                    cengJsonObject.put("buildingType", tcBuildingList.getBuildingType());
                    cengJsonObject.put("isParent", true);
                    cengJsonObject.put("nocheck",true);
                    jsonArray.add(cengJsonObject);
                }

                String leafCode = tcBuildingList.getBuildingCode();
                String leafName = tcBuildingList.getBuildingName();
                String leafFullName = tcBuildingList.getBuildingFullName();
                String houseCode = tcBuildingList.getHouseCode();
                String houseCodeNew = tcBuildingList.getHouseCodeNew();
                if(!exist.containsKey(leafCode)) {
                    exist.put(leafCode, "");
                    JSONObject leafJsonObject = new JSONObject();
                    leafJsonObject.put("id", leafCode);
                    leafJsonObject.put("pId", cengCode);
                    leafJsonObject.put("name", leafName + "|" + houseCodeNew);
                    leafJsonObject.put("fullname", leafFullName);
                    leafJsonObject.put("houseCode", houseCode);
                    leafJsonObject.put("houseCodeNew", houseCodeNew);
                    leafJsonObject.put("buildingType", tcBuildingList.getBuildingType());
                    leafJsonObject.put("isParent", false);

                    if(CollectionUtils.isNotEmpty(existFixedCarParkList)){
                        for (TProductDetailList tProductDetailList : existFixedCarParkList) {
                            String houseCodeNewFromProduct = tProductDetailList.getFieldValue();
                            if(houseCodeNew.equals(houseCodeNewFromProduct)){
                                // 已经创建过固定车位产品的车位不能够再选
                                leafJsonObject.put("nocheck",true);
                            }
                        }
                    }
                    jsonArray.add(leafJsonObject);
                }
            }
        }
        return jsonArray;
    }
}
