package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.NoExceptionProxy;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.AccountIdentity;
import com.everwing.coreservice.common.platform.entity.generated.AccountIdentityExample;
import com.everwing.coreservice.common.platform.service.IdentityService;
import com.everwing.coreservice.platform.api.util.ServiceResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IdentityApi extends ServiceResources {

    @Autowired
    private IdentityService identityService;

    @NoExceptionProxy
    public LinphoneResult addIdentityInfo(String accountId, String identityCode, String identityType, String realName, String mobile) {
        return identityService.insert(accountId,identityCode,identityType,realName,mobile);
    }
    @NoExceptionProxy
    public LinphoneResult modifyIdentityInfo(String id, String accountId, String identityCode, String identityType, String realName, String mobile) {
        return identityService.update(id,accountId,identityCode,identityType,realName,mobile);
    }
    @NoExceptionProxy
    public List<Map<String,String>> getByAccountId(String accountId) {
        return identityService.queryByAccountId(accountId);
    }
    @NoExceptionProxy
    public boolean remove(String identityId) {
        return identityService.deleteByKey(identityId);
    }
    /**
     * 查询账号身份认证信息
     */
    public RemoteModelResult<AccountIdentity> queryByAccountName(String accountName, int accountType) {
        Account account = accountService.queryByAccountName(accountName, accountType).getModel();

        AccountIdentityExample example = new AccountIdentityExample();
        example.createCriteria().andAccountIdEqualTo(account.getAccountId());
        return commonService.selectOneByExample(AccountIdentity.class, example);
    }

    @NoExceptionProxy
    public LinphoneResult getById(String identityId) {
        return identityService.queryById(identityId);
    }
}
