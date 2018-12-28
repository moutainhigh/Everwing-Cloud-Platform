package com.everwing.coreservice.common.wy.entity.cust.person;

import java.io.Serializable;


/*
 补充托收业主
 */
public class TBcCillectionName  implements Serializable {


    private static final long serialVersionUID = 5996410334742294027L;
   private  String cardNo;
   private  String backName;
   private  String buildingCode;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBackName() {
        return backName;
    }

    public void setBackName(String backName) {
        this.backName = backName;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }
}