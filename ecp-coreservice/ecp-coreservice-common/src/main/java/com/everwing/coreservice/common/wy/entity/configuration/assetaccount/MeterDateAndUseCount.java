package com.everwing.coreservice.common.wy.entity.configuration.assetaccount;

import java.io.Serializable;
import java.util.Date;

public class MeterDateAndUseCount implements Serializable {
    private static final long serialVersionUID = 532083951535972773L;
    private Date timeDate;
    private  Double useCount;
    private  String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(Date timeDate) {
        this.timeDate = timeDate;
    }

    public Double getUseCount() {
        return useCount;
    }

    public void setUseCount(Double useCount) {
        this.useCount = useCount;
    }
}
