package com.everwing.server.admin.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.everwing.server.admin.contants.ResponseCode;

//@JsonSerialize(using = JackJson2FastJsonSerializer.class)
public class ResponseResult {
	@JSONField(ordinal = 1)
	private Integer code = ResponseCode.SUCCESS.getCode();

	@JSONField(ordinal = 2)
	private String msg = ResponseCode.SUCCESS.getMsg();

	@JSONField(ordinal = 3)
	private Object data;

	@JSONField(serialize = false)
	private ResponseCode responseCode = ResponseCode.SUCCESS;

	public ResponseResult() {
		super();
	}

	public ResponseResult(ResponseCode responseCode) {
		super();
		this.responseCode = responseCode;
	}

	public String getMsg() {
		if (responseCode != null) {
			msg = responseCode.getMsg();
		}
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ResponseCode getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}

	public Integer getCode() {
		if (responseCode != null) {
			code = responseCode.getCode();
		}
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
