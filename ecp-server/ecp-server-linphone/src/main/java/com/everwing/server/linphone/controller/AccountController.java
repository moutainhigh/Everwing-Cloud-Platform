package com.everwing.server.linphone.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.AccountDto;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseSipData;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.utils.OpenFireUtils;
import com.everwing.coreservice.common.utils.SipUtils;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.wy.api.cust.enterprise.EnterpriseCustApi;
import com.everwing.coreservice.wy.api.cust.person.PersonCustApi;
import com.everwing.utils.TokenGenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/{version}")
public class AccountController{

    private static final Logger logger= LogManager.getLogger(AccountController.class);

    @Autowired
    private SipUtils sipUtils;

    @Autowired
    private OpenFireUtils openFireUtils;

    @Autowired
    private AccountApi accountApi;

    @Autowired
    private EnterpriseCustApi enterpriseCustApi;

    @Autowired
    private PersonCustApi personCustApi;

    @PostMapping("/register")
    @ApiVersion(1.0)
    public LinphoneResult register(String password, String mobile, String verificationCode, String sourceType){
        Account account=new Account();
        account.setPassword(password);
        account.setAccountName(TokenGenUtils.generateToken(mobile));
        account.setType(Dict.ACCOUNT_TYPE_LY_USER.getIntValue());
        account.setSourceType(sourceType);
        return accountApi.registerLinPhone(account,verificationCode,mobile);
    }

    @PostMapping("/verificationCode")
    @ApiVersion(1.0)
    public LinphoneResult verificationCode(String mobile){
        return accountApi.getVerificationCode(mobile);
    }

//    @PostMapping("/resetMobile") 合版暂时1130
//    @ApiVersion(1.0)
//    public LinphoneResult resetPhone(HttpServletRequest request,String mobile,String verificationCode){
//        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
//        String OldMobile = account.getMobile();
//        RemoteModelResult remoteModelResult = accountApi.resetMobile(account.getAccountId(),mobile,OldMobile,verificationCode);
//
//        RemoteModelResult remoteModelResult1 = accountApi.getCompanyId(mobile);
////        String companyId = accountApi.getCompanyId(mobile);
//        String companyId = (String) remoteModelResult1.getModel();
//
//        personCustApi.resetMobileByPhone(companyId,mobile,OldMobile) ;
//
//        if(remoteModelResult.isSuccess()){
//            return new LinphoneResult(ReturnCode.API_RESOLVE_SUCCESS);
//        }else {
//            return new LinphoneResult(remoteModelResult);
//        }
//    }

    @PostMapping("login")
    @ApiVersion(1.0)
    public LinphoneResult login(String mobile, String password, String sourceType){
        int loginType=Dict.ACCOUNT_TYPE_LY_USER.getIntValue();
        LinphoneResult linphoneResult=accountApi.login(mobile,password,sourceType,loginType);
        if(linphoneResult.isSuccess()){
            //查询账户、房间关系
            AccountDto accountDto= (AccountDto) linphoneResult.getData();
            RemoteModelResult<List<String>> codeResult=accountApi.queryAccountHouse(mobile);
            if(codeResult.isSuccess()){//系统存在绑定关系
                List<String> codeList=codeResult.getModel();
                if(!codeList.isEmpty()){
                    logger.debug("删除系统中存在的账号绑定关系:{}",mobile);
                    accountApi.delAccoutHouse(accountDto.getMobile());
                }
                logger.debug("查询绑定关系并插入系统！:{}",mobile);
                RemoteModelResult<List<String>> bindResult=accountApi.queryAccountBuildingR(mobile);
                if(bindResult.isSuccess()){//查询到绑定数据
                    List<String> bindCodeList=bindResult.getModel();
                    logger.debug("删除远程服务器原有的绑定关系:{},{}",mobile,JSON.toJSONString(codeList));
                    removeBind(codeList,accountDto);
                    logger.debug("同步新的绑定关系到远程服务器:{},{}",mobile,JSON.toJSONString(bindCodeList));
                    addBind(bindCodeList,accountDto);
                }
            }
        }
        return linphoneResult;
    }

    @PostMapping("resetPassword")
    @ApiVersion(1.0)
    public LinphoneResult resetPassword(String password, String mobile, String verificationCode){
        LinphoneResult linphoneResult= accountApi.resetPassword(password,mobile,verificationCode);
        if(linphoneResult.isSuccess()){
            List<Account> accountList=new ArrayList<>(1);
            accountList.add((Account) linphoneResult.getData());
            logger.info("重置密码,开始同步sip、openfire服务器");
            sipUtils.modifyPwd(accountList);
            openFireUtils.modifyPwd(accountList);
            linphoneResult.setData(null);
        }
        return linphoneResult;
    }

    @PostMapping(value = "modifyPassword")
    @ApiVersion(1.0)
    public LinphoneResult modifyPassword(HttpServletRequest request, String password){
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        LinphoneResult linphoneResult=accountApi.modifyPassword(account,password);
        if(linphoneResult.isSuccess()){
            List<Account> accountList=new ArrayList<>(1);
            account.setPassword(password);
            accountList.add(account);
            logger.info("修改密码,开始同步sip、openfire服务器");
            sipUtils.modifyPwd(accountList);
            openFireUtils.modifyPwd(accountList);
        }
        return linphoneResult;
    }

    @PostMapping(value = "loginout")
    @ApiVersion(1.0)
    public LinphoneResult loginOut(String accountId){
        return accountApi.loginOut(accountId);
    }

    private void addBind(List<String> codeList,AccountDto accountDto){
        if(!codeList.isEmpty()){
            List<AccountAndHouseSipData> accountAndHouseSipDataList=new ArrayList<>();
            String accountCode=accountDto.getAccountCode();
            for(String code:codeList){
                AccountAndHouseSipData accountAndHouseSipData=new AccountAndHouseSipData(accountCode,code);
                accountAndHouseSipDataList.add(accountAndHouseSipData);
            }
            if(sipUtils.bind(accountAndHouseSipDataList)) {
                logger.info("用户房间关系绑定成功!");
            }else {
                logger.error("用户房间关系绑定失败!");
            }
        }else {//未查询到账号绑定关系。不同步
            logger.info("未查询到账号:{}的房间绑定关系!",accountDto.getMobile());
        }
    }

    private void removeBind(List<String> codeList, AccountDto accountDto) {
        if(!codeList.isEmpty()){
            List<AccountAndHouseSipData> accountAndHouseSipDataList=new ArrayList<>();
            String accountCode=accountDto.getAccountCode();
            for(String code:codeList){
                AccountAndHouseSipData accountAndHouseSipData=new AccountAndHouseSipData(accountCode,code);
                accountAndHouseSipDataList.add(accountAndHouseSipData);
            }
            if(sipUtils.unbind(accountAndHouseSipDataList)){
                logger.info("用户房间关系解除绑定成功!");
            }else {
                logger.error("用户房间关系解绑失败!");
            }
        }else {
            logger.info("未查询到账号:{}的房间绑定关系!",accountDto.getMobile());
        }

    }


//    /**
//     * 根据手机号码查询是否为企业管理人 合版1130
//     * @param mobile
//     * @return
//     */
//    @PostMapping(value = "getEnterpriseAdmin")
//    @ApiVersion(1.0)
//    private LinphoneResult getEnterpriseAdmin(String mobile){
//        LinphoneResult linphoneResult = new LinphoneResult();
//        Map<String,Object> map  = new HashMap<>();
////        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
//        RemoteModelResult remoteModelResult1 = accountApi.getCompanyId(mobile);
////        String companyId = accountApi.getCompanyId(mobile);
//        String companyId = (String) remoteModelResult1.getModel();
//
//        if("".equals(companyId) || companyId == null){
//            map.put("enterpriseId","");
//            map.put("enterpriseName","");
//            map.put("manager",0);
//            linphoneResult.setCode(ReturnCode.API_RESOLVE_SUCCESS.getCode());
//            linphoneResult.setData(map);
//            linphoneResult.setMessage(ReturnCode.API_RESOLVE_SUCCESS.getDescription());
//        }else{
//            RemoteModelResult<EnterpriseCustNew> remoteModelResult = enterpriseCustApi.getEnterpriseAdmin(companyId,mobile);
//            logger.debug("根据手机号码,远程调用返回数据：{}",remoteModelResult);
//
//
//            if(remoteModelResult.isSuccess()){
//                EnterpriseCustNew enterpriseCustNew = remoteModelResult.getModel();
//                if(enterpriseCustNew != null){
//                    map.put("enterpriseId",enterpriseCustNew.getEnterpriseId());
//                    map.put("enterpriseName",enterpriseCustNew.getEnterpriseName());
//                    map.put("manager",1);
//                }else {
//                    map.put("enterpriseId","");
//                    map.put("enterpriseName","");
//                    map.put("manager",0);
//                }
//                linphoneResult.setCode(ReturnCode.API_RESOLVE_SUCCESS.getCode());
//                linphoneResult.setData(map);
//                linphoneResult.setMessage(ReturnCode.API_RESOLVE_SUCCESS.getDescription());
//            }else{
//                linphoneResult.setCode(ReturnCode.API_RESOLVE_FAIL.getCode());
//                linphoneResult.setData(map);
//                linphoneResult.setMessage(ReturnCode.API_RESOLVE_FAIL.getDescription());
//            }
//        }
//
//        return  linphoneResult;
//    }
}
