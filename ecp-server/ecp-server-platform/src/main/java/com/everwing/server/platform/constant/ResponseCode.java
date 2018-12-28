package com.everwing.server.platform.constant;

/**
 * @description API返回状态码
 * @author MonKong
 * @date 2016年12月20日
 */
public enum ResponseCode {
	/*----- Company -----*/
	COMPANY_EXISTED(12001,"公司名称已存在"),
	
	/*----- Account -----*/
	ACCOUNT_OR_PSW_INCORRECT(10001, "账号或密码不正确", "10001"), //
	ACCOUNT_EXISTED(10002, "账号已存在", "10002"), //
	PSW_FORMAT_INCORRECT(10003, "密码格式不正确", "10003"), //
	ACCOUNT_NAME_FORMAT_INCORRECT(10004, "用户名格式不正确", "10004"), //
	ACCOUNT_NOT_EXISTS(10005, "账号不存在", "10005"), //
	BIND_EXITED(10006, "资产已绑定", "10006"), //
	ACCOUNT_CANCELED(10007, "账号已作废", "10007"), //
	PHONE_NUMBER_OR_SMSCODE_ERROR(10008, "缺少手机号码或验证码", "10008"), //

	/*----- Device -----*/
	DEVICE_MODEL_NOT_EXITS(11001, "不存在此设备型号", "11001"), //
	LASTEST_ALREADY(11002, "已经是最新版本", "11002"), //
	VERSION_ERROR(11003, "版本号错误", "11003"), //

	/*----- SMS -----*/
	SMS_FAIL(100, "短信发送失败", "100"), //
	SMS_TYPE_ERROR(101, "短信发送类型错误", "101"), //
	SMS_CODE_ERROR(102, "短信验证码有误或过期", "102"), //

	/*----- Common -----*/
	SERVER_EXCEPTION(5, "服务器错误", "209999"), //
	BATCH_DATAS_ACCOUNTNAME_EXISTED(4, "批量数据中账号已存在", "4"), //
	PARAMS_MISSING(3, "缺少参数", "3"), //
	BATCH_DATAS_ERROR(2, "批量数据有误", "2"), //
	PARAMS_ERROR(1, "参数错误", "1"), //
	DATA_NOT_EXITS(-1, "没有此数据", "5"), //
	SUCCESS(0, "成功", "200000");

	private ResponseCode(Integer code, String msg, String... mapCode) {
		this.code = code;
		this.msg = msg;
		this.mapCode = mapCode;
	}

	private Integer code;

	private String msg;

	private String[] mapCode;// 一个Service错误码可以映射多个Controller错误码

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String[] getMapCode() {
		return mapCode;
	}

	public void setMapCode(String[] mapCode) {
		this.mapCode = mapCode;
	}

}
