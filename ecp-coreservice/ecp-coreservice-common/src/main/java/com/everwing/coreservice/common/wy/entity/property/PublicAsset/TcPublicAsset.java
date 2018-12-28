package com.everwing.coreservice.common.wy.entity.property.PublicAsset;

import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingNew;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TcPublicAsset implements java.io.Serializable{

    private static final long serialVersionUID = 4541129456866192736L;

    /** 客户房屋关系id **/
    private String  id;

    /** 建筑编号 **/
    private String buildingCode;

    /** 资产编码 **/
    private String houseCodeNew;

    /** 资产名称 **/
    private String buildingFullName;

    /** 地址 **/
    private String address;

    /** 描述 **/
    private String description;

    /** 位置 **/
    private String location;

    /** 数量 **/
    private Integer amount;

    /** 单位 **/
    private String unit;

    /** 是否自持 **/
    private String isHold;

    /** 用途 **/
    private String purpose;

    /** 是否经营 **/
    private String isManage;

    /** 是否供水 **/
    private String isWater;

    /** 是否供电 **/
    private String isElectricity;

    /** 水表数量 **/
    private Integer waterAmount;

    /** 电表数量 **/
    private Integer electricityAmount;

    /** 物业管理费单价 **/
    private String unitWyPrice;

    /** 本体基金单价 **/
    private String unitBtPrice;

    /** 是否计费 **/
    private String isBilling;

    /** 项目ID **/
    private String projectId;

    /** 创建人 **/
    private String createrId;

    /** 创建人姓名 **/
    private String createrName;

    /** 修改人ID **/
    private String modifyId;

    /** 修改人姓名 **/
    private String modifyName;

    /** 创建时间 **/
    private Date createTime;

    /** 修改时间 **/
    private Date modifyTime;

    /** 上传资料实体	**/
    private List<Annex> annexs; //上传资料实体

    /** 绑定关系 **/
    private List<PersonBuildingNew> buildingPerson;

    /** 修改字段数组 **/
    private String[] dirtyInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getBuildingFullName() {
        return buildingFullName;
    }

    public void setBuildingFullName(String buildingFullName) {
        this.buildingFullName = buildingFullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIsHold() {
        return isHold;
    }

    public void setIsHold(String isHold) {
        this.isHold = isHold;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getIsManage() {
        return isManage;
    }

    public void setIsManage(String isManage) {
        this.isManage = isManage;
    }

    public String getIsWater() {
        return isWater;
    }

    public void setIsWater(String isWater) {
        this.isWater = isWater;
    }

    public String getIsElectricity() {
        return isElectricity;
    }

    public void setIsElectricity(String isElectricity) {
        this.isElectricity = isElectricity;
    }

    public Integer getWaterAmount() {
        return waterAmount;
    }

    public void setWaterAmount(Integer waterAmount) {
        this.waterAmount = waterAmount;
    }

    public Integer getElectricityAmount() {
        return electricityAmount;
    }

    public void setElectricityAmount(Integer electricityAmount) {
        this.electricityAmount = electricityAmount;
    }

    public String getUnitWyPrice() {
        return unitWyPrice;
    }

    public void setUnitWyPrice(String unitWyPrice) {
        this.unitWyPrice = unitWyPrice;
    }

    public String getUnitBtPrice() {
        return unitBtPrice;
    }

    public void setUnitBtPrice(String unitBtPrice) {
        this.unitBtPrice = unitBtPrice;
    }

    public String getIsBilling() {
        return isBilling;
    }

    public void setIsBilling(String isBilling) {
        this.isBilling = isBilling;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public List<Annex> getAnnexs() {
        return annexs;
    }

    public void setAnnexs(List<Annex> annexs) {
        this.annexs = annexs;
    }

    public List<PersonBuildingNew> getBuildingPerson() {
        return buildingPerson;
    }

    public void setBuildingPerson(List<PersonBuildingNew> buildingPerson) {
        this.buildingPerson = buildingPerson;
    }

    public String[] getDirtyInfo() {
        return dirtyInfo;
    }

    public void setDirtyInfo(String[] dirtyInfo) {
        this.dirtyInfo = dirtyInfo;
    }

    @Override
    public String toString() {
        return "TcPublicAsset{" +
                "id='" + id + '\'' +
                ", buildingCode='" + buildingCode + '\'' +
                ", houseCodeNew='" + houseCodeNew + '\'' +
                ", buildingFullName='" + buildingFullName + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                ", isHold='" + isHold + '\'' +
                ", purpose='" + purpose + '\'' +
                ", isManage='" + isManage + '\'' +
                ", isWater='" + isWater + '\'' +
                ", isElectricity='" + isElectricity + '\'' +
                ", waterAmount=" + waterAmount +
                ", electricityAmount=" + electricityAmount +
                ", unitWyPrice='" + unitWyPrice + '\'' +
                ", unitBtPrice='" + unitBtPrice + '\'' +
                ", isBilling='" + isBilling + '\'' +
                ", projectId='" + projectId + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", modifyId='" + modifyId + '\'' +
                ", modifyName='" + modifyName + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", annexs=" + annexs +
                ", buildingPerson=" + buildingPerson +
                ", dirtyInfo=" + Arrays.toString(dirtyInfo) +
                '}';
    }
}
