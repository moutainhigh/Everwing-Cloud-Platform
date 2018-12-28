package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2018/12/7.
 */

import com.everwing.coreservice.common.wy.entity.common.select.asset.AssetSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.customer.CustomerSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.vehicle.VehicleSelectList;

/**
 *
 * Function:我的购物车，存放公共字段以及组合所有产品类型的购物车
 * Reason:
 * Date:2018/12/7
 * @author wusongti@lii.com.cn
 */
public class MyShoppingCart implements java.io.Serializable{

    private static final long serialVersionUID = 9192411468174067528L;

    /** 购物车的该数据是否被选中 */
    private boolean checked;

    /** 产品类型 */
    private String productType;

    /** 项目id */
    private String projectId;

    /** 产品批号 */
    private String batchNo;

    /** 产品编码 */
    private String productCode;

    /** 关联客户 */
    private CustomerSelectList customer;

    /** 关联资产 */
    private AssetSelectList asset;

    /** 关联车辆 */
    private VehicleSelectList vehicle;

    /** 统计checked为true的产品的金额 */
    private String totalPrice;

    /** 统计checked为true的折扣金额 */
    private String totalDiscountAmount;

    /** 是否是续费操作 */
    private boolean isRenewalTerm;

    /** 描述 */
    private String description;

    /** 建筑租赁专用购物车 */
    private MyShoppingCartBuildingLease myShoppingCartBuildingLease;

    /** 停车优惠卡专用购物车 */
    private MyShoppingCartCarParksCard myShoppingCartCarParksCard;

    /** 公共服务专用购物车 */
    private MyShoppingCartCommonService myShoppingCartCommonService;

    /** 产品编码 */
    private MyShoppingCartDecorationService myShoppingCartDecorationService;

    /** 门禁卡专用购物车 */
    private MyShoppingCartEntranceGuardCard myShoppingCartEntranceGuardCard;

    /** 固定车位专用购物车 */
    private MyShoppingCartFixedcarPark myShoppingCartFixedcarPark;


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(String totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public boolean isRenewalTerm() {
        return isRenewalTerm;
    }

    public void setRenewalTerm(boolean renewalTerm) {
        isRenewalTerm = renewalTerm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MyShoppingCartBuildingLease getMyShoppingCartBuildingLease() {
        return myShoppingCartBuildingLease;
    }

    public void setMyShoppingCartBuildingLease(MyShoppingCartBuildingLease myShoppingCartBuildingLease) {
        this.myShoppingCartBuildingLease = myShoppingCartBuildingLease;
    }

    public MyShoppingCartCarParksCard getMyShoppingCartCarParksCard() {
        return myShoppingCartCarParksCard;
    }

    public void setMyShoppingCartCarParksCard(MyShoppingCartCarParksCard myShoppingCartCarParksCard) {
        this.myShoppingCartCarParksCard = myShoppingCartCarParksCard;
    }

    public MyShoppingCartCommonService getMyShoppingCartCommonService() {
        return myShoppingCartCommonService;
    }

    public void setMyShoppingCartCommonService(MyShoppingCartCommonService myShoppingCartCommonService) {
        this.myShoppingCartCommonService = myShoppingCartCommonService;
    }

    public MyShoppingCartDecorationService getMyShoppingCartDecorationService() {
        return myShoppingCartDecorationService;
    }

    public void setMyShoppingCartDecorationService(MyShoppingCartDecorationService myShoppingCartDecorationService) {
        this.myShoppingCartDecorationService = myShoppingCartDecorationService;
    }

    public MyShoppingCartEntranceGuardCard getMyShoppingCartEntranceGuardCard() {
        return myShoppingCartEntranceGuardCard;
    }

    public void setMyShoppingCartEntranceGuardCard(MyShoppingCartEntranceGuardCard myShoppingCartEntranceGuardCard) {
        this.myShoppingCartEntranceGuardCard = myShoppingCartEntranceGuardCard;
    }

    public MyShoppingCartFixedcarPark getMyShoppingCartFixedcarPark() {
        return myShoppingCartFixedcarPark;
    }

    public void setMyShoppingCartFixedcarPark(MyShoppingCartFixedcarPark myShoppingCartFixedcarPark) {
        this.myShoppingCartFixedcarPark = myShoppingCartFixedcarPark;
    }
}
