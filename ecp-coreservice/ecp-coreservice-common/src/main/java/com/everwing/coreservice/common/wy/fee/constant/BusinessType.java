package com.everwing.coreservice.common.wy.fee.constant;

/**
 * @Author: zgrf
 * @Description:
 * @Date: Create in 2018-07-31 17:16
 **/

public enum BusinessType {

    PRESTORE(1,"预存"),

    DEDUCTIBLE(2,"抵扣"),  //抵扣本金用和违约金区分开

    REFUND(3,"退款"),

    REGRESSES(4,"回退"),

    PAYMENT(5,"缴费"),

    REDUCTION(6,"减免"),
    //针对抵扣违约金的
    DEDUCTIBLE_LATE_FEE(7,"抵扣");
    




    private int code;

    private String description;

    BusinessType(int code, String description) {
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

    public static BusinessType getBusinessTypeByCode(int code){
        for(BusinessType businessType:BusinessType.values()){
            int storedCode=businessType.getCode();
            if(storedCode==code){
                return businessType;
            }
        }
        return null;
    }
}
