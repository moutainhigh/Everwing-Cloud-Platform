package com.everwing.server.linphone.constant;

public enum SourceType {
    /**
     * ios
     */
    IOS("ios"),
    /**
     * android
     */
    ANDROID("android"),
    /**
     * weixin
     */
    WEIXIN("weixin");

    private String type;

    public String getType() {
        return type;
    }

    SourceType(String type){
        this.type=type;
    }

    public static boolean isDefined(String type){
        for(SourceType sourceType:SourceType.values()){
            if (sourceType.getType().equals(type)){
                return true;
            }
        }
        return false;
    }
}

