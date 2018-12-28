package com.everwing.coreservice.common.wy.entity.property.building;/**
 * Created by wust on 2017/4/17.
 */

import com.everwing.coreservice.common.Page;

import java.util.List;

/**
 *
 * Function:查询对象
 * Reason:
 * Date:2017-4-17 16:49:15
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
public class TcBuildingSearch extends TcBuilding{
    private static final long serialVersionUID = 2784370879693349714L;

    private Page page;

    private List<String> buildingCodeList;

    private String projectName;

    private String pidIsNullOrNotNull;

    private List<String> buildingTypeList;

    private String customerType;

    private String customerId;

    private String buildingTreeSearchKeyWord;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<String> getBuildingCodeList() {
        return buildingCodeList;
    }

    public void setBuildingCodeList(List<String> buildingCodeList) {
        this.buildingCodeList = buildingCodeList;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPidIsNullOrNotNull() {
        return pidIsNullOrNotNull;
    }

    public void setPidIsNullOrNotNull(String pidIsNullOrNotNull) {
        this.pidIsNullOrNotNull = pidIsNullOrNotNull;
    }

    public List<String> getBuildingTypeList() {
        return buildingTypeList;
    }

    public void setBuildingTypeList(List<String> buildingTypeList) {
        this.buildingTypeList = buildingTypeList;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBuildingTreeSearchKeyWord() {
        return buildingTreeSearchKeyWord;
    }

    public void setBuildingTreeSearchKeyWord(String buildingTreeSearchKeyWord) {
        this.buildingTreeSearchKeyWord = buildingTreeSearchKeyWord;
    }
}
