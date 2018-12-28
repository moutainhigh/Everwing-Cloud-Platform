package com.everwing.coreservice.common.wy.fee.constant;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-07-31 17:08
 **/

public enum  AccountType {

    COMMON(1,"通用账户"),

    SPECIAL(2,"专项账户"),

    DELAY(3,"滞纳金"),

    DEPOSIT(4,"押金");


    private int code;

    private String description;

    AccountType(int code, String description) {
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
}
