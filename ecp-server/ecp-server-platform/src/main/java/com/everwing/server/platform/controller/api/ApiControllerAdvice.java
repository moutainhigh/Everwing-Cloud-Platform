package com.everwing.server.platform.controller.api;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.server.platform.constant.ResponseCode;
import com.everwing.server.platform.exception.ApiException;
import com.everwing.server.platform.pojo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;



/**
 * @description 统一处理Controller异常
 * @author MonKong
 * @date 2017年3月28日
 */
@ControllerAdvice(basePackages = "com.everwing.server.platform.controller")
public class ApiControllerAdvice  {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 错误码映射关系Map：Map<Service错误码,Controller错误码>
	private Map<String, ResponseCode> codeMap = new HashMap<String, ResponseCode>();

	@ExceptionHandler
	public @ResponseBody String handleApiException(ApiException e) {
		RemoteModelResult<?> rmr = e.getRemoteModelResult();
		ResponseCode rc = e.getResponseCode();
		ResponseResult rr = new ResponseResult();

		if (rmr != null) {// Service层异常处理
			// 错误码/错误信息 映射
			rr.setResponseCode(codeMap.get(rmr.getCode()));
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

		rr.setResponseCode(rc);
		e.printStackTrace();
		logger.error(e.getMessage(), e);

		return JSON.toJSONString(rr);
	}

	@PostConstruct
	private void initCodeMap() {
		if (codeMap.size() == 0) {
			for (ResponseCode rc : ResponseCode.values()) {
				for (String mapCode : rc.getMapCode()) {
					codeMap.put(mapCode, rc);
				}
			}
		}
	}
}
