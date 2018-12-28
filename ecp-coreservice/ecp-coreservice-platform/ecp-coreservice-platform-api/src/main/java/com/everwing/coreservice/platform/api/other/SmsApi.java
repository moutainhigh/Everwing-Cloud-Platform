package com.everwing.coreservice.platform.api.other;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.service.other.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsApi {

	@Autowired
	private SmsService smsService;

	public RemoteModelResult<Void> sendVerifyCode(String phoneNum) {
		return smsService.sendVerifyCode(phoneNum);
	}

}