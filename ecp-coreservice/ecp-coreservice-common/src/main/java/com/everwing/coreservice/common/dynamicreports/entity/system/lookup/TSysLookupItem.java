package com.everwing.coreservice.common.dynamicreports.entity.system.lookup;


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
    //field
    /** 主键 **/
    private String lookupItemId;
    /** 主表主键 **/
    private String lookupId;
    /** 属性编码 **/
    private String code;
    /** 属性名称 **/
    private String name;
    /** 父级编码 **/
    private String parentCode;
    /** 语言，默认为zh_CN **/
    private String lan;
    /** 属性描述 **/
    private String description;
    /** 状态，enable是启用，disable是禁用 **/
    private String status;
    /**  **/
    private Object itemOrder;
    /** 备用字段1 **/
    private String column1;
    /** 备用字段2 **/
    private String column2;
    /** 备用字段3 **/
    private String column3;
    /** 备用字段4 **/
    private String column4;
    /** 备用字段5 **/
    private String column5;

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

    @Override
    public String getLan() {
        return lan;
    }

    @Override
    public void setLan(String lan) {
        this.lan = lan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(Object itemOrder) {
        this.itemOrder = itemOrder;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public String getColumn4() {
        return column4;
    }

    public void setColumn4(String column4) {
        this.column4 = column4;
    }

    public String getColumn5() {
        return column5;
    }

    public void setColumn5(String column5) {
        this.column5 = column5;
    }
}
