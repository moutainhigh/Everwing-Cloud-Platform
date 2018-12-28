package com.everwing.coreservice.platform.core.service.impl;

import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.platform.constant.IdentityTypeEnum;
import com.everwing.coreservice.common.platform.entity.generated.AccountIdentity;
import com.everwing.coreservice.common.platform.service.IdentityService;
import com.everwing.coreservice.platform.util.MapperResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IdentityServiceImpl extends MapperResources implements IdentityService  {

    private static final Logger logger= LoggerFactory.getLogger(IdentityServiceImpl.class);
    @Override
    public LinphoneResult insert(String accountId, String identityCode, String identityType, String realName, String mobile) {
        AccountIdentity accountIdentity=new AccountIdentity();
        accountIdentity.setAccountId(accountId);
        accountIdentity.setIdentityCode(identityCode);
        boolean contains=IdentityTypeEnum.contais(identityType);
        if(!contains){
            logger.error("传入的证件类型在系统中不存在,拒绝处理!");
            return new LinphoneResult(ResponseCode.RESOLVE_FAIL);
        }
        accountIdentity.setIdentityType(Integer.parseInt(identityType));
        accountIdentity.setRealName(realName);
        accountIdentity.setMobile(mobile);
        int count=identityExtraMapper.checkExists(accountId);
        if(count>0){
            return new LinphoneResult(ResponseCode.ADD_CERTIFICATE_EXISTED);
        }
        int insert=identityExtraMapper.insert(accountIdentity);
        if(insert>0){
            return new LinphoneResult(ResponseCode.RESOLVE_SUCCESS);
        }
        return new LinphoneResult(ResponseCode.RESOLVE_FAIL);
    }

    @Override
    public LinphoneResult update(String id, String accountId, String identityCode, String identityType, String realName, String mobile) {
        AccountIdentity accountIdentity=new AccountIdentity();
        accountIdentity.setId(id);
        accountIdentity.setAccountId(accountId);
        accountIdentity.setIdentityCode(identityCode);
        boolean contains=IdentityTypeEnum.contais(identityType);
        if(!contains){
            logger.error("传入的证件类型在系统中不存在,拒绝处理!");
            return new LinphoneResult(ResponseCode.RESOLVE_FAIL);
        }
        accountIdentity.setIdentityType(Integer.parseInt(identityType));
        accountIdentity.setRealName(realName);
        accountIdentity.setMobile(mobile);
        accountIdentity.setCreateTime(new Date());
        int count=identityExtraMapper.checkExists(accountId);
        if(count>0){
            return new LinphoneResult(ResponseCode.ADD_CERTIFICATE_EXISTED);
        }
        int updateCount=accountIdentityMapper.updateByPrimaryKey(accountIdentity);
        if(updateCount>0){
            return new LinphoneResult(ResponseCode.RESOLVE_SUCCESS);
        }
        return new LinphoneResult(ResponseCode.RESOLVE_FAIL);
    }

    @Override
    public List<Map<String,String>> queryByAccountId(String accountId) {
        return identityExtraMapper.selectByAccountId(accountId);
    }

    @Override
    public LinphoneResult queryById(String identityId) {
        return new LinphoneResult(identityExtraMapper.selectById(identityId));
    }

    @Override
    public boolean deleteByKey(String identityId) {
        return accountIdentityMapper.deleteByPrimaryKey(identityId)>0;
    }

}
