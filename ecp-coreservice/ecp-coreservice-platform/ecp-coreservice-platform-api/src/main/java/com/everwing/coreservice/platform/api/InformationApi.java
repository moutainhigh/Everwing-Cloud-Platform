package com.everwing.coreservice.platform.api;

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.platform.api.util.ServiceResources;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InformationApi extends ServiceResources {
    @Autowired
    private SpringRedisTools springRedisTools;

    @Value("${linphoneVerifySMSKeyPrefix}")
    private String linphoneSMSCodePrefix;

    @Value("${linphoneSMSKeyCountPrefix}")
    private String linphoneSMSCountPrefix;

    @Value("${linphoneVerifySMSMaxCount}")
    private Integer linphoneSMSMaxCount;

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(InformationApi.class);


    public RemoteModelResult getIdentity(String accountCode) {
        return  informationService.getIdentity(accountCode);
    }
    @NoExceptionProxy
    public LinphoneResult setIdentity(Account account, String name, String id) {
        return  informationService.setIdentity(account,name,id);
    }

}
