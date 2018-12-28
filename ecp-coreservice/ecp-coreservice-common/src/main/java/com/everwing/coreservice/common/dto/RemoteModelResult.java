package com.everwing.coreservice.common.dto;

import java.io.Serializable;

import com.everwing.coreservice.common.constant.ReturnCode;

/**
 * 通用远程调用返回对象
 * 
 * @author zhugeruifei
 *
 * @param <T>
 */
public class RemoteModelResult<T extends Object> implements Serializable {
	private static final long serialVersionUID = -1496405684560284917L;
	// 业务对象
	private T model;
	// 响应码
	private String code = ReturnCode.SUCCESS.getCode();
	// 响应信息
	private String msg = ReturnCode.SUCCESS.getDescription();
	
	public RemoteModelResult(){}
	
	public RemoteModelResult(T model){
		this.model = model;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 调用返回值之前先要判断此方法 是否操作成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		if (ReturnCode.SUCCESS.getCode().equals(code)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "RemoteModelResult [model=" + model + ", code=" + code
				+ ", msg=" + msg + "]";
	}
}
