package com.everwing.coreservice.common.wy.entity.system.importExport;/**
 * Created by wust on 2017/5/5.
 */

import com.everwing.coreservice.common.BaseEntity;

import java.util.Date;

/**
 *
 * Function:导入导出实体类
 * Reason:
 * Date:2017/5/5
 * @author wusongti@lii.com.cn
 */
public class TSysImportExport extends BaseEntity{
    private static final long serialVersionUID = -7357407565907811032L;
    //field
    /**  **/
    private String id;
    /** 文件系统返回的id，导入导出的文件 **/
    private String uploadFileId;
    /** 文件服务器返回的id，导入导出的结果信息文件 **/
    private String uploadMessageId;
    /** 模块名称 **/
    private String moduleDescription;
    /** 描述 **/
    private String description;
    /** 文件名 **/
    private String fileName;
    /** 文件大小，MB **/
    private String fileSize;
    /** 文件类型 **/
    private String fileType;
    /** 文件所在目录 **/
    private String fileDir;
    /** 操作类型：1是导入，2是导出 **/
    private String operationType;
    /** 当前状态：1，执行中；2，全部导入成功；3，部分导入成功；4，导入失败。 **/
    private String status;
    /** 批次号 **/
    private String batchNo;
    /** 开始时间 **/
    private Date startTime;
    /** 结束时间 **/
    private Date endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUploadFileId() {
        return uploadFileId;
    }

    public void setUploadFileId(String uploadFileId) {
        this.uploadFileId = uploadFileId;
    }

    public String getUploadMessageId() {
        return uploadMessageId;
    }

    public void setUploadMessageId(String uploadMessageId) {
        this.uploadMessageId = uploadMessageId;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TSysImportExport{" +
                "id='" + id + '\'' +
                ", uploadFileId='" + uploadFileId + '\'' +
                ", uploadMessageId='" + uploadMessageId + '\'' +
                ", moduleDescription='" + moduleDescription + '\'' +
                ", description='" + description + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileDir='" + fileDir + '\'' +
                ", operationType='" + operationType + '\'' +
                ", status='" + status + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
