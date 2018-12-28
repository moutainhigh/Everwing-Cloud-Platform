package com.everwing.coreservice.common.platform.constant;

public enum IdentityTypeEnum {

    ID_CARD("idCard","0"),

    PASS_PORT("passport","1"),

    OTHER("other","2");

    private String code;

    private String type;

    IdentityTypeEnum(String type,String code) {
        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static boolean contais(String code){
        IdentityTypeEnum[] identityTypeEnums=IdentityTypeEnum.values();
        for (int i=0;i<identityTypeEnums.length;i++){
            if (identityTypeEnums[i].getCode().equals(code)){
                return true;
            }
        }
        return false;
    }
}
