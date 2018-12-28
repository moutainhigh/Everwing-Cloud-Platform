package com.everwing.coreservice.common.wy.fee.constant;

/**
 * @Author: zgrf
 * @Description: 客户端类型
 * @Date: Create in 2018-08-14 15:40
 **/

public enum ClientType {
    PC(1,"前台PC"),
    POS(2,"前台POS"),
    MOBILE(3,"个人移动端");

    private int code;

    private String description;

    ClientType(int code, String description) {
        this.code = code;
        this.description = description;
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

    public static ClientType getClientTypeByCode(int code){
        for (ClientType clientType:ClientType.values()){
            int defaultCode=clientType.getCode();
            if(defaultCode==code){
                return clientType;
            }
        }
        return null;
    }
}
