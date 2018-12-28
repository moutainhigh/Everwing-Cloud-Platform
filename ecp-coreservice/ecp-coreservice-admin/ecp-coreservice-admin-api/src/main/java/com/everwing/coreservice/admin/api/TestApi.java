package com.everwing.coreservice.admin.api;

import com.everwing.coreservice.common.admin.service.AdminTest;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestApi {

	@Autowired
	private AdminTest testApi;


	public RemoteModelResult<String> test(String accountName) {
		System.out.println("TestApi.test()");
		
		return testApi.test(accountName);
	}

}