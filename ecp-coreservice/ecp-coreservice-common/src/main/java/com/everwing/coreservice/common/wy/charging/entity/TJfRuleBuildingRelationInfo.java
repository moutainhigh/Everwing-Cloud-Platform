package com.everwing.coreservice.common.wy.charging.entity;

import java.util.Date;

public class TJfRuleBuildingRelationInfo {
    private String id;

    /**
     *房号
     */
    private String houseCode;

    /**
     *计费规则id
     */
    private String ruleId;

    /**
     *创建人
     */
    private String createId;

    /**
     *创建时间
     */
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}