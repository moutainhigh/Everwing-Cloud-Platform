package com.everwing.coreservice.common.wy.entity.common.select.asset;/**
 * Created by wust on 2018/12/4.
 */

/**
 *
 * Function:资产下拉框选择器查询对象
 * Reason:
 * Date:2018/12/4
 * @author wusongti@lii.com.cn
 */
public class AssetSelectSearch implements java.io.Serializable{
    private static final long serialVersionUID = -1852501360809981176L;

    private String assetType;       // 资产类型：标准建筑，公建/工程施工等
    private String customerId;      // 客户id
    private String houseCode;       // 资产老编码
    private String houseCodeNew;     // 资产新编码
    private String name;            // 资产名称

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getHouseCodeNew() {
        return houseCodeNew;
    }

    public void setHouseCodeNew(String houseCodeNew) {
        this.houseCodeNew = houseCodeNew;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
