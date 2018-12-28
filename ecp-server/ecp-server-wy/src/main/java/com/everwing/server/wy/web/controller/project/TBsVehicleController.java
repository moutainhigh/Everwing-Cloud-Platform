package com.everwing.server.wy.web.controller.project;/**
 * Created by wust on 2017/8/1.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicle;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicleList;
import com.everwing.coreservice.common.wy.entity.property.vehicle.TVehicleSearch;
import com.everwing.coreservice.wy.api.business.vehicle.TBsVehicleApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/1
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/TBsVehicleController")
public class TBsVehicleController {
    @Autowired
    private TBsVehicleApi tBsVehicleApi;

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Vehicle,businessName="分页查询车辆信息",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPage",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPage(HttpServletRequest request, @RequestBody TVehicleSearch condition){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = tBsVehicleApi.listPage(ctx,condition);
        if(result.isSuccess()){
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Vehicle,businessName="查询指定条件车辆信息",operationType= OperationEnum.Search)
    @RequestMapping(value = "/findByCondition",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto findByCondition(HttpServletRequest request, @RequestBody TVehicleSearch condition){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<List<TVehicleList>> result = tBsVehicleApi.findByCondition(ctx,condition);
        if(result.isSuccess()){
            baseDto.setLstDto(result.getModel());
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Vehicle,businessName="新增车辆",operationType= OperationEnum.Insert)
    @RequestMapping(value="/add",method =RequestMethod.POST)
    public @ResponseBody  MessageMap add(HttpServletRequest request,@RequestBody TVehicle entity){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        entity.setCreaterId(ctx.getUserId());
        entity.setCreaterName(ctx.getStaffName());
        RemoteModelResult result = tBsVehicleApi.insert(ctx,entity);
        if(result.isSuccess()){
            mm = result.getModel() == null ? null : (MessageMap)result.getModel();
        }
        return mm;
    }

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Vehicle,businessName="修改车辆",operationType= OperationEnum.Modify)
    @RequestMapping(value="/modify",method =RequestMethod.POST)
    public @ResponseBody  MessageMap modify(HttpServletRequest request,@RequestBody TVehicle entity){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();
        entity.setModifyId(ctx.getUserId());
        entity.setModifyName(ctx.getStaffName());
        RemoteModelResult result = tBsVehicleApi.update(ctx,entity);
        if(result.isSuccess()){
            mm = result.getModel() == null ? null : (MessageMap)result.getModel();
        }
        return mm;
    }

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Vehicle,businessName="删除车辆",operationType= OperationEnum.Delete)
    @RequestMapping(value="/delete/{id}",method =RequestMethod.DELETE)
    public @ResponseBody  MessageMap delete(HttpServletRequest request, @PathVariable String id){
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        TVehicle entity = new TVehicle();
        entity.setId(id);
        RemoteModelResult result = tBsVehicleApi.delete(ctx,entity);
        if(result.isSuccess()){
            mm = result.getModel() == null ? null : (MessageMap)result.getModel();
        }
        return mm;
    }
}
