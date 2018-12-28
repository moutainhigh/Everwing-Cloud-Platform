package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/8/30.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/30
 * @author wusongti@lii.com.cn
 */
public class TProduct implements java.io.Serializable{
    private static final long serialVersionUID = 814767018568115062L;

    /** 项目编码 **/
    private String projectId;
    /** 批次号 **/
    private String batchNo;
    /** 字段名 **/
    private String fieldId;
    /** 字段值 **/
    private String fieldValue;


    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getFieldId() {
        return this.fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldValue() {
        return this.fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
