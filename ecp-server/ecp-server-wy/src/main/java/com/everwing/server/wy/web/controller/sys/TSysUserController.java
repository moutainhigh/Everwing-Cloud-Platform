package com.everwing.server.wy.web.controller.sys;/**
 * Created by wust on 2017/4/12.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.platform.entity.generated.AccountIdentity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.LookupEnum;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookupSelectSearch;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookupSelectView;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.common.wy.entity.system.user.UserResourceList;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.platform.api.IdentityApi;
import com.everwing.coreservice.wy.api.sys.TSysLookupApi;
import com.everwing.coreservice.wy.api.sys.TSysOrganizationApi;
import com.everwing.coreservice.wy.api.sys.TSysUserApi;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017-4-12 12:53:01
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
@Controller
@RequestMapping("/system/TSysUserController")
public class TSysUserController {
    @Autowired
    private TSysUserApi tSysUserApi;

    @Autowired
    private AccountApi accountApi;

    @Autowired
    private IdentityApi identityApi;

    @Autowired
    private TSysLookupApi tSysLookupApi;

    @Autowired
    private TSysOrganizationApi tSysOrganizationApi;


    /**
     * 用户列表分页查询
     * @param request
     * @param condition
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_User,businessName="查询用户列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPageUser",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageUser(HttpServletRequest request, @RequestBody TSysUserSearch condition){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<BaseDto> result = tSysUserApi.listPageUser(ctx,condition);
        if(result.isSuccess()){
            mm.setFlag(MessageMap.INFOR_SUCCESS);
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    /**
     * 根据工号获取其组织关系列表
     * @param request
     * @param staffNumber
     * @return
     */
    @RequestMapping(value = "/getUserResourceListByStaffNumber/{staffNumber}",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto getUserResourceListByStaffNumber(HttpServletRequest request, @PathVariable String staffNumber){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<List<UserResourceList>> result = tSysOrganizationApi.getUserResourceListByKey(ctx.getCompanyId(),staffNumber);
        if(result.isSuccess()){
            mm.setFlag(MessageMap.INFOR_SUCCESS);
            baseDto.setLstDto(result.getModel());
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 根据登录账号获取用户信息
     * @param loginName
     * @return
     */
    @RequestMapping(value = "/getUserByLoginName/{loginName}",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto getUserByLoginName(@PathVariable String loginName){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        TSysUserSearch tSysUserSearch = new TSysUserSearch();
        tSysUserSearch.setLoginName(loginName);
        RemoteModelResult<List<TSysUserList>> userListRemoteModelResult = tSysUserApi.findByCondition(ctx,tSysUserSearch);
        if(userListRemoteModelResult.isSuccess()){
            List<TSysUserList> tSysUserLists = userListRemoteModelResult.getModel();
            if(CollectionUtils.isNotEmpty(tSysUserLists)){
                TSysUserList tSysUserList = tSysUserLists.get(0);
                baseDto.setT(tSysUserList);
            }else {
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("没有该用户");
            }
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(userListRemoteModelResult.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    /**
     * 离职
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_User,businessName="离职",operationType= OperationEnum.Modify)
    @RequestMapping(value = "/resign/{loginName}",method = RequestMethod.POST)
    public @ResponseBody  MessageMap resign(@PathVariable String loginName){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        TSysUserSearch tSysUserSearch = new TSysUserSearch();
        tSysUserSearch.setLoginName(loginName);
        RemoteModelResult<List<TSysUserList>> remoteModelResult = tSysUserApi.findByCondition(ctx,tSysUserSearch);
        if(remoteModelResult.isSuccess()){
            TSysUserList tSysUserList = remoteModelResult.getModel().get(0);


            if(LookupItemEnum.staffType_systemAdmin.getStringValue().equalsIgnoreCase(tSysUserList.getType())){
                mm.setFlag(MessageMap.INFOR_WARNING);
                mm.setMessage("不能对超级管理员执行此操作");
            }else {
                if(LookupItemEnum.staffState_joined.getStringValue().equals(tSysUserList.getStaffState())){
                    tSysUserList.setStaffState(LookupItemEnum.staffState_left.getStringValue());
                    RemoteModelResult<MessageMap> result = tSysUserApi.modify(ctx,tSysUserList);
                    if(result.isSuccess()){
                        mm = result.getModel();
                    }else{
                        mm.setFlag(MessageMap.INFOR_WARNING);
                        mm.setMessage(result.getMsg());
                    }
                }else{
                    mm.setFlag(MessageMap.INFOR_WARNING);
                    mm.setMessage("不合法数据，请和管理员确认。");
                }
            }
        }

        return mm;
    }





    /**
     * 从平台获取用户
     * @param request
     * @param loginName
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_User,businessName="邀请入职",operationType= OperationEnum.Search)
    @RequestMapping(value = "/getUserInfoByLoginName/{loginName}",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap getUserInfoByLoginName(HttpServletRequest request,@PathVariable String loginName){
        MessageMap mm = new MessageMap();

        final WyBusinessContext ctx = WyBusinessContext.getContext();

        // 检测该员工是否已经在本公司入职成功过
        TSysUserSearch tSysUserSearch = new TSysUserSearch();
        tSysUserSearch.setLoginName(loginName);
        RemoteModelResult<List<TSysUserList>> userListRemoteModelResult = tSysUserApi.findByCondition(ctx,tSysUserSearch);
        if(userListRemoteModelResult.isSuccess()){
            List<TSysUserList> tSysUserLists = userListRemoteModelResult.getModel();
            if(CollectionUtils.isEmpty(tSysUserLists)){   // 如果在当前公司还没有入职，需要判断该员工是否已经在其他公司入职
                // 看看该员工在其他公司是否已经入职
                RemoteModelResult<Account> remoteModelResultAccount = accountApi.queryByAccountName(loginName,Dict.ACCOUNT_TYPE_STAFF.getIntValue());
                if (remoteModelResultAccount.isSuccess()){
                    Account account = remoteModelResultAccount.getModel();
                    if(account == null){
                        mm.setFlag(MessageMap.INFOR_WARNING);
                        mm.setMessage("该员工还没有注册。");
                        return mm;
                    }

                    /**
                     * 看看该账号是否已经入职其他公司
                     */
                    String companyId4account = account.getCompanyId();
                    if(StringUtils.isNotBlank(CommonUtils.null2String(companyId4account))){
                        WyBusinessContext ctx4account = ctx;
                        ctx4account.setCompanyId(companyId4account);
                        RemoteModelResult<List<TSysUserList>> userListRemoteModelResult4account = tSysUserApi.findByCondition(ctx4account,tSysUserSearch);
                        if(userListRemoteModelResult4account.isSuccess()) {
                            List<TSysUserList> tSysUserLists4account = userListRemoteModelResult4account.getModel();
                            if(CollectionUtils.isNotEmpty(tSysUserLists4account)){
                                TSysUserList tSysUserList4account = tSysUserLists4account.get(0);
                                if(!LookupItemEnum.staffState_left.getStringValue().equalsIgnoreCase(tSysUserList4account.getStaffState())){
                                    mm.setFlag(MessageMap.INFOR_WARNING);
                                    mm.setMessage("邀请入职失败：原因是该员工已经在其他公司入职。");
                                    return mm;
                                }
                            }
                        }
                    }
                }

                /**
                 * 如果校验通过，接着从平台获取认证后的员工数据
                 */
                RemoteModelResult<AccountIdentity> remoteModelResult = identityApi.queryByAccountName(loginName, Dict.ACCOUNT_TYPE_STAFF.getIntValue());
                if(remoteModelResult.isSuccess()){
                    TSysUserList tSysUserList = new TSysUserList();
                    AccountIdentity account = remoteModelResult.getModel();
                    if(account != null){
                        TSysLookupSelectSearch tSysLookupSelectSearch = new TSysLookupSelectSearch();
                        tSysLookupSelectSearch.setParentCode(LookupEnum.sex.name());
                        tSysLookupSelectSearch.setLookupItemCode(account.getSex() == null ? LookupItemEnum.sex_male.getStringValue() : LookupItemEnum.sex_female.getStringValue());
                        String sexName = "";
                        RemoteModelResult<List<TSysLookupSelectView>> remoteModelResultSex = tSysLookupApi.findLookupItem(ctx,tSysLookupSelectSearch);
                        if(remoteModelResultSex.isSuccess()){
                            sexName = remoteModelResultSex.getModel().get(0).getName();
                        }


                        tSysLookupSelectSearch.setParentCode(LookupEnum.documentType.name());
                        tSysLookupSelectSearch.setLookupItemCode(account.getIdentityType()+"");
                        String documentTypeName = "";
                        RemoteModelResult<List<TSysLookupSelectView>> remoteModelResultDocumentType = tSysLookupApi.findLookupItem(ctx,tSysLookupSelectSearch);
                        if(remoteModelResultDocumentType.isSuccess()){
                            documentTypeName = remoteModelResultDocumentType.getModel().get(0).getName();
                        }

                        tSysUserList.setUserId(account.getAccountId());
                        tSysUserList.setLoginName(loginName);
                        tSysUserList.setStaffName(account.getRealName());
                        tSysUserList.setDocumentType(account.getIdentityType()+"");
                        tSysUserList.setDocumentTypeName(documentTypeName);
                        tSysUserList.setDocumentNumber(account.getIdentityCode());
                        tSysUserList.setSex(account.getSex() == null ? LookupItemEnum.sex_male.getStringValue() : LookupItemEnum.sex_female.getStringValue());
                        tSysUserList.setSexName(sexName);
                        tSysUserList.setMobileTelephone(account.getMobile());
                        tSysUserList.setEmail(account.getEmail());
                        tSysUserList.setFileIds(account.getIdentityFileIds());
                        mm.setObj(tSysUserList);
                    }else{
                        mm.setFlag(MessageMap.INFOR_WARNING);
                        mm.setMessage("邀请入职失败：原因是该员工没有认证通过。");
                    }
                }else{
                    mm.setFlag(MessageMap.INFOR_WARNING);
                    mm.setMessage(remoteModelResult.getMsg());
                }
            }else{  // 已经在当前公司入职，就刷新数据
                RemoteModelResult<AccountIdentity> remoteModelResult = identityApi.queryByAccountName(loginName, Dict.ACCOUNT_TYPE_STAFF.getIntValue());
                if(remoteModelResult.isSuccess()){
                    TSysUserList tSysUserList = new TSysUserList();
                    AccountIdentity account = remoteModelResult.getModel();
                    if(account != null){
                        TSysLookupSelectSearch tSysLookupSelectSearch = new TSysLookupSelectSearch();
                        tSysLookupSelectSearch.setParentCode(LookupEnum.sex.name());
                        tSysLookupSelectSearch.setLookupItemCode(account.getSex() == null ? LookupItemEnum.sex_male.getStringValue() : LookupItemEnum.sex_female.getStringValue());
                        String sexName = "";
                        RemoteModelResult<List<TSysLookupSelectView>> remoteModelResultSex = tSysLookupApi.findLookupItem(ctx,tSysLookupSelectSearch);
                        if(remoteModelResultSex.isSuccess()){
                            sexName = remoteModelResultSex.getModel().get(0).getName();
                        }


                        tSysLookupSelectSearch.setParentCode(LookupEnum.documentType.name());
                        tSysLookupSelectSearch.setLookupItemCode(account.getIdentityType()+"");
                        String documentTypeName = "";
                        RemoteModelResult<List<TSysLookupSelectView>> remoteModelResultDocumentType = tSysLookupApi.findLookupItem(ctx,tSysLookupSelectSearch);
                        if(remoteModelResultDocumentType.isSuccess()){
                            documentTypeName = remoteModelResultDocumentType.getModel().get(0).getName();
                        }

                        tSysUserList.setLoginName(loginName);
                        tSysUserList.setStaffName(account.getRealName());
                        tSysUserList.setDocumentType(account.getIdentityType()+"");
                        tSysUserList.setDocumentTypeName(documentTypeName);
                        tSysUserList.setDocumentNumber(account.getIdentityCode());
                        tSysUserList.setSex(account.getSex() == null ? LookupItemEnum.sex_male.getStringValue() : LookupItemEnum.sex_female.getStringValue());
                        tSysUserList.setSexName(sexName);
                        tSysUserList.setMobileTelephone(account.getMobile());
                        tSysUserList.setEmail(account.getEmail());
                        tSysUserList.setFileIds(account.getIdentityFileIds());
                        mm.setObj(tSysUserList);
                    }else{
                        mm.setFlag(MessageMap.INFOR_WARNING);
                        mm.setMessage("邀请入职失败：原因是该员工没有认证通过。");
                    }
                }else{
                    mm.setFlag(MessageMap.INFOR_WARNING);
                    mm.setMessage(remoteModelResult.getMsg());
                }
            }
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(userListRemoteModelResult.getMsg());
        }
        return mm;
    }

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_User,businessName="邀请入职",operationType= OperationEnum.Insert)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public @ResponseBody  MessageMap save(@RequestBody TSysUser entity){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        TSysUserSearch tSysUserSearch = new TSysUserSearch();
        tSysUserSearch.setLoginName(entity.getLoginName());
        RemoteModelResult<List<TSysUserList>> userListRemoteModelResult = tSysUserApi.findByCondition(ctx,tSysUserSearch);
        if(userListRemoteModelResult.isSuccess()){
            List<TSysUserList> tSysUserLists = userListRemoteModelResult.getModel();
            if(CollectionUtils.isNotEmpty(tSysUserLists)){
                TSysUser modifyEntity = tSysUserLists.get(0);
                modifyEntity.setCompanyId(ctx.getCompanyId());
                modifyEntity.setModifyId(ctx.getUserId());
                modifyEntity.setModifyName(ctx.getStaffName());
                modifyEntity.setStaffState(LookupItemEnum.staffState_joined.getStringValue());
                modifyEntity.setDocumentType(entity.getDocumentType());
                modifyEntity.setDocumentNumber(entity.getDocumentNumber());
                modifyEntity.setSex(entity.getSex());
                modifyEntity.setStaffName(entity.getStaffName());
                modifyEntity.setMobileTelephone(entity.getMobileTelephone());
                modifyEntity.setEmail(entity.getEmail());
                RemoteModelResult<MessageMap> remoteModelResult = tSysUserApi.modify(ctx,modifyEntity);
                if(remoteModelResult.isSuccess()){
                    mm = remoteModelResult.getModel();

                    // 物业这边邀请用户数据到数据库成功后，回写平台的公司编码
                    accountApi.updateCompanyIdByAccountNameAndType(entity.getLoginName(),Dict.ACCOUNT_TYPE_STAFF.getIntValue(),ctx.getCompanyId());
                }else {
                    mm.setFlag(MessageMap.INFOR_WARNING);
                    mm.setMessage(remoteModelResult.getMsg());
                }
            }else{
                // 新增员工信息

                entity.setCompanyId(ctx.getCompanyId());
                entity.setCreaterId(ctx.getUserId());
                entity.setCreaterName(ctx.getStaffName());
                RemoteModelResult<MessageMap> remoteModelResult = tSysUserApi.insert(ctx,entity);
                if(remoteModelResult.isSuccess()){
                    // 物业这边邀请用户数据到数据库成功后，回写平台的公司编码
                    accountApi.updateCompanyIdByAccountNameAndType(entity.getLoginName(),Dict.ACCOUNT_TYPE_STAFF.getIntValue(),ctx.getCompanyId());

                    mm = remoteModelResult.getModel();
                }else{
                    mm.setFlag(MessageMap.INFOR_WARNING);
                    mm.setMessage(remoteModelResult.getMsg());
                }
            }
        }
        return mm;
    }
    
    /**
     * 用户列表分页查询, 支持姓名/手机/身份证号码模糊查询, 当前用途: 银账交割成员查询, 工单处理人查询
     * @param request
     * @param condition
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_User,businessName="查询用户列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPageUserOther",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageUserOther(HttpServletRequest request, @RequestBody TSysUserSearch condition){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<BaseDto> result = tSysUserApi.listPageUserOther(ctx,condition);
        if(result.isSuccess()){
            mm.setFlag(MessageMap.INFOR_SUCCESS);
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }
    
    /**
     * 用户 银账交割成员查询, 当前用途: 银账交割.同一项目下 同一用户只能出现一次
     * @param request
     * @param condition
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_User,businessName="联合查询用户/银账交割成员列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPageUserInJg",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageUserInJg(HttpServletRequest request, @RequestBody TSysUserSearch condition){
    	BaseDto baseDto = new BaseDto();
    	MessageMap mm = new MessageMap();
    	
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	
    	RemoteModelResult<BaseDto> result = tSysUserApi.listPageUserInJg(ctx,condition);
    	if(result.isSuccess()){
    		mm.setFlag(MessageMap.INFOR_SUCCESS);
    		baseDto = result.getModel();
    	}else{
    		mm.setFlag(MessageMap.INFOR_WARNING);
    		mm.setMessage(result.getMsg());
    	}
    	baseDto.setMessageMap(mm);
    	return baseDto;
    }
}
