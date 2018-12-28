package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/9/1.
 */

import com.everwing.coreservice.common.wy.entity.common.attachment.Attachment;
import com.everwing.coreservice.common.wy.entity.common.quilleditor.TQuillEditor;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/1
 * @author wusongti@lii.com.cn
 */
public class TProductDetailModify extends TProductDetail{
    private static final long serialVersionUID = -1446772307775982025L;
    // key = field_id, value = field_value
    private Map<String,String> fieldMap;

    List<Attachment> productAttachments;

    List<TQuillEditor> tQuillEditors;


    public Map<String, String> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public List<Attachment> getProductAttachments() {
        return productAttachments;
    }

    public void setProductAttachments(List<Attachment> productAttachments) {
        this.productAttachments = productAttachments;
    }

    public List<TQuillEditor> gettQuillEditors() {
        return tQuillEditors;
    }

    public void settQuillEditors(List<TQuillEditor> tQuillEditors) {
        this.tQuillEditors = tQuillEditors;
    }
}
