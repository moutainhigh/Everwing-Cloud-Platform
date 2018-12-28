package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/9/14.
 */

import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/14
 * @author wusongti@lii.com.cn
 */
public class TProductModify extends TProduct{

    private static final long serialVersionUID = -1446772307775982025L;

    // key = field_id, value = field_value
    private Map<String,String> fieldMap;

    public Map<String, String> getFieldMap() {
        return this.fieldMap;
    }

    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }
}
