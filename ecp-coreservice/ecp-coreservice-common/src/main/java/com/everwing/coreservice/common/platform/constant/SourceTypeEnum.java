package com.everwing.coreservice.common.platform.constant;

public enum SourceTypeEnum {
    IOS("ios"),ANDROID("android"),WEIXIN("weixin");

    private String type;

    public String getType() {
        return type;
    }

    SourceTypeEnum(String type){
        this.type=type;
    }

    public static boolean isDefined(String type){
        for(SourceTypeEnum sourceType:SourceTypeEnum.values()){
            if (sourceType.getType().equals(type)){
                return true;
            }
        }
        return false;
    }
}

