package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/8/30.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/30
 * @author wusongti@lii.com.cn
 */
public class TProductSearch extends TProduct{
    private static final long serialVersionUID = -3475458012208431283L;

    private Page page;

    private String productTableId;

    private String productDetailTableId;

    public Page getPage() {
        return this.page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getProductTableId() {
        return this.productTableId;
    }

    public void setProductTableId(String productTableId) {
        this.productTableId = productTableId;
    }

    public String getProductDetailTableId() {
        return this.productDetailTableId;
    }

    public void setProductDetailTableId(String productDetailTableId) {
        this.productDetailTableId = productDetailTableId;
    }
}
