package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.dto.LoginRslt;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.service.PlatformAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("platformAccountApi")
public class TestApi {

	@Autowired
	private PlatformAccountService platformAccountService;

	@NoExceptionProxy
	public RemoteModelResult<LoginRslt> test(String test, int test2) throws Exception {
		System.out.println("TestApi.test()");
		RemoteModelResult<LoginRslt> result = new RemoteModelResult<LoginRslt>();
		result.setModel(platformAccountService.platformAccountLogin("sfsdf", "sfsdfs"));
		if (false) {
			throw new Exception("testaaa");
		}
		return result;
	}

	public RemoteModelResult<LoginRslt> test2(String accountName) {
		System.out.println("TestApi.test()1111111111111111");
		RemoteModelResult<LoginRslt> result = new RemoteModelResult<LoginRslt>();
		result.setModel(platformAccountService.platformAccountLogin("sfsdf", "sfsdfs"));
		return result;
	}

}