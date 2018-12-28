package com.everwing.coreservice.wy.api.sys;/**
 * Created by wust on 2017/4/10.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017-4-10 15:33:50
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
@Component
public class TSysUserApi {
    @Autowired
    private TSysUserService tSysUserService;


    public RemoteModelResult<BaseDto> listPageUser(WyBusinessContext ctx, TSysUserSearch condition){
        BaseDto baseDto = tSysUserService.listPageUser(ctx,condition);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }

    public RemoteModelResult<TSysUser> findByPrimaryKey(String userId){
        RemoteModelResult<TSysUser> result = new RemoteModelResult<>();
        result.setModel(tSysUserService.findByPrimaryKey(CommonUtils.getCompanyIdByCurrRequest(),userId));
        return result;
    }

    public RemoteModelResult<List<TSysUserList>> findByCondition(WyBusinessContext ctx, TSysUserSearch tSysUserSearch) {
        RemoteModelResult<List<TSysUserList>> remoteModelResult = new RemoteModelResult<>();
        List<TSysUserList> tSysUserLists = tSysUserService.findByCondition(ctx,tSysUserSearch);
        remoteModelResult.setModel(tSysUserLists);
        return remoteModelResult;
    }



    public RemoteModelResult<MessageMap> insert(WyBusinessContext ctx, TSysUser entity){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(tSysUserService.insert(ctx,entity));
        return remoteModelResult;
    }

    public RemoteModelResult<MessageMap> modify(WyBusinessContext ctx, TSysUser entity){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(tSysUserService.modify(ctx,entity));
        return remoteModelResult;
    }



    public RemoteModelResult<MessageMap> login(String loginName, String password){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(tSysUserService.login(loginName,password));
        return remoteModelResult;
    }

	public RemoteModelResult<BaseDto> listPageUserOther(WyBusinessContext ctx, TSysUserSearch condition) {
		return new RemoteModelResult<BaseDto>(this.tSysUserService.listPageUserOther(ctx.getCompanyId(),condition));
	}

	public RemoteModelResult<BaseDto> listPageUserInJg(WyBusinessContext ctx, TSysUserSearch condition) {
		return new RemoteModelResult<BaseDto>(this.tSysUserService.listPageUserInJg(ctx.getCompanyId(),condition));
	}

    public RemoteModelResult<List<Map<String,String>>> listByCompanyId(String companyId) {
        return new RemoteModelResult<>(tSysUserService.findByCompanyId(companyId));
    }
}
