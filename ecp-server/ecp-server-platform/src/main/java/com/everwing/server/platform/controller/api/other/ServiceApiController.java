package com.everwing.server.platform.controller.api.other;

import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.server.platform.constant.ResponseCode;
import com.everwing.server.platform.controller.api.BaseApiController;
import com.everwing.server.platform.exception.ApiException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/service")
public class ServiceApiController extends BaseApiController {

	/**
	 * @description 发送短信验证码
	 */
	@PostMapping("/sendSmsCode")
	public String sendSmsCode(@RequestBody Map<String, Object> paramsMap) {
		String phoneNum = (String) paramsMap.get("phoneNum");
		Integer type = (Integer) paramsMap.get("type");
		if (Dict.SMS_TYPE_VERIFY_CODE.getIntValue() == type) {
			smsApi.sendVerifyCode(phoneNum);
		} else {
			throw new ApiException(ResponseCode.SMS_TYPE_ERROR);
		}
		return renderSuccess();
	}

}