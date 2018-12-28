package com.everwing.coreservice.common.constant;

public enum ReturnCode {

    /*=============== Platform-START ===============*/
    /*----- Account -----*/
    PF_ACCOUNT_OR_PSW_INCORRECT("10001", "账号或密码不正确"), //
    PF_ACCOUNT_EXISTED("10002", "账号已存在"), //
    PF_PSW_FORMAT_INCORRECT("10003", "密码格式不正确"), //
    PF_ACCOUNT_NAME_FORMAT_INCORRECT("10004", "用户名格式不正确"), //
    PF_ACCOUNT_NOT_EXISTS("10005", "账号不存在"), //
    PF_BIND_EXITED("10006", "资产已绑定"), //
    PF_ACCOUNT_CANCELED("10007", "账号已作废"), //
    PF_PHONE_NUMBER_OR_SMSCODE_ERROR("10008", "缺少手机号码或验证码"), //

    /*----- Device -----*/
    PF_DEVICE_MODEL_NOT_EXITS("11001", "不存在此设备型号"), //
    PF_LASTEST_ALREADY("11002", "已经是最新版本"), //
    PF_VERSION_ERROR("11003", "版本号错误"), //

    /*----- SMS -----*/
    PF_SMS_FAIL("100", "短信发送失败"), //
    PF_SMS_TYPE_ERROR("101", "短信发送类型错误"), //
    PF_SMS_CODE_ERROR("102", "短信验证码有误或过期"), //

    /*----- Common -----*/
    PF_DATA_NOT_EXITS("5", "没有此数据"), //
    PF_BATCH_DATAS_ACCOUNTNAME_EXISTED("4", "批量数据中账号已存在"), //
    PF_PARAMS_MISSING("3", "缺少参数"), //
    PF_BATCH_DATAS_ERROR("2", "批量数据有误"), //
    PF_PARAMS_ERROR("1", "参数错误"), //
    //app接口调用返回码
    API_RESOLVE_SUCCESS("000000", "处理成功"),
    API_TOKEN_VALIDATE("100001", "token不存在或者已经失效，请重新登录获取"),
    API_SIGN_ERROR("100002", "请求签名错误"),
    API_REQUEST_EXPIRED("100003", "请求已经过期"),
    API_PARAMS_IS_NULL("100004", "请求参数存在空值或参数未传入"),
    API_RESOLVE_FAIL("100005", "处理失败"),
    API_LOGIN_FAILURE("101001", "登录失败，账号或者密码错误"),
    API_USER_LOG_OFF("101002", "登录失败，该用户已经注销"),
    API_USER_EXISTED("102001", "注册失败,账户名已经存在"),
    API_MOBILE_EXISTED("102002", "注册失败，手机号码已经存在"),
    API_MOBILE_VALIDATE_CODE_ERROR("102003", "注册失败,手机验证码错误"),
    API_GET_VALIDATE_CODE_ERROR("103001", "验证码获取失败,请重新获取"),
    API_VALIDATE_CODE_MAX_TIME("103002", "验证码获取失败,该手机号已经超过最大发送次数"),
    API_ADD_CERTIFICATE_EXISTED("104001", "资产身份信息添加失败,证件信息已经存在"),
    API_UPDATE_CERTIFICATE_EXISTED("104002", "资产身份信息修改失败,证件信息已经存在"),
    API_FIND_PASSWORD_COUNT_ERROR("105001", "找回密码失败,账号信息错误"),
    API_FIND_PASSWORD_VALIDATE_CODE_ERROR("105002", "找回密码失败,验证码输入错误"),
    API_AUTHORIZED_FAIL_COUNT_ERROR("106001", "授权失败,被授权用户不存在"),
    API_AUTHORIZED_FAIL_EXISTS("106002", "授权失败,授权信息已存在"),
    API_REGISTER_COMPANY_IDENTITY_NOT_EXISTS("107001", "当前账号无注册公司权限,原因:未实名认证"),
    API_REGISTER_COMPANY_PERMISSION_DENY("107002", "注册公司失败,无注册公司权限"),
    API_WY_COMPANYID_NOT_FOUND("107003", "公司ID不存在，请联系管理员进行入职操作!"),
    API_WY_METER_DATE_OUT_OF_RANGE("107004", "抄表时间越界"),
    API_WY_METER_DATE_UPDATE_FAILED("107005", "抄表数据更新失败"),
    API_LINPHONE_RESET_MOBILE_VALIDATE_CODE_ERROR("108001", "手机验证码错误，绑定手机号码失败!"),
    API_LINPHONE_RESET_MOBILE_MOBILE_EXISTS_ERROR("108002", "该手机号已被绑定，绑定手机号码失败!"),
	/*=============== Platform-END ===============*/


    /*=============== wy-START ===============*/
    WY_PARAM_IS_NULL("20001", "参数为空"),
    WY_DS_IS_NULL("20002", "数据源为空"),
    WY_FILE_NOT_FOUND("20003", "文件未找到"),


    /*=============== datasource-START ===============*/
    WY_DS_COMPANY_ID_IS_NULL("29999", "传入公司id为空,无法切换数据源"),
    WY_DS_COMPANY_ID_PARAMS_ERROR("29998", "传入公司id参数错误,无法解析"),
		/*=============== datasource-END ===============*/

    /*=============== business-START ===============*/
    WY_BUSINESS_FILE_UPLOAD_FAILED("21000", "文件上传错误."),
    WY_BUSINESS_FILE_DELETE_FAILED("21001", "文件删除失败."),
    /*=============== business-END ===============*/
	
    /*=============== wy2Siebel-START ===============*/
    WY_TO_SEIBEL_FAILED("22000","数据推送至SIEBEL失败."),
    /*=============== wy2Siebel-START ===============*/

    
    /*=============== wy-END ===============*/


    SUCCESS("200000", "SUCCESS"),
    SYSTEM_ERROR("209999", "后台处理异常"),
    FORMULA_ERROE("1100348", "计算公式不正确,无法进行计算"),


    /* gating start */
    MK_GATING_DELETE_FAILED("400001", "删除门控机内部账号失败,无法删除门控机.");
	/* gating end */

    private String code;

    private String description;

    private ReturnCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }


    public String getDescription() {
        return description;
    }

    public static ReturnCode getReturnCodeByValue(String code) {
        for (ReturnCode returnCode : ReturnCode.values()) {
            if (returnCode.code.equals(code)) {
                return returnCode;
            }
        }
        return null;
    }
}
