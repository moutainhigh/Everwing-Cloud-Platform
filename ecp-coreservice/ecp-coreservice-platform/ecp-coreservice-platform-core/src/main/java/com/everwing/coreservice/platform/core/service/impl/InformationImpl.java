package com.everwing.coreservice.platform.core.service.impl;

import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.AccountInformation;
import com.everwing.coreservice.common.platform.service.InformationService;
import com.everwing.coreservice.platform.core.util.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class InformationImpl extends Resources implements InformationService {
    private static final Logger logger= LoggerFactory.getLogger(InformationImpl.class);

    @Value("${linphoneVerifySMSKeyPrefix}")
    private String linphoneSMSCodePrefix;

    @Value("${linphoneSMSKeyCountPrefix}")
    private String linphoneSMSCountPrefix;

    @Value("${wyVerifySMSKeyPrefix}")
    private String wySMSCodePrefix;

    @Value("${wySMSKeyCountPrefix}")
    private String wySMSKeyCountPrefix;

    @Value("${linphoneVerifySMSMaxCount}")
    private Integer linphoneSMSMaxCount;

    @Value("${linphoneTokenKeyPrefix}")
    private String linphoneTokePrefix;

    @Value("${linphoneTokenExpireTime}")
    private int linphoneTokenExpireTime;

    @Override
    public RemoteModelResult getIdentity(String accountCode) {
        List<Map<String,String>> owners = informationMapper.getIdentity(accountCode);
        RemoteModelResult RemoteModelResult = new RemoteModelResult();
        RemoteModelResult.setModel(owners);
        RemoteModelResult.setCode(ReturnCode.API_RESOLVE_SUCCESS.getCode());
        return   RemoteModelResult;
    }

    @Override
    public LinphoneResult setIdentity(Account account, String name, String id) {

        String accountId = account.getAccountId();
        int count=informationMapper.checkExists(accountId);
        if(count>0){
            return new LinphoneResult(ResponseCode.ADD_CERTIFICATE_EXISTED);
        }
        AccountInformation accountInformation = new AccountInformation();
        accountInformation.setAccountid(account.getAccountId());
        accountInformation.setAccountCode(account.getAccountCode());
        accountInformation.setMobile(account.getMobile());
        accountInformation.setName(name);
        accountInformation.setCardnum(id);
         count=informationMapper.setIdentity(accountInformation);
        if(count==0){
            return new  LinphoneResult(ResponseCode.RESOLVE_FAIL);
        }
        return new  LinphoneResult(ResponseCode.RESOLVE_SUCCESS);
    }

}

