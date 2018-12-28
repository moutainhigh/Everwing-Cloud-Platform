package com.everwing.coreservice.common.wy.entity.cust.person;

public class PersonCustNewImportList extends PersonCustNew{

    private static final long serialVersionUID = 3212083061140166702L;
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
        return "PersonCustNewImportList{" +
                "successFlag=" + successFlag +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
