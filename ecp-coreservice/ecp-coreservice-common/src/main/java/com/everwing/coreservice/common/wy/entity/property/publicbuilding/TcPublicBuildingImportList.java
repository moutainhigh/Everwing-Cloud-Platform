package com.everwing.coreservice.common.wy.entity.property.publicbuilding;/**
 * Created by wust on 2017/7/26.
 */

import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingImportList;

/**
 *
 * Function:
 * Reason:
 * Date:2017/7/26
 * @author wusongti@lii.com.cn
 */
public class TcPublicBuildingImportList extends TcBuildingImportList{

    private static final long serialVersionUID = 2599496115366749829L;

    private String name;


    // 是否成功，必须要加该字段
    private Boolean successFlag;

    // 错误原因，必须要加该字段
    private String errorMessage;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return super.toString() + "\nTcPublicBuildingImportList{" +
                "name='" + name + '\'' +
                ", successFlag=" + successFlag +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
