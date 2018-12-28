package com.everwing.coreservice.common.enums;

/**
 * Created by DELL on 2018/3/8.
 */
public enum PushServerReturnCode {

    TOKEN_UPLOAD_SUCCESS(0,"TOKEN上传成功"),

    DATABASE_ERROR(1,"数据库操作失败"),

    UPLOAD_PARAM_ERROR(2,"上传失败,参数格式不正确"),

    PUSH_TOKEN_ERROR(3,"推送失败，此token不存在"),

    PUSH_IOS_ERROR(4,"推送失败, IOS类型不正确"),

    PUSH_SUCCESS(5,"消息推送成功"),

    PUSH_FAIL(6,"消息推送失败"),

    PARAM_ERROR(-100,"消息传递不完整");

    private int code;

    private String msg;

    PushServerReturnCode(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public static String getMsg(int code){
        for (PushServerReturnCode returnCode : PushServerReturnCode.values()) {
            if (returnCode.code==code) {
                return returnCode.msg;
            }
        }
        return null;
    }
}
