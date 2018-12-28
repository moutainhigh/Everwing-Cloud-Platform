package com.everwing.myexcel;/**
 * Created by wust on 2017/7/24.
 */

import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/24
 * @author wusongti@lii.com.cn
 */
public class ExcelParameters implements java.io.Serializable{
    private static final long serialVersionUID = 3460691524623454102L;
    private String xmlName;
    private String fileType;
    private Map parameters;
    private String batchNo;


    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
