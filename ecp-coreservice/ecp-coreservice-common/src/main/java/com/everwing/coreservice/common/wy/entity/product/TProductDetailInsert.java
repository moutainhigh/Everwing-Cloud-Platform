package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/9/6.
 */

import com.everwing.coreservice.common.wy.entity.common.attachment.Attachment;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditor;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/6
 * @author wusongti@lii.com.cn
 */
public class TProductDetailInsert extends TProductDetail{
    private static final long serialVersionUID = 8574662863029331299L;

    /**
     * 表标识
     */
    private String tableId;

    /**
     * 产品明细表标识
     */
    private String productDetailTableId;

    /**
     * 产品主表信息
     *  key = field_id, value = field_value
     */
    private Map<String,String> fieldMap;

    /**
     * 产品子表信息
     */
    private List<Map<String,String>> productDetailList;

    /**
     * 附件集合
     */
    private List<Attachment> productAttachmentsCreates;

    /**
     * 富文本集合
     */
    private List<TQuillEditor> tQuillEditors;


    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getProductDetailTableId() {
        return productDetailTableId;
    }

    public void setProductDetailTableId(String productDetailTableId) {
        this.productDetailTableId = productDetailTableId;
    }

    public Map<String, String> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public List<Map<String, String>> getProductDetailList() {
        return productDetailList;
    }

    public void setProductDetailList(List<Map<String, String>> productDetailList) {
        this.productDetailList = productDetailList;
    }

    public List<Attachment> getProductAttachmentsCreates() {
        return productAttachmentsCreates;
    }

    public void setProductAttachmentsCreates(List<Attachment> productAttachmentsCreates) {
        this.productAttachmentsCreates = productAttachmentsCreates;
    }

    public List<TQuillEditor> gettQuillEditors() {
        return tQuillEditors;
    }

    public void settQuillEditors(List<TQuillEditor> tQuillEditors) {
        this.tQuillEditors = tQuillEditors;
    }
}
