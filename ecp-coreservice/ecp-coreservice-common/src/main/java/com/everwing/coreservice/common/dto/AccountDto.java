package com.everwing.coreservice.common.dto;

import com.everwing.coreservice.common.platform.entity.generated.Account;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class AccountDto extends Account{

    private String verificationCode;

    private Integer count;

    private String token;

    public AccountDto(){

    }
    public AccountDto(Account account){
        try {
            BeanUtils.copyProperties(this,account);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
