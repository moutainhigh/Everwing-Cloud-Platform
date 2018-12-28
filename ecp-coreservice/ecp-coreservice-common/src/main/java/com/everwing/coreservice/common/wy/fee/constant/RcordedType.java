package com.everwing.coreservice.common.wy.fee.constant;

/**
 * @Author: zgrf
 * @Description: 入账类型
 * @Date: Create in 2018-08-19 12:03
 **/

public enum RcordedType {
    IS_RCORDEDTYPE(1,"已入账"),
    NOT_RCORDEDTYPE(2,"未入账")
    ;
    private int code;

    private String description;

    RcordedType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static RcordedType getRcordedTypeByCode(int code){
        for (RcordedType rcordedType:RcordedType.values()){
            int defaultCode=rcordedType.getCode();
            if(defaultCode==code){
                return rcordedType;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
