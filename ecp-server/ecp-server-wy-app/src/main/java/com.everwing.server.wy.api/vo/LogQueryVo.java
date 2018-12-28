package com.everwing.server.wy.api.vo;

import java.io.Serializable;

/**
 * Created by zhugeruifei on 17/9/19.
 */
public class LogQueryVo implements Serializable {
    private static final long serialVersionUID = -8350319341684248048L;

    private String openDoorLogDate;

    private String openDoorType;

    public LogQueryVo() {
    }

    public LogQueryVo(String openDoorLogDate, String openDoorType) {
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
        return "LogQueryVo{" +
                "openDoorLogDate='" + openDoorLogDate + '\'' +
                ", openDoorType='" + openDoorType + '\'' +
                '}';
    }
}
