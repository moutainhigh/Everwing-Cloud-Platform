package com.everwing.coreservice.common.service;

import com.everwing.coreservice.common.dto.LoginRslt;

public interface PlatformAccountService {

	public LoginRslt platformAccountLogin(String accountName,String password);
	
}
