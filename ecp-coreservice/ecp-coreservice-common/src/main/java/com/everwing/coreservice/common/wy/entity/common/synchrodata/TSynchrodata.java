package com.everwing.coreservice.common.wy.entity.common.synchrodata;/**
 * Created by wust on 2018/12/19.
 */

import com.everwing.coreservice.common.BaseEntity;

import java.util.Date;

/**
 *
 * Function:
 * Reason:
 * Date:2018/12/19
 * @author wusongti@lii.com.cn
 */
public class TSynchrodata extends BaseEntity implements java.io.Serializable{
    private static final long serialVersionUID = -4904514494566913559L;
    private String id;
    private String code;
    private String tableName;
    private String tableFieldName;
    private String tableFieldValue;
    private String destinationQueue;
    private String operation;
    private Integer priorityLevel;
    private String state;
    private String description;
    private String errorMessage;
    private Date synchroTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableFieldName() {
        return tableFieldName;
    }

    public void setTableFieldName(String tableFieldName) {
        this.tableFieldName = tableFieldName;
    }

    public String getTableFieldValue() {
        return tableFieldValue;
    }

    public void setTableFieldValue(String tableFieldValue) {
        this.tableFieldValue = tableFieldValue;
    }

    public String getDestinationQueue() {
        return destinationQueue;
    }

    public void setDestinationQueue(String destinationQueue) {
        this.destinationQueue = destinationQueue;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getSynchroTime() {
        return synchroTime;
    }

    public void setSynchroTime(Date synchroTime) {
        this.synchroTime = synchroTime;
    }
}
