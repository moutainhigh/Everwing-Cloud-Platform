package com.everwing.coreservice.common.platform.service.other;

import com.everwing.coreservice.common.dto.RemoteModelResult;

public interface SmsService {
	public RemoteModelResult<Void> sendVerifyCode(String phoneNum);
	
	public RemoteModelResult<Void> sendText(String content, String phoneNum);
	
	public RemoteModelResult<Void> deletePhoneNum(String phoneNum);
	
	public RemoteModelResult<Boolean> isVerifyCodeCorrect(String phoneNum, String verifyCode, boolean delete);

}
