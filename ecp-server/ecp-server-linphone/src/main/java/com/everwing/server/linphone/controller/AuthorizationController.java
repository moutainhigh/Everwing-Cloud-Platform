package com.everwing.server.linphone.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseSipData;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.utils.SipUtils;
import com.everwing.coreservice.platform.api.AuthorizationApi;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/{version}")
public class AuthorizationController {

    private static  final Logger logger= LogManager.getLogger(AuthorizationController.class);

    @Autowired
    private SipUtils sipUtils;

    @Autowired
    private AuthorizationApi authorizationApi;

    @PostMapping(value = "authorization")
    @ApiVersion(1.0)
    public LinphoneResult authorization(String authorizedAccountCode, String authorizeeAccountCode, String startTime, String endTime, String buildingId){
        return authorizationApi.authorization(authorizedAccountCode,authorizeeAccountCode,startTime,endTime,buildingId);
    }
    @PostMapping(value = "queryAuthorized")
    @ApiVersion(1.0)
    public LinphoneResult queryAuthorized(String accountCode){
        return authorizationApi.getAuthorized(accountCode);
    }

    @PostMapping(value = "queryAuthorizee")
    @ApiVersion(1.0)
    public LinphoneResult queryAuthorizee(String accountCode){
        LinphoneResult re=authorizationApi.getAuthorizee(accountCode);
        if(re.isSuccess()){
            Object object=re.getData();
            if(object!=null){
                List<Map<String,String>> authorizees= (List<Map<String, String>>) object;
                if(!authorizees.isEmpty()){
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Iterator<Map<String,String>> iterator=authorizees.iterator();
                    List<Map<String,String>> removeList=new ArrayList<>(authorizees.size());
                    logger.info("开始校验授权时间");
                    while (iterator.hasNext()){
                        Map<String,String> authorizee=iterator.next();
                        String startTime=authorizee.get("startTime");
                        String endTime=authorizee.get("endTime");
                        if(StringUtils.isNotEmpty(startTime)&&StringUtils.isNotEmpty(endTime)) {
                            Date date = new Date();
                            try {
                                Date start = simpleDateFormat.parse(startTime);
                                Date end = simpleDateFormat.parse(endTime);
                                if(date.after(start)&&date.before(end)){
                                    continue;
                                }else {
                                    logger.info("被授权超时，丢弃");
                                    removeList.add(authorizee);
                                    iterator.remove();
                                }
                            } catch (ParseException e) {
                                logger.error("开始时间或结束时间格式设置错误!丢弃");
                                removeList.add(authorizee);
                                iterator.remove();
                            }
                        }else {
                            logger.info("未设置开始时间和结束时间，丢弃！");
                            removeList.add(authorizee);
                            iterator.remove();
                        }
                    }
                    if(!removeList.isEmpty()){
                        logger.info("同步删除绑定信息到sip服务器");
                        List<AccountAndHouseSipData> data=new ArrayList<>(removeList.size());
                        for(Map<String,String> authorizee:removeList){
                            if(!authorizee.isEmpty()) {
                                AccountAndHouseSipData sipData = new AccountAndHouseSipData(accountCode, authorizee.get("buildingCode"));
                                data.add(sipData);
                            }
                        }
                        sipUtils.unbind(data);
                    }
                }
            }
        }
        return re;
    }

    @PostMapping("deleteAuthorizee")
    @ApiVersion(1.0)
    public LinphoneResult deleteAuthorizee(HttpServletRequest request, String authorizeeAccountCodes, String buildingId){
        String accountStr= (String) request.getAttribute("account");
        Account account= JSON.parseObject(accountStr,Account.class);
        return authorizationApi.deleteAuthorizee(account.getAccountCode(),authorizeeAccountCodes,buildingId);
    }
}
