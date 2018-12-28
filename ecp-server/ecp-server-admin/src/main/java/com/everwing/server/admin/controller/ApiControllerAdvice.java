package com.everwing.server.admin.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.server.admin.contants.ResponseCode;
import com.everwing.server.admin.exception.ApiException;
import com.everwing.server.admin.pojo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description 统一处理Controller异常
 * @author MonKong
 * @date 2017年3月28日
 */
@ControllerAdvice(basePackages = "com.everwing.server.admin.controller")
public class ApiControllerAdvice {
	private Logger logger = LoggerFactory.getLogger(getClass());


	@ExceptionHandler
	public @ResponseBody String handleApiException(ApiException e) {
		RemoteModelResult<?> rmr = e.getRemoteModelResult();
		ResponseCode rc = e.getResponseCode();
		ResponseResult rr = new ResponseResult();

		if (rmr != null) {// Service层异常处理
			// 错误码/错误信息 映射
			rr.setResponseCode(ResponseCode.getByMapCode(rmr.getCode()));
		} else if (rc != null) {// Controller层异常处理
			rr.setResponseCode(rc);
		} else {
			rr.setResponseCode(ResponseCode.SERVER_EXCEPTION);
		}
		logger.error("\n" + e.getMessage() + "\n");

		return JSON.toJSONString(rr);
	}

	@ExceptionHandler
	public @ResponseBody String handleException(Exception e) {
		ResponseResult rr = new ResponseResult();
		ResponseCode rc = ResponseCode.SERVER_EXCEPTION;

		rc.setMsg(rc.getMsg() + ": " + e.getMessage());// 丰富错误信息
		rr.setResponseCode(rc);
		e.printStackTrace();
		logger.error(e.getMessage(), e);

		return JSON.toJSONString(rr);
	}

}
