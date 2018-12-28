package com.everwing.coreservice.common.platform.entity.extra;

import java.io.Serializable;

public class GatingDto implements Serializable{

    private String gatingId;

    private String gatingAddress;

    private String gatingCode;

    public String getGatingId() {
        return gatingId;
    }

    public void setGatingId(String gatingId) {
        this.gatingId = gatingId;
    }

    public String getGatingAddress() {
        return gatingAddress;
    }

    public void setGatingAddress(String gatingAddress) {
        this.gatingAddress = gatingAddress;
    }

    public String getGatingCode() {
        return gatingCode;
    }

    public void setGatingCode(String gatingCode) {
        this.gatingCode = gatingCode;
    }
}
