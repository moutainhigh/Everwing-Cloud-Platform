package com.everwing.server.wy.web.controller.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompany;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanySearch;
import com.everwing.coreservice.wy.api.sys.TSysCompanyApi;
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
@RequestMapping("/system/TSysCompanyController")
public class TSysCompanyController {
    @Autowired
    private TSysCompanyApi tSysCompanyApi;

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Company,businessName="查询公司列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPage",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPage(HttpServletRequest request, @RequestBody TSysCompanySearch condition){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();


        RemoteModelResult<BaseDto> result = tSysCompanyApi.listPage(ctx.getCompanyId(),condition);
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

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Company,businessName="保存公司",operationType= OperationEnum.Save)
    @RequestMapping(value="/save",method =RequestMethod.POST)
    public @ResponseBody  MessageMap save(HttpServletRequest request,@RequestBody TSysCompany entity){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<MessageMap> result = tSysCompanyApi.save(ctx.getCompanyId(),entity);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Company,businessName="删除公司",operationType= OperationEnum.Delete)
    @RequestMapping(value="/delete/{id}",method =RequestMethod.DELETE)
    public @ResponseBody  MessageMap delete(HttpServletRequest request, @PathVariable String id){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        TSysCompany entity = new TSysCompany();
        entity.setCompanyId(id);

        RemoteModelResult<MessageMap> result = tSysCompanyApi.delete(ctx.getCompanyId(),entity);
        if(result.isSuccess()){
            mm = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }
}
