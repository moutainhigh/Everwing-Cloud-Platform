package com.everwing.server.wy.web.controller.sys;/**
 * Created by wust on 2017/4/12.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.system.relation.TSysRoleResource;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRole;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleSearch;
import com.everwing.coreservice.wy.api.sys.TSysRoleApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * Function:
 * Reason:
 * Date:2017-4-12 12:53:01
 * @author wusongti@lii.com.cn/wusongti@163.com
 */
@Controller
@RequestMapping("/system/TSysRoleController")
public class TSysRoleController {
    @Autowired
    private TSysRoleApi tSysRoleApi;

    /**
     * 角色列表分页查询
     * @param request
     * @param condition
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Role,businessName="查询角色列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPageRole",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageRole(HttpServletRequest request, @RequestBody TSysRoleSearch condition){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tSysRoleApi.listPageRole(ctx,condition);
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
     * 保存角色
     * @param request
     * @param tSysRole
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Role,businessName="新增或保存角色",operationType= OperationEnum.Save)
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public @ResponseBody  MessageMap add(HttpServletRequest request, @RequestBody TSysRole tSysRole){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<MessageMap> result = tSysRoleApi.save(ctx,tSysRole);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }



    /**
     * 删除角色信息
     * @param request
     * @param guids
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Role,businessName="删除角色",operationType= OperationEnum.Delete)
    @RequestMapping(value = "/delete/{guids}",method = RequestMethod.DELETE)
    public @ResponseBody  MessageMap delete(HttpServletRequest request,@PathVariable String guids){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<MessageMap> result = tSysRoleApi.delete(ctx.getCompanyId(),guids);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }

    /**
     * 启用/禁用
     * @param request
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Role,businessName="变更角色状态",operationType= OperationEnum.Modify)
    @RequestMapping(value = "/changeStatus",method = RequestMethod.POST)
    public @ResponseBody  MessageMap changeStatus(HttpServletRequest request, @RequestBody TSysRole entity){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<MessageMap> result = tSysRoleApi.changeStatus(ctx,entity);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }



    /**
     * 根据角色获取资源树
     * @param request
     * @param toRoleId
     * @return
     */
    @RequestMapping(value = "/findResourceTreeByRoleId/{toRoleId}",method = RequestMethod.POST)
    public  @ResponseBody  BaseDto findResourceTreeByRoleId(HttpServletRequest request,@PathVariable String toRoleId){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<BaseDto> result = tSysRoleApi.findResourceTreeByRoleId(ctx.getCompanyId(),toRoleId);
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
     * 授权
     * @param request
     * @param rr
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Role,businessName="授权",operationType= OperationEnum.Modify)
    @RequestMapping(value = "/authorize",method = RequestMethod.POST)
    public @ResponseBody  MessageMap authorize(HttpServletRequest request,@RequestBody TSysRoleResource rr){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<MessageMap> result = tSysRoleApi.authorize(ctx,rr);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }
}
