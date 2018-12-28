package com.everwing.coreservice.common.wy.entity.cust.person;

import com.everwing.coreservice.common.Page;

public class PersonCustNewSearch  extends PersonCustNew {

    private static final long serialVersionUID = -4134061900916393496L;

    private Page page;

    /** 房号 **/
    private String houseCodeNew;

    /** 房屋地址 **/
    private String propertyAddr;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getPropertyAddr() {
        return propertyAddr;
    }

    public void setPropertyAddr(String propertyAddr) {
        this.propertyAddr = propertyAddr;
    }

    @Override
    public String toString() {
        return "PersonCustNewSearch{" +
                "page=" + page +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", propertyAddr='" + propertyAddr + '\'' +
                '}';
    }
}
