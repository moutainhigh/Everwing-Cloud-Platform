package com.everwing.server.wy.api.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.wy.api.sys.TSysOrganizationApi;
import com.everwing.coreservice.wy.api.sys.TSysUserApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhugeruifei
 * Created by zhugeruifei on 17/8/11.
 */
@RestController
@RequestMapping("{version}")
public class ApiUserinfoController {

    private static final Logger logger= LogManager.getLogger(ApiUserinfoController.class);

    @Autowired
    private TSysOrganizationApi organizationApi;

    @Autowired
    private TSysUserApi userApi;

    @PostMapping("companyInformation")
    @ApiVersion(1.0)
    public LinphoneResult companyInformation(HttpServletRequest request,String accountId){
        Account account= JSON.parseObject((String) request.getAttribute("account"),Account.class);
        WyBusinessContext businessContext=WyBusinessContext.getContext();
        businessContext.setCompanyId(account.getCompanyId());
        TSysUserSearch userSearch=new TSysUserSearch();
        userSearch.setLoginName(account.getMobile());
        RemoteModelResult modelResult=userApi.findByCondition(businessContext,userSearch);
        if(modelResult.isSuccess()){
            logger.debug("userApi调用成功!:{}",modelResult.getModel());
            Object o=modelResult.getModel();
            if(o!=null) {
                List<TSysUserList> userList = (List<TSysUserList>)o;
                if(userList.size()>0){
                    TSysUserList tSysUserList=userList.get(0);
                    String staffNumber=tSysUserList.getStaffNumber();
                    if(staffNumber.isEmpty()){
                        logger.info("未查询到员工信息");
                    }else {
                        RemoteModelResult result=organizationApi.getUserResourceListByKey(account.getCompanyId(),staffNumber);
                        if(result.isSuccess()){
                            logger.info("查询个人公司项目部门信息成功");
                            return new LinphoneResult(result);
                        }
                    }
                }
            }
        }
        return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
    }

}
