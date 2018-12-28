package com.everwing.coreservice.common.wy.entity.property.stall;/**
 * Created by wust on 2017/5/19.
 */

import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingImportList;

/**
 *
 * Function:导入excel封装数据的实体
 * Reason:
 * Date:2017/5/19
 * @author wusongti@lii.com.cn
 */
public class TcStallImportList extends TcBuildingImportList{
    private static final long serialVersionUID = 3406831906535827115L;
    /** 车位名称 **/
    private String stallName;
    /** 是否机械车位 **/
    private String isMechanicalStall;
    /** 车位属性 **/
    private String stallProperty;
    /** 车位类型 **/
    private String stallType;
    /**私家车位管理费用**/
    private Double administrativeExpenese;

    // 是否成功，必须要加该字段
    private Boolean successFlag;

    // 错误原因，必须要加该字段
    private String errorMessage;


    public String getStallName() {
        return stallName;
    }

    public void setStallName(String stallName) {
        this.stallName = stallName;
    }

    public String getIsMechanicalStall() {
        return isMechanicalStall;
    }

    public void setIsMechanicalStall(String isMechanicalStall) {
        this.isMechanicalStall = isMechanicalStall;
    }

    public String getStallProperty() {
        return stallProperty;
    }

    public void setStallProperty(String stallProperty) {
        this.stallProperty = stallProperty;
    }

    public String getStallType() {
        return stallType;
    }

    public void setStallType(String stallType) {
        this.stallType = stallType;
    }

    public Double getAdministrativeExpenese() {
        return administrativeExpenese;
    }

    public void setAdministrativeExpenese(Double administrativeExpenese) {
        this.administrativeExpenese = administrativeExpenese;
    }

    public Boolean getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(Boolean successFlag) {
        this.successFlag = successFlag;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return super.toString() + "\nTcStallImportList{" +
                "stallName='" + stallName + '\'' +
                ", isMechanicalStall='" + isMechanicalStall + '\'' +
                ", stallProperty='" + stallProperty + '\'' +
                ", stallType='" + stallType + '\'' +
                ", administrativeExpenese=" + administrativeExpenese +
                ", successFlag=" + successFlag +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
