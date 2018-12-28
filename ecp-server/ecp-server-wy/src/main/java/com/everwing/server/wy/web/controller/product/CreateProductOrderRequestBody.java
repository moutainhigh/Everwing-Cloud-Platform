package com.everwing.server.wy.web.controller.product;/**
 * Created by wust on 2017/11/6.
 */

import java.util.List;

/**
 *
 * Function:用于封装创建订单数据的实体
 * Reason:
 * Date:2017/11/6
 * @author wusongti@lii.com.cn
 */
public class CreateProductOrderRequestBody implements java.io.Serializable{

    private static final long serialVersionUID = -6433207564551165321L;

    private String projectId;

    private String productType;

    private String customerId;

    private String totalPrice;

    private List<String> checkedProductCodes;


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<String> getCheckedProductCodes() {
        return checkedProductCodes;
    }

    public void setCheckedProductCodes(List<String> checkedProductCodes) {
        this.checkedProductCodes = checkedProductCodes;
    }
}
