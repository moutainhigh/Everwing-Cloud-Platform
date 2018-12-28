package com.everwing.server.wy.web.controller.product;/**
 * Created by wust on 2017/8/30.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.common.attachment.AttachmentList;
import com.everwing.coreservice.common.wy.entity.common.attachment.AttachmentSearch;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditorList;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditorSearch;
import com.everwing.coreservice.common.wy.entity.product.*;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.api.common.AttachmentApi;
import com.everwing.coreservice.wy.api.common.QuillEditorApi;
import com.everwing.coreservice.wy.api.product.TProductApi;
import com.everwing.coreservice.wy.api.product.TProductOrderApi;
import com.everwing.coreservice.wy.api.sys.TSysLookupApi;
import com.everwing.utils.CreateFileUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * Function:产品管理公共控制器
 * Reason:各种产品类型可以共享的方法
 * Date:2017/8/30
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/TProductController")
public class TProductController {
    static Logger logger = LogManager.getLogger(TProductController.class);


    @Autowired
    protected TProductApi tProductApi;

    @Autowired
    protected FastDFSApi fastDFSApi;

    @Autowired
    protected SpringRedisTools springRedisTools;

    @Autowired
    protected AttachmentApi attachmentApi;


    @Autowired
    protected TProductOrderApi tProductOrderApi;

    @Autowired
    protected TSysLookupApi tSysLookupApi;


    @Autowired
    protected QuillEditorApi quillEditorApi;


    /**
     * 分页查询产品明细信息
     * @param tProductDetailSearch
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Product,businessName="分页查询产品明细信息",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPageProductDetail",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageProductDetail(@RequestBody TProductDetailSearch tProductDetailSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        tProductDetailSearch.setProductDetailTableId("productdetail_" + tProductDetailSearch.getProductDetailTableId());
        RemoteModelResult<BaseDto> result = tProductApi.listPageProductDetail(ctx,tProductDetailSearch);
        if(result.isSuccess()){
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }






    /**
     * 获取产品明细
     * @param tableId
     * @param productCode
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Product,businessName="产品详情",operationType= OperationEnum.Search)
    @RequestMapping(value = "/getProductDetailByCode/{tableId}/{productCode}",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto getProductDetailByCode(@PathVariable String tableId,@PathVariable String productCode) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        TProductDetailSearch tProductDetailSearch = new TProductDetailSearch();
        tProductDetailSearch.setProductDetailTableId("productdetail_" + tableId);
        tProductDetailSearch.setCode(productCode);
        RemoteModelResult<List<TProductDetailList>> result = tProductApi.findProductDetailByCondition(ctx,tProductDetailSearch);
        if(result.isSuccess()){
            List<TProductDetailList> lists = result.getModel();
            if(CollectionUtils.isNotEmpty(lists)){
                JSONObject jsonObject = new JSONObject();
                for (TProductDetailList list : lists) {
                    if("market_state".equalsIgnoreCase(list.getFieldName())) {
                        String fixedCarParkSalesStatusName = DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(ctx.getCompanyId(), "fixedCarParkSalesStatus", list.getFieldValue());
                        jsonObject.put("market_state_name", fixedCarParkSalesStatusName);
                    }
                    jsonObject.put(list.getFieldName(), list.getFieldValue());
                }



                /**
                 * 图片集合
                 */
                AttachmentSearch attachmentSearch = new AttachmentSearch();
                attachmentSearch.setTableId("t_product_detail");
                attachmentSearch.setRelationId(productCode);
                RemoteModelResult<List<AttachmentList>> remoteModelResult = attachmentApi.findByCondition(ctx,attachmentSearch);
                if(remoteModelResult.isSuccess()){
                    List<AttachmentList> tProductAttachmentLists = remoteModelResult.getModel();
                    if(CollectionUtils.isNotEmpty(tProductAttachmentLists)){
                        JSONArray attachmentKeyJSONArray = new JSONArray(tProductAttachmentLists.size());
                        for (AttachmentList tProductAttachmentList : tProductAttachmentLists) {
                            JSONObject img = new JSONObject();
                            img.put("img",tProductAttachmentList.getAttachmentKey());
                            img.put("check",false);
                            attachmentKeyJSONArray.add(img);
                        }
                        jsonObject.put("photos",attachmentKeyJSONArray.toJSONString());
                    }
                }

                // 富文本
                TQuillEditorSearch tQuillEditorSearch = new TQuillEditorSearch();
                tQuillEditorSearch.setTableId("t_product_detail");
                tQuillEditorSearch.setRelationId(productCode);
                RemoteModelResult<List<TQuillEditorList>> remoteModelResult1 = quillEditorApi.findByCondition(ctx,tQuillEditorSearch);
                if(remoteModelResult1.isSuccess()){
                    List<TQuillEditorList> tQuillEditorLists = remoteModelResult1.getModel();
                    if(CollectionUtils.isNotEmpty(tQuillEditorLists)){
                        jsonObject.put("quillEditorText",tQuillEditorLists.get(0).getHtmlContent());
                    }
                }

                // 设置销售权限
                jsonObject.putAll(TProductDetailList.setProductSalesAuthorization(jsonObject));

                // 设置编辑权限
                jsonObject.putAll(TProductDetailList.setProductModifyAuthorization(jsonObject));

                /**
                 * 则设置续费状态
                 */
                TProductOrderDetailList tProductOrderDetailList = null;
                String marketState = jsonObject.getString(ProductConstant.PRODUCTDETAIL_COLUMN_MARKET_STATE);
                if("002".equals(marketState)){
                    RemoteModelResult<List<TProductOrderDetailList>> remoteModelResult2 = tProductOrderApi.findRecentProductOrderByProductCode(ctx,productCode);
                    if(remoteModelResult2.isSuccess()){
                        List<TProductOrderDetailList> tProductOrderDetailLists = remoteModelResult2.getModel();
                        if(CollectionUtils.isNotEmpty(tProductOrderDetailLists)){
                            tProductOrderDetailList = tProductOrderDetailLists.get(0);
                        }
                    }
                }
                jsonObject.putAll(TProductDetailList.setProductRenewalTermAuthorization(jsonObject,tProductOrderDetailList));

                baseDto.setE(jsonObject);
            }else{
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("没有找到产品信息");
            }
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }








    /**
     * 上架
     * @param productType 产品类型
     * @param projectId 项目编码
     * @param batchNo 产品批次号
     * @param code  产品编码
     * @param endTime 失效时间
     * @param stock 库存
     * @return
     */
    @RequestMapping(value = "/shelve/{productType}/{projectId}/{batchNo}/{code}/{endTime}/{stock}",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap shelve(@PathVariable String productType,@PathVariable String projectId,@PathVariable String batchNo,@PathVariable String code,@PathVariable String endTime,@PathVariable String stock){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        /**
         * 修改产品明细的状态
         */
        TProductDetailModify tProductDetailModify = new TProductDetailModify();
        tProductDetailModify.setProjectId(projectId);
        tProductDetailModify.setBatchNo(batchNo);
        tProductDetailModify.setCode(code);
        Map<String,String> fieldMap = new HashMap<>(1);

        DateTime nowDateTime = new DateTime();
        fieldMap.put("productdetail_" + productType + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_START_TIME,nowDateTime.toString("yyyy-MM-dd"));
        fieldMap.put("productdetail_" + productType + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_END_TIME,endTime);
        fieldMap.put("productdetail_" + productType + "_modify_id",ctx.getUserId());
        fieldMap.put("productdetail_" + productType + "_modify_name",ctx.getStaffName());
        fieldMap.put("productdetail_" + productType + "_modify_time",new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

        tProductDetailModify.setFieldMap(fieldMap);
        RemoteModelResult<MessageMap> result = tProductApi.shelve(ctx,tProductDetailModify);
        if(!result.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());

        }
        return mm;
    }


    /**
     * 正常下架
     * @param productType 产品类型
     * @param projectId 项目编码
     * @param batchNo 产品批次号
     * @param code  产品编码
     * @return
     */
    @RequestMapping(value = "/normalOffShelve/{productType}/{projectId}/{batchNo}/{code}",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap normalOffShelve(@PathVariable String productType,@PathVariable String projectId,@PathVariable String batchNo,@PathVariable String code){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        /**
         * 修改产品明细的状态
         */
        TProductDetailModify tProductDetailModify = new TProductDetailModify();
        tProductDetailModify.setProjectId(projectId);
        tProductDetailModify.setBatchNo(batchNo);
        tProductDetailModify.setCode(code);
        Map<String,String> fieldMap = new HashMap<>(1);
        fieldMap.put("productdetail_" + productType + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_START_TIME,"");
        fieldMap.put("productdetail_" + productType + "_" + ProductConstant.PRODUCTDETAIL_COLUMN_END_TIME,"");
        fieldMap.put("productdetail_" + productType + "_modify_id",ctx.getUserId());
        fieldMap.put("productdetail_" + productType + "_modify_name",ctx.getStaffName());
        fieldMap.put("productdetail_" + productType + "_modify_time",new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

        tProductDetailModify.setFieldMap(fieldMap);
        RemoteModelResult<MessageMap> result = tProductApi.normalOffShelve(ctx,tProductDetailModify);
        if(!result.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());

        }
        return mm;
    }


    /**
     * 删除产品明细
     * @param projectId
     * @param batchNo
     * @param productCode
     * @return
     */
    @RequestMapping(value = "/deleteProductDetailByProductCode/{projectId}/{batchNo}/{productCode}",method = RequestMethod.DELETE)
    public @ResponseBody
    MessageMap deleteProductDetailByProductCode(@PathVariable String projectId,@PathVariable String batchNo,@PathVariable String productCode){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<MessageMap> result = tProductApi.deleteProductDetailByProductCode(ctx,projectId,batchNo,productCode);
        if(!result.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }




    /**
     * 上传附件
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadFile/uploadAttachment",method=RequestMethod.POST)
    public @ResponseBody
    MessageMap uploadAttachment (MultipartHttpServletRequest request) {
        MessageMap mm = new MessageMap();

        MultiValueMap<String, MultipartFile> multipartFileMultiValueMap = request.getMultiFileMap();
        Set<String> keys = multipartFileMultiValueMap.keySet();
        for (String key : keys) {
            List<MultipartFile> multipartFiles = (List<MultipartFile>) multipartFileMultiValueMap.get(key);
            if(CollectionUtils.isNotEmpty(multipartFiles)){
                try {
                    RemoteModelResult<List<UploadFile>> remoteModelResult;
                    if(multipartFiles.size() > 1){
                        remoteModelResult = fastDFSApi.batchUploadFile((MultipartFile[]) multipartFiles.toArray());
                    }else{
                        remoteModelResult = fastDFSApi.batchUploadFile(multipartFiles.get(0));
                    }
                    if (remoteModelResult.isSuccess()) {
                        List<UploadFile> uploadFiles = remoteModelResult.getModel();
                        mm.setObj(uploadFiles.get(0).getUploadFileId());
                    }
                } catch (Exception e) {
                    logger.warn("上传失败："+e);
                    mm.setFlag(MessageMap.INFOR_WARNING);
                    mm.setMessage("上传失败。");
                    return mm;
                }
            }
        }
        return mm;
    }




    /**
     * 将base64转成图片文件
     * @param url
     * @param loginName
     * @return
     */
    protected File decodeBase64ToImage(String url, String loginName) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 创建临时目录
            String dirName = CreateFileUtil.getRootPathBySystem() + File.separator + "temp" + File.separator + loginName;
            // 创建临时文件
            String prefix = DateTime.now().toString("yyyyMMddHHmmSS");
            String surfix = "." + url.substring(url.indexOf("/") + 1,url.indexOf(";"));
            String tempFilePath =  CreateFileUtil.createTempFile(prefix, surfix, dirName);
            File file = new File(tempFilePath);
            FileOutputStream write = new FileOutputStream(file);
            String base64 = url.substring(url.indexOf(",")+1);
            byte[] decoderBytes = decoder.decodeBuffer(base64);
            write.write(decoderBytes);
            write.close();
            return file;
        } catch (IOException e) {
            return null;
        } catch (Exception e) {
            return null;
        }finally {

        }
    }
}