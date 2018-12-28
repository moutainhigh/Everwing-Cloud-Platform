package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/8/30.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/30
 * @author wusongti@lii.com.cn
 */
public class TProductList extends TProduct{
    private static final long serialVersionUID = -388420600596521619L;

   private String fieldName;

   private String stockCount;


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getStockCount() {
        return stockCount;
    }

    public void setStockCount(String stockCount) {
        this.stockCount = stockCount;
    }
}
