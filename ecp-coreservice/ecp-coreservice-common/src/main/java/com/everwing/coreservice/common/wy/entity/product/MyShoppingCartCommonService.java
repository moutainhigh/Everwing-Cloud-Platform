package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2018/12/7.
 */

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.wy.entity.common.select.asset.AssetSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.customer.CustomerSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.vehicle.VehicleSelectList;

/**
 *
 * Function:普通服务购物车
 * Reason:
 * Date:2018/12/7
 * @author wusongti@lii.com.cn
 */
public class MyShoppingCartCommonService implements java.io.Serializable{
    private static final long serialVersionUID = -5497968690786641155L;


    /** 产品类型 */
    private String productType;

    /** 项目id */
    private String projectId;

    /** 产品批号 */
    private String batchNo;

    /** 产品编码 */
    private String productCode;

    /** 购买数量 */
    private Integer quantity;

    /** 开始计费时间 */
    private String startTime;

    /** 购买到期时间 */
    private String endTime;

    /** 关联客户：id */
    private String customerId;

    /** 关联客户：名称 */
    private String customerName;

    /** 关联客户 */
    private CustomerSelectList customer;

    /** 关联资产 */
    private AssetSelectList asset;

    /** 关联车辆 */
    private VehicleSelectList vehicle;

    /** 单位 */
    private String unit;

    /** 总价 */
    private String totalPrice;

    /** 折扣 */
    private String discountAmount;

    /** 产品JSON对象 */
    private JSONObject productJSONObject;

    private String description;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public CustomerSelectList getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerSelectList customer) {
        this.customer = customer;
    }

    public AssetSelectList getAsset() {
        return asset;
    }

    public void setAsset(AssetSelectList asset) {
        this.asset = asset;
    }

    public VehicleSelectList getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleSelectList vehicle) {
        this.vehicle = vehicle;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public JSONObject getProductJSONObject() {
        return productJSONObject;
    }

    public void setProductJSONObject(JSONObject productJSONObject) {
        this.productJSONObject = productJSONObject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
