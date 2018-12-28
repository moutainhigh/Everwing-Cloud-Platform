package com.everwing.server.platform.exception;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.server.platform.constant.ResponseCode;

/**
 * @description 用于抛出API业务异常，返回对应json
 * @author MonKong
 * @date 2016年12月30日
 */
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = -1474421839583539963L;

	public ApiException(ResponseCode responseCode) {
		super(responseCode.getMsg());
		this.responseCode = responseCode;
	}
	
	public ApiException(RemoteModelResult<?> remoteModelResult) {
		super(remoteModelResult.getMsg());
		this.remoteModelResult = remoteModelResult;
	}
	
	private ResponseCode responseCode;
	
	private RemoteModelResult<?> remoteModelResult;

	public ResponseCode getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}

	public RemoteModelResult<?> getRemoteModelResult() {
		return remoteModelResult;
	}

	public void setRemoteModelResult(RemoteModelResult<?> remoteModelResult) {
		this.remoteModelResult = remoteModelResult;
	}
}