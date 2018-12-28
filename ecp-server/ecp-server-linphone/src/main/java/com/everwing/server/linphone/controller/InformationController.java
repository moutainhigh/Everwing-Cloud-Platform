package com.everwing.server.linphone.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.platform.api.InformationApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 身份证 信息放入
 * zyf
 *
 */

@RestController
@RequestMapping("/{version}")
public class InformationController {
    private static final Logger logger= LogManager.getLogger(InformationController.class);


    @Autowired
    private InformationApi informationApi;


    /**
     *  查询某个账号是否已经上传身份认证
     * @param accountCode
     * @return
     */
    @PostMapping(value = "getIdentity")
    @ApiVersion(1.0)
    public LinphoneResult getIdentity(String accountCode){
        RemoteModelResult RemoteModelResult = informationApi.getIdentity(accountCode);
        return new LinphoneResult(RemoteModelResult);
    }


    /**
     * 邻音用户完善身份信息，输入姓名和身份证号码。
     * @param name
     * @param id
     * @return
     */
    @PostMapping(value = "setIdentity")
    @ApiVersion(1.0)
    public  LinphoneResult setIdentity(HttpServletRequest request, String name, String id){
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        return  informationApi.setIdentity(account,name,id);
    }


}
