package com.everwing.coreservice.common.wy.fee.constant;

/**
 * @Author: zgrf
 * @Description: 操作人类型
 * @Date: Create in 2018-08-14 15:47
 **/

public enum PersonType {

    QT(1,"前台"),
    YZ(2,"业主");

    private int code;

    private String description;

    PersonType(int code, String description) {
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

    public static PersonType getPersonTypeByCode(int code){
        for (PersonType personType:PersonType.values()){
            int defaultCode=personType.getCode();
            if(defaultCode==code){
                return personType;
            }
        }
        return null;
    }
}
