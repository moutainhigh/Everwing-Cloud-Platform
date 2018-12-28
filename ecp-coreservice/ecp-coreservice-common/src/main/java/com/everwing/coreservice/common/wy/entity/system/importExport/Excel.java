package com.everwing.coreservice.common.wy.entity.system.importExport;/**
 * Created by wust on 2017/4/27.
 */

import com.everwing.coreservice.common.BaseEntity;

import java.util.Map;

/**
 *
 * Function:excel基础对象
 * Reason:
 * Date:2017/4/27
 * @author wusongti@lii.com.cn
 */
public class Excel extends BaseEntity{

    private static final long serialVersionUID = 5640805115855419885L;
    private String xmlName;
    private String moduleDescription;
    private String excelVersion;
    private Map parameters;
    private String sql;
    private String excelName;
    private String batchNo;
    private String uploadDir;


    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public String getExcelVersion() {
        return excelVersion;
    }

    public void setExcelVersion(String excelVersion) {
        this.excelVersion = excelVersion;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
