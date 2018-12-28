package com.everwing.coreservice.platform.core.service.impl;


import com.everwing.coreservice.common.dto.LoginRslt;
import com.everwing.coreservice.common.service.PlatformAccountService;
import org.springframework.stereotype.Component;

@Component("platformAccountService")
public class PlatformAccountServiceImpl implements PlatformAccountService{

    @Override
    public LoginRslt platformAccountLogin(String accountName, String password) {
        System.err.println("PlatformAccountServiceImpl.platformAccountLogin()");
        LoginRslt rslt = new LoginRslt();
        rslt.setAccountID("1111");
        return rslt;
    }

}
