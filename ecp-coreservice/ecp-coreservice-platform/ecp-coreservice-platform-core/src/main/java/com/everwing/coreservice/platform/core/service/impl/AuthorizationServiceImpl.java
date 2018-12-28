package com.everwing.coreservice.platform.core.service.impl;

import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseSipData;
import com.everwing.coreservice.common.platform.entity.generated.LyAuthorizationAccount;
import com.everwing.coreservice.common.platform.entity.generated.TcBuilding;
import com.everwing.coreservice.common.platform.service.AuthorizationService;
import com.everwing.coreservice.common.utils.SipUtils;
import com.everwing.coreservice.platform.dao.mapper.extra.AuthorizationExtraMapper;
import com.everwing.coreservice.platform.dao.mapper.generated.TcBuildingMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final Logger logger= LogManager.getLogger(AuthorizationServiceImpl.class);

    @Autowired
    private AuthorizationExtraMapper authorizationExtraMapper;

    @Autowired
    private TcBuildingMapper tcBuildingMapper;

    @Autowired
    private SipUtils sipUtils;

    @Override
    public LinphoneResult authorization(String authorizedAccountCode, String authorizeeAccountCode, String startTime, String endTime, String buildingId) {
        LyAuthorizationAccount lyAuthorizationAccount=new LyAuthorizationAccount();
        lyAuthorizationAccount.setAuthorizedAccountId(authorizedAccountCode);
        lyAuthorizationAccount.setAuthorizeeAccountId(authorizeeAccountCode);
        int checkCount=authorizationExtraMapper.checkAuthorizeeExists(authorizeeAccountCode);
        if(checkCount==0){
            logger.debug("授权失败,被授权用户不存在!");
            return new LinphoneResult(ResponseCode.AUTHORIZED_FAIL_COUNT_ERROR);
        }
        lyAuthorizationAccount.setStartTime(Timestamp.valueOf(startTime));
        lyAuthorizationAccount.setEndTime(Timestamp.valueOf(endTime));
        lyAuthorizationAccount.setBuildIds(buildingId);
        int existsCount=authorizationExtraMapper.checkAuthExists(lyAuthorizationAccount);
        if(existsCount==1){
            logger.debug("授权失败,授权信息已存在");
            return new LinphoneResult(ResponseCode.AUTHORIZED_FAIL_EXISTS);
        }
        int count=authorizationExtraMapper.insert(lyAuthorizationAccount);
        if(count==1){
            logger.info("授权成功!");
            String buildingCode=null;
            TcBuilding tcBuilding=tcBuildingMapper.selectByPrimaryKey(buildingId);
            if(tcBuilding!=null){
                buildingCode=tcBuilding.getBuildingCode();
            }
            if(StringUtils.isNotEmpty(buildingCode)) {
                logger.debug("查询到buildingCode,同步绑定关系到sip服务器");
                addBind(buildingCode, authorizeeAccountCode);
            }else {
                logger.debug("未查询到buildingCode,不同步绑定关系!");
            }
            return new LinphoneResult(ResponseCode.RESOLVE_SUCCESS);
        }
        return new LinphoneResult(ResponseCode.RESOLVE_FAIL);
    }

    @Override
    public LinphoneResult queryAuthorized(String accountCode) {
        return new LinphoneResult(authorizationExtraMapper.selectAuthorizedInfo(accountCode));
    }

    @Override
    public LinphoneResult queryAuthorizee(String accountCode) {
        return new LinphoneResult(authorizationExtraMapper.selectAuthorizeeInfo(accountCode));
    }

    @Override
    public LinphoneResult removeAuthorizee(String authorizedAccountCode, String authorizeeAccountCodes, String buildingId) {
        String[] authAccoutCodeArray=authorizeeAccountCodes.split(",");
        authorizationExtraMapper.deleteAuthInfo(authorizedAccountCode,authAccoutCodeArray,buildingId);
        logger.debug("同步删除绑定关系到sip");
        String buildingCode=null;
        TcBuilding tcBuilding=tcBuildingMapper.selectByPrimaryKey(buildingId);
        if(tcBuilding!=null){
            buildingCode=tcBuilding.getBuildingCode();
        }
        if(StringUtils.isNotEmpty(buildingCode)) {
            logger.debug("查询到buildingCode,同步删除绑定关系到sip服务器");
            removeBind(buildingCode, authorizeeAccountCodes.split(","));
        }else {
            logger.debug("未查询到buildingCode,不同步删除绑定关系!");
        }
        return new LinphoneResult(ResponseCode.RESOLVE_SUCCESS);
    }

    private void addBind(String buildingCode, String accountCode){
        List<AccountAndHouseSipData> accountAndHouses=new ArrayList<>();
        AccountAndHouseSipData accountAndHouseSipData=new AccountAndHouseSipData(accountCode,buildingCode);
        accountAndHouses.add(accountAndHouseSipData);
        sipUtils.bind(accountAndHouses);
        logger.info("用户房间关系绑定成功!");
    }

    private void removeBind(String buildingCode, String[] accountCodes) {
        List<AccountAndHouseSipData> accountAndHouses=new ArrayList<>(accountCodes.length);
        for (String accountCode : accountCodes) {
            AccountAndHouseSipData accountAndHouseSipData = new AccountAndHouseSipData(accountCode, buildingCode);
            accountAndHouses.add(accountAndHouseSipData);
        }
        sipUtils.unbind(accountAndHouses);
        logger.info("用户房间关系解除绑定成功!");
    }

}
