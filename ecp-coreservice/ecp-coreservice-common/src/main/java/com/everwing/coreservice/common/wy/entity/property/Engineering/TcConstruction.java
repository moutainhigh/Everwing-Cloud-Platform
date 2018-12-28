package com.everwing.coreservice.common.wy.entity.property.Engineering;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;

import java.util.Arrays;
import java.util.Date;

/**
 * 工程施工
 */

public class TcConstruction   implements java.io.Serializable{

    private static final long serialVersionUID = -9053947529318190278L;

    /** 主键id **/
    private String id;

    /** 施工编码 **/
    private String houseCodeNew;

    /** 施工地址**/
    private String constructionAddr;

    /** 开工日期 **/
    private Date startDate;

    /** 工程名称 **/
    private String engineeringName;

    /** 工程周期 **/
    private String engineeringCycle;

    /** 竣工日期 **/
    private Date completionDate;

    /** 工程单位 **/
    private String engineeringUnit;

    /** 物业负责人 **/
    private String ownerOfProperty;

    /** 工程负责人 **/
    private String engineeringDirector;

    /** 是否用水:Yes 是,No  否**/
    private String waterUse;

    /** 是否用电:Yes 是,No  否**/
    private String electricityUse;

    /** 是否已供电:Yes 是,No  否**/
    private String electricPower;

    /** 是否已供水:Yes 是,No  否 **/
    private String waterPower;

    /** 计费状态: 0 开始, 1 结束 **/
    private String billingStatus;

    /** 状态: 0 施工中, 1 暂停, 2 完工 **/
    private String constructionState;

    /** 状态  **/

    private  String state;

    /** 项目id **/
    private String projectId;

    /** 公司id **/
    private String companyId;

    /** 创建人 **/
    private  String  createrId;

    /** 创建人姓名 **/
    private  String  createrName;

    /** 修改人ID **/
    private  String  modifyId;

    /** 修改人姓名 **/
    private  String  modifyName;

    /** 创建时间 **/
    private Date createTime;

    /** 修改时间 **/
    private  Date modifyTime;

    /** 删除状态   0未删除   1已删除**/
    private  String isDelete;

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String[] dirtyinfo;

    public String[] getDirtyinfo() {
        return dirtyinfo;
    }

    public void setDirtyinfo(String[] dirtyinfo) {
        this.dirtyinfo = dirtyinfo;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getConstructionState() {
        return constructionState;
    }

    public void setConstructionState(String constructionState) {
        this.constructionState = constructionState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConstructionAddr() {
        return constructionAddr;
    }

    public void setConstructionAddr(String constructionAddr) {
        this.constructionAddr = constructionAddr;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getEngineeringName() {
        return engineeringName;
    }

    public void setEngineeringName(String engineeringName) {
        this.engineeringName = engineeringName;
    }

    public String getEngineeringCycle() {
        return engineeringCycle;
    }

    public void setEngineeringCycle(String engineeringCycle) {
        this.engineeringCycle = engineeringCycle;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getEngineeringUnit() {
        return engineeringUnit;
    }

    public void setEngineeringUnit(String engineeringUnit) {
        this.engineeringUnit = engineeringUnit;
    }

    public String getOwnerOfProperty() {
        return ownerOfProperty;
    }

    public void setOwnerOfProperty(String ownerOfProperty) {
        this.ownerOfProperty = ownerOfProperty;
    }

    public String getEngineeringDirector() {
        return engineeringDirector;
    }

    public void setEngineeringDirector(String engineeringDirector) {
        this.engineeringDirector = engineeringDirector;
    }

    public String getWaterUse() {
        return waterUse;
    }

    public void setWaterUse(String waterUse) {
        this.waterUse = waterUse;
    }

    public String getElectricityUse() {
        return electricityUse;
    }

    public void setElectricityUse(String electricityUse) {
        this.electricityUse = electricityUse;
    }

    public String getElectricPower() {
        return electricPower;
    }

    public void setElectricPower(String electricPower) {
        this.electricPower = electricPower;
    }

    public String getWaterPower() {
        return waterPower;
    }

    public void setWaterPower(String waterPower) {
        this.waterPower = waterPower;
    }

    public String getBillingStatus() {
        return billingStatus;
    }

    public void setBillingStatus(String billingStatus) {
        this.billingStatus = billingStatus;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public String getModifyName() {
        return modifyName;
    }

    public void setModifyName(String modifyName) {
        this.modifyName = modifyName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        return "TcConstruction{" +
                "id='" + id + '\'' +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", constructionAddr='" + constructionAddr + '\'' +
                ", startDate=" + startDate +
                ", engineeringName='" + engineeringName + '\'' +
                ", engineeringCycle='" + engineeringCycle + '\'' +
                ", completionDate=" + completionDate +
                ", engineeringUnit='" + engineeringUnit + '\'' +
                ", ownerOfProperty='" + ownerOfProperty + '\'' +
                ", engineeringDirector='" + engineeringDirector + '\'' +
                ", waterUse='" + waterUse + '\'' +
                ", electricityUse='" + electricityUse + '\'' +
                ", electricPower='" + electricPower + '\'' +
                ", waterPower='" + waterPower + '\'' +
                ", billingStatus='" + billingStatus + '\'' +
                ", constructionState='" + constructionState + '\'' +
                ", state='" + state + '\'' +
                ", projectId='" + projectId + '\'' +
                ", companyId='" + companyId + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", modifyId='" + modifyId + '\'' +
                ", modifyName='" + modifyName + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", isDelete='" + isDelete + '\'' +
                ", dirtyinfo=" + Arrays.toString(dirtyinfo) +
                '}';
    }
}
