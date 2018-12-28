package com.everwing.coreservice.common.wy.entity.system.lookup;


import com.everwing.coreservice.common.BaseEntity;

/**
 * LookupItemVO
 *
 * @author Wusongti
 * @date Sep 16, 2015
 * @time 10:15:29 AM
 */
public class TSysLookupItem extends BaseEntity{

    private static final long serialVersionUID = 5077016941879794919L;
    private String lookupItemId;
    private String lookupId;
    private String code;
    private String name;
    private String parentCode;
    private String parentName;
    private String status;
    private String description;
    private Integer itemOrder;

    public String getLookupItemId() {
        return lookupItemId;
    }

    public void setLookupItemId(String lookupItemId) {
        this.lookupItemId = lookupItemId;
    }

    public String getLookupId() {
        return lookupId;
    }

    public void setLookupId(String lookupId) {
        this.lookupId = lookupId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
}
