package com.everwing.coreservice.common.dto;

import java.io.Serializable;

/**
 * Created by zhugeruifei on 17/9/19.
 */
public class LogQueryDTO implements Serializable{
    private static final long serialVersionUID = 4437438416412782494L;

    private String openDoorLogDate;

    private String openDoorType;

    public LogQueryDTO() {
    }

    public LogQueryDTO(String openDoorLogDate, String openDoorType) {
        this.openDoorLogDate = openDoorLogDate;
        this.openDoorType = openDoorType;
    }

    public String getOpenDoorLogDate() {
        return openDoorLogDate;
    }

    public void setOpenDoorLogDate(String openDoorLogDate) {
        this.openDoorLogDate = openDoorLogDate;
    }

    public String getOpenDoorType() {
        return openDoorType;
    }

    public void setOpenDoorType(String openDoorType) {
        this.openDoorType = openDoorType;
    }

    @Override
    public String toString() {
        return "LogQueryDTO{" +
                "openDoorLogDate='" + openDoorLogDate + '\'' +
                ", openDoorType='" + openDoorType + '\'' +
                '}';
    }
}
