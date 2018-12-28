package com.everwing.server.wy.web.controller.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.wy.api.sys.TSysProjectApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/system/TSysProjectController")
public class TSysProjectController {

    @Autowired
    private TSysProjectApi tSysProjectApi;

    /**
     * 分页查询项目列表
     * @param request
     * @param condition
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Project,businessName="查询项目列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPage",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPage(HttpServletRequest request, @RequestBody TSysProjectSearch condition){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tSysProjectApi.listPage(ctx,condition);
        if(result.isSuccess()){
            baseDto = result.getModel();
            mm = baseDto.getMessageMap();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    /**
     * 获取指定的项目信息
     * @param tSysProjectSearch
     * @return
     */
    @RequestMapping(value = "/findByCondition",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto findByCondition(@RequestBody TSysProjectSearch tSysProjectSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tSysProjectApi.findByCondition(ctx,tSysProjectSearch);
        if(result.isSuccess()){
            baseDto = result.getModel();
            mm = baseDto.getMessageMap();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 获取项目汇总信息
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/getSummaryInformationByProjectId/{projectId}",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto getSummaryInformationByProjectId(@PathVariable String projectId){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<Map> result = tSysProjectApi.getSummaryInformationByProjectId(ctx,projectId);
        if(result.isSuccess()){
            Map map = result.getModel();
            baseDto.setT(map);
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Project,businessName="保存项目",operationType= OperationEnum.Save)
    @RequestMapping(value="/save",method =RequestMethod.POST)
    public @ResponseBody  MessageMap save(@RequestBody TSysProject entity){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        entity.setCreaterId(ctx.getUserId());
        entity.setCreaterName(ctx.getStaffName());
        entity.setModifyId(ctx.getUserId());
        entity.setModifyName(ctx.getStaffName());
        RemoteModelResult<MessageMap> result = tSysProjectApi.save(ctx.getCompanyId(),entity);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Project,businessName="删除项目",operationType= OperationEnum.Delete)
    @RequestMapping(value="/delete/{id}",method =RequestMethod.DELETE)
    public @ResponseBody  MessageMap delete(HttpServletRequest request, @PathVariable String id){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        TSysProject entity = new TSysProject();
        entity.setProjectId(id);

        RemoteModelResult<MessageMap> result = tSysProjectApi.delete(ctx.getCompanyId(),entity);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Project,businessName="变更状态",operationType= OperationEnum.Modify)
    @RequestMapping(value = "/changeStatus",method = RequestMethod.POST)
    public @ResponseBody  MessageMap changeStatus(HttpServletRequest request, @RequestBody TSysProject entity){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<MessageMap> result = tSysProjectApi.changeStatus(ctx.getCompanyId(),entity);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }



















    @SuppressWarnings("rawtypes")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Project,businessName="查询所有项目",operationType= OperationEnum.Search)
    @RequestMapping(value="/findAllProject",method =RequestMethod.POST)
    public @ResponseBody BaseDto findAllProject(HttpServletRequest request,@RequestBody TSysProjectSearch search){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return BaseDtoUtils.getDto(tSysProjectApi.findAllProject(ctx.getCompanyId(),search)); 
    }
    
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Project,businessName="查询项目列表以及水电表总数",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPageAndCountMeters",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageAndCountMeters(HttpServletRequest request, @RequestBody TSysProjectSearch condition){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        condition.setLoginName(ctx.getLoginName());
        condition.setCompanyId(ctx.getCompanyId());
        RemoteModelResult<BaseDto> result = tSysProjectApi.listPageAndCountMeters(ctx.getCompanyId(),condition);
        if(result.isSuccess()){
            baseDto = result.getModel();
            mm = baseDto.getMessageMap();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }
}
