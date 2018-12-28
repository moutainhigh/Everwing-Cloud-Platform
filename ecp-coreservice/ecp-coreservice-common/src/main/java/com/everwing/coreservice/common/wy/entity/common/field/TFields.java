package com.everwing.coreservice.common.wy.entity.common.field;/**
 * Created by wust on 2017/8/30.
 */

import com.everwing.coreservice.common.BaseEntity;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/30
 * @author wusongti@lii.com.cn
 */
public class TFields extends BaseEntity{
    private static final long serialVersionUID = -7736587372032731989L;
    /** 主键 **/
    private String id;
    /** 表标识 **/
    private String tableId;
    /** 字段名 **/
    private String fieldName;
    /** 字段类型 **/
    private String fieldType;
    /** 字段长度 **/
    private Integer fieldLength;
    /** 字段显示名称 **/
    private String displayName;
    /** 是否展示 **/
    private String displayFlag;
    /** 是否必须 **/
    private String required;
    /** 排序 **/
    private Integer orderIndex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public int getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(String displayFlag) {
        this.displayFlag = displayFlag;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
