package com.everwing.coreservice.common.wy.entity.cust.enterprise;

public class EnterpriseCustNewImportList extends EnterpriseCustNew{


    private static final long serialVersionUID = -5520209148743226817L;


    // 是否成功，必须要加该字段
    private Boolean successFlag;

    // 错误原因，必须要加该字段
    private String errorMessage;


    public static long getSerialVersionUID() {
        return serialVersionUID;
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
        return "EnterpriseCustNewImportList{" +
                "successFlag=" + successFlag +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
