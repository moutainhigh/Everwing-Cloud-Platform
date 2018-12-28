package com.everwing.server.wy.web.controller.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartment;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartmentSearch;
import com.everwing.coreservice.wy.api.sys.TSysDepartmentApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/system/TSysDepartmentController")
public class TSysDepartmentController {
    @Autowired
    private TSysDepartmentApi tSysDepartmentApi;

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Department,businessName="查询部门列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPage",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPage(HttpServletRequest request, @RequestBody TSysDepartmentSearch condition){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tSysDepartmentApi.listPage(ctx,condition);
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

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Department,businessName="新增或修改部门",operationType= OperationEnum.Save)
    @RequestMapping(value="/save",method =RequestMethod.POST)
    public @ResponseBody  MessageMap save(HttpServletRequest request,@RequestBody  TSysDepartment tSysDepartment){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<MessageMap> result = tSysDepartmentApi.save(ctx,tSysDepartment);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Department,businessName="删除部门",operationType= OperationEnum.Delete)
    @RequestMapping(value="/delete/{id}",method =RequestMethod.DELETE)
    public @ResponseBody  MessageMap delete(HttpServletRequest request, @PathVariable String id){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        TSysDepartment entity = new TSysDepartment();
        entity.setDepartmentId(id);

        RemoteModelResult<MessageMap> result = tSysDepartmentApi.delete(ctx,entity);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }

    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Organization,businessName = "通过公司和项目加载部门列表",operationType = OperationEnum.Search)
    @RequestMapping("loadDeptsByProjectId/{projectId}")
    @ResponseBody
    public BaseDto loadDeptsByProjectId(@PathVariable String projectId){
        BaseDto baseDto=new BaseDto();
        if(StringUtils.isNotEmpty(projectId)){
            RemoteModelResult<BaseDto> result=tSysDepartmentApi.loadDeptsByProjectId(projectId);
            MessageMap messageMap=new MessageMap();
            if(result.isSuccess()){
                messageMap.setFlag(MessageMap.INFOR_SUCCESS);
                return BaseDtoUtils.getDto(result);
            }else {
                messageMap.setFlag(MessageMap.INFOR_ERROR);
                messageMap.setMessage(result.getMsg());
                baseDto.setMessageMap(messageMap);
                return baseDto;
            }
        }else {
            MessageMap messageMap=new MessageMap(MessageMap.INFOR_ERROR,"参数传递错误!");
            baseDto.setMessageMap(messageMap);
            return baseDto;
        }
    }
}
