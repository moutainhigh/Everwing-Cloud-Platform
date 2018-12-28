package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/9/4.
 */

import com.everwing.coreservice.common.Page;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/4
 * @author wusongti@lii.com.cn
 */
public class TProductDetailSearch extends TProductDetail{

    private static final long serialVersionUID = 5871135051917447538L;
    private Page page;

    private String productTableId;

    private String productDetailTableId;

    private List<String> productCodeList;


    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getProductTableId() {
        return productTableId;
    }

    public void setProductTableId(String productTableId) {
        this.productTableId = productTableId;
    }

    public String getProductDetailTableId() {
        return productDetailTableId;
    }

    public void setProductDetailTableId(String productDetailTableId) {
        this.productDetailTableId = productDetailTableId;
    }

    public List<String> getProductCodeList() {
        return productCodeList;
    }

    public void setProductCodeList(List<String> productCodeList) {
        this.productCodeList = productCodeList;
    }
}
