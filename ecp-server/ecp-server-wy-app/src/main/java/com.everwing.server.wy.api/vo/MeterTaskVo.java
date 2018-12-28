package com.everwing.server.wy.api.vo;


import java.io.Serializable;
import java.util.List;

/**
 *
 * 抄表任务
 * Created by zhugeruifei on 17/8/12.
 */
public class MeterTaskVo<T extends MeterDataVo> implements Serializable {

    private static final long serialVersionUID = 2289945060519138006L;
    private String taskId;

    private String createTime;

    private String updateTime;

    private String startTime;

    private String endTime;

    private String status;

    private String taskContent;

    private String readingPersonName;

    private List<T> meterDataVoList;

    public MeterTaskVo() {
    }

    public MeterTaskVo(String taskId, String createTime,String startTime, String updateTime, String status, String taskContent, String readingPersonName,String endTime, List<T> meterDataVoList) {
        this.taskId = taskId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
        this.taskContent = taskContent;
        this.readingPersonName = readingPersonName;
        this.meterDataVoList = meterDataVoList;
        this.startTime=startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getReadingPersonName() {
        return readingPersonName;
    }

    public void setReadingPersonName(String readingPersonName) {
        this.readingPersonName = readingPersonName;
    }

    public List<T> getMeterDataVoList() {
        return meterDataVoList;
    }

    public void setMeterDataVoList(List<T> meterDataVoList) {
        this.meterDataVoList = meterDataVoList;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "MeterTaskVo{" +
                "taskId='" + taskId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", status='" + status + '\'' +
                ", taskContent='" + taskContent + '\'' +
                ", readingPersonName='" + readingPersonName + '\'' +
                ", meterDataVoList=" + meterDataVoList +
                '}';
    }
}
