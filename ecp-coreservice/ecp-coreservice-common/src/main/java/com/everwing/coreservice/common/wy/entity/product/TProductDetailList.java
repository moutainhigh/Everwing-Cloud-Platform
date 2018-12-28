package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/9/4.
 */

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.math.BigDecimal;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/4
 * @author wusongti@lii.com.cn
 */
public class TProductDetailList extends TProductDetail {
    private static final long serialVersionUID = -8488868894227789347L;

    private String fieldName;

    private BigDecimal buildingArea;

    private BigDecimal usableArea;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public BigDecimal getBuildingArea() {
        return buildingArea;
    }

    public void setBuildingArea(BigDecimal buildingArea) {
        this.buildingArea = buildingArea;
    }

    public BigDecimal getUsableArea() {
        return usableArea;
    }

    public void setUsableArea(BigDecimal usableArea) {
        this.usableArea = usableArea;
    }




    /**
     * 设置产品修改权限
     * @param jsonObject
     * @return
     */
    public static JSONObject setProductModifyAuthorization (final JSONObject jsonObject){
        final JSONObject result = jsonObject;

        if(StringUtils.isBlank(result.getString("start_time"))){ // 启用日期为空
            // 该产品可以被编辑
            result.put("showModifyButton", true);
            return result;
        }

        DateTime dateTimeStart = new DateTime(result.getString("start_time"));
        DateTime dateTimeNow = DateTime.parse(new DateTime().toString("yyyy-MM-dd"));
        if(StringUtils.isBlank(result.getString("end_time"))){// 结束日期为空
            if(dateTimeStart.equals(dateTimeNow) || dateTimeStart.isBefore(dateTimeNow.getMillis())){  // 启用日期等于今天
                // 不允许被编辑
                result.put("showModifyButton", false);
            }else{// 启用日期不等于今天
                // 允许被编辑
                result.put("showModifyButton", true);
            }
            return result;
        }


        DateTime dateTimeEnd = new DateTime(result.getString("end_time")).plusDays(1);
        Interval interval = new Interval(dateTimeStart, dateTimeEnd);
        boolean contained = interval.contains(new DateTime());
        if(contained){// 在有效期范围
            // 不允许被编辑
            result.put("showModifyButton", false);
        }else{// 不在有效期范围
            // 该产品可以被编辑
            result.put("showModifyButton", true);
        }
        return result;
    }


    /**
     * 设置销售权限
     * @param jsonObject
     * @return
     */
    public static JSONObject setProductSalesAuthorization (final JSONObject jsonObject){
        final JSONObject result = jsonObject;

        if(StringUtils.isBlank(result.getString("start_time"))){ // 启用日期为空
            // 显示上架按钮
            result.put("showPutawayButton", true);
            return result;
        }

        DateTime dateTimeStart = new DateTime(result.getString("start_time"));
        DateTime dateTimeNow = DateTime.parse(new DateTime().toString("yyyy-MM-dd"));
        if(StringUtils.isBlank(result.getString("end_time"))){// 结束日期为空
            if(dateTimeStart.equals(dateTimeNow) || dateTimeStart.isBefore(dateTimeNow.getMillis())){  // 启用日期等于今天
                // 隐藏上架按钮
                result.put("showPutawayButton", false);

            }else{// 启用日期不等于今天

                // 显示上架按钮
                result.put("showPutawayButton", true);
            }
            return result;
        }


        DateTime dateTimeEnd = new DateTime(result.getString("end_time")).plusDays(1);
        Interval interval = new Interval(dateTimeStart, dateTimeEnd);
        boolean contained = interval.contains(new DateTime());
        if(contained){// 在有效期范围

            // 隐藏上架按钮
            result.put("showPutawayButton", false);
        }else{// 不在有效期范围

            // 显示上架按钮
            result.put("showPutawayButton", true);
        }
        return result;
    }


    /**
     * 设置产品续费权限
     * @param jsonObject
     * @param tProductOrderDetailList
     * @return
     */
    public static JSONObject setProductRenewalTermAuthorization (final JSONObject jsonObject,final TProductOrderDetailList tProductOrderDetailList){
        final JSONObject result = jsonObject;

        if(tProductOrderDetailList == null){
            result.put("showRenewalTermButton",false);
            return result;
        }

        DateTime dateTimeBegin = new DateTime(tProductOrderDetailList.getBuyBeginDate());
        DateTime dateTimeEnd = new DateTime(tProductOrderDetailList.getBuyEndDate());

        if(dateTimeBegin == null || dateTimeEnd == null){
            result.put("showRenewalTermButton",false);
            return result;
        }else {
            if(dateTimeEnd.isBeforeNow()){ // 过期，隐藏续费按钮
                result.put("showRenewalTermButton",false);
            }else{ // 未过期，显示续费按钮
                result.put("showRenewalTermButton",true);

                // 判断，续费按钮是否到达可用状态，即订单结束日期距离当前日期小于一个月才能进行续费操作
                if(new DateTime().plusMonths(1).isAfter(dateTimeEnd)){
                    result.put("disabledRenewalTermButton",false);
                }else{
                    result.put("disabledRenewalTermButton",true);
                }
            }
            return result;
        }
    }

}
