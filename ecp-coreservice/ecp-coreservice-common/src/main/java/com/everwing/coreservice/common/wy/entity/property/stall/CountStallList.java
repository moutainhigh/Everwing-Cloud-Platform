package com.everwing.coreservice.common.wy.entity.property.stall;/**
 * Created by wust on 2017/7/20.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/20
 * @author wusongti@lii.com.cn
 */
public class CountStallList implements java.io.Serializable{

    private static final long serialVersionUID = -9219119456225653770L;
    private Double openParkingCount = 0.00;    // 流动车位数量
    private Double fixedParkingCount  = 0.00;   // 固定车位数量
    private Double privateParkingCount  = 0.00; // 私家车位数量
    private Integer allCount = 0;              // 总数量
    private Integer soldCount = 0;          // 已销售数量


    public Double getOpenParkingCount() {
        return this.openParkingCount;
    }

    public void setOpenParkingCount(Double openParkingCount) {
        this.openParkingCount = openParkingCount;
    }

    public Double getFixedParkingCount() {
        return this.fixedParkingCount;
    }

    public void setFixedParkingCount(Double fixedParkingCount) {
        this.fixedParkingCount = fixedParkingCount;
    }

    public Double getPrivateParkingCount() {
        return this.privateParkingCount;
    }

    public void setPrivateParkingCount(Double privateParkingCount) {
        this.privateParkingCount = privateParkingCount;
    }

    public Integer getAllCount() {
        return this.allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getSoldCount() {
        return this.soldCount;
    }

    public void setSoldCount(Integer soldCount) {
        this.soldCount = soldCount;
    }
}
