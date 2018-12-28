package com.everwing.coreservice.common.constant;

public enum  ResponseCode {

    RESOLVE_SUCCESS("000000","处理成功"),
    TOKEN_VALIDATE("100001","token不存在或者已经失效，请重新登录获取"),
    SIGN_ERROR("100002","请求签名错误"),
    REQUEST_EXPIRED("100003","请求已经过期"),
    PARAMS_IS_NULL("100004","请求参数存在空值或参数未传入"),
    RESOLVE_FAIL("100005","处理失败"),
    LOGIN_FAILURE("101001","登录失败，账号或者密码错误"),
    USER_LOG_OFF("101002","登录失败，该用户已经注销"),
    USER_EXISTED("102001","注册失败,账户名已经存在"),
    MOBILE_EXISTED("102002","注册失败，手机号码已经存在"),
    MOBILE_VALIDATE_CODE_ERROR("102003","注册失败,手机验证码错误"),
    GET_VALIDATE_CODE_ERROR("103001","验证码获取失败,请重新获取"),
    VALIDATE_CODE_MAX_TIME("103002","验证码获取失败,该手机号已经超过最大发送次数"),
    ADD_CERTIFICATE_EXISTED("104001","资产身份信息添加失败,证件信息已经存在"),
    UPDATE_CERTIFICATE_EXISTED("104002","资产身份信息修改失败,证件信息已经存在"),
    FIND_PASSWORD_COUNT_ERROR("105001","找回密码失败,账号信息错误"),
    FIND_PASSWORD_VALIDATE_CODE_ERROR("105002","找回密码失败,验证码输入错误"),
    AUTHORIZED_FAIL_COUNT_ERROR("106001","授权失败,被授权用户不存在"),
    AUTHORIZED_FAIL_EXISTS("106002","授权失败,授权信息已存在");
    /**
     * 返回码
     */
    private String code;

    /**
     * 描述
     */
    private String message;

    ResponseCode(String code,String message){
        this.code=code;
        this.message=message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
