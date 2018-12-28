package com.everwing.coreservice.wy.api.sys;/**
 * Created by wust on 2017/6/20.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/20
 * @author wusongti@lii.com.cn
 */
@Component
public class TSysProjectApi {
    @Autowired
    private TSysProjectService tSysProjectService;

    public RemoteModelResult<BaseDto> listPage(WyBusinessContext ctx, TSysProjectSearch condition){
        BaseDto baseDto = tSysProjectService.listPage(ctx,condition);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }

    public RemoteModelResult<BaseDto> findByCondition(WyBusinessContext ctx, TSysProjectSearch condition){
        BaseDto baseDto = tSysProjectService.findByCondition(ctx,condition);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }

    public RemoteModelResult<Map> getSummaryInformationByProjectId(WyBusinessContext ctx, String projectId){
        Map map = tSysProjectService.getSummaryInformationByProjectId(ctx,projectId);
        RemoteModelResult<Map> result = new RemoteModelResult<>();
        result.setModel(map);
        return result;
    }

    public RemoteModelResult<MessageMap> save(String companyId,TSysProject entity){
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tSysProjectService.save(companyId,entity));
        return result;
    }

    public RemoteModelResult<MessageMap> delete(String companyId,TSysProject entity){
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tSysProjectService.delete(companyId,entity));
        return result;
    }

    public RemoteModelResult<MessageMap> changeStatus(String companyId,TSysProject entity){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(tSysProjectService.changeState(companyId,entity));
        return remoteModelResult;
    }



	public RemoteModelResult<BaseDto> findAllProject(String companyId,TSysProjectSearch search) {
		RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
		result.setModel(tSysProjectService.findAllProject(companyId,search));
		return result;
	}

	public RemoteModelResult<BaseDto> listPageAndCountMeters(String companyId,TSysProjectSearch condition) {
		return new RemoteModelResult<BaseDto>(this.tSysProjectService.listPageAndCountMeters(companyId,condition));
	}
}
