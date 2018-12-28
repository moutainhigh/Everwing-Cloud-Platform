package com.everwing.coreservice.common.wy.entity.property.house;/**
 * Created by wust on 2017/7/20.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/20
 * @author wusongti@lii.com.cn
 */
public class CountHouseList implements java.io.Serializable{

    private static final long serialVersionUID = -9219119456225653770L;
    private Double houseAreaSum = 0.00 ;
    private Double usableAreaSum = 0.00 ;
    private Double finishAreaSum = 0.00 ;
    private Double shareAreaSum = 0.00 ;
    private Integer allCount = 0;
    private Integer soldCount = 0;

    public Double getHouseAreaSum() {
        return this.houseAreaSum;
    }

    public void setHouseAreaSum(Double houseAreaSum) {
        this.houseAreaSum = houseAreaSum;
    }

    public Double getUsableAreaSum() {
        return this.usableAreaSum;
    }

    public void setUsableAreaSum(Double usableAreaSum) {
        this.usableAreaSum = usableAreaSum;
    }

    public Double getFinishAreaSum() {
        return this.finishAreaSum;
    }

    public void setFinishAreaSum(Double finishAreaSum) {
        this.finishAreaSum = finishAreaSum;
    }

    public Double getShareAreaSum() {
        return this.shareAreaSum;
    }

    public void setShareAreaSum(Double shareAreaSum) {
        this.shareAreaSum = shareAreaSum;
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
