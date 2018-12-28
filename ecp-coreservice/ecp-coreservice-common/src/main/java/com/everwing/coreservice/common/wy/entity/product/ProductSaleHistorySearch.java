package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2018/11/21.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2018/11/21
 * @author wusongti@lii.com.cn
 */
public class ProductSaleHistorySearch implements java.io.Serializable{
    private static final long serialVersionUID = -5599558541765517515L;

    private Page page;

    private String productCode;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
