package com.everwing.server.wy.web.controller.sys;/**
 * Created by wust on 2018/12/19.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodataSearch;
import com.everwing.coreservice.wy.api.common.SynchrodataApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Function:同步数据控制器
 * Reason:
 * Date:2018/12/19
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/SynchrodataController")
public class SynchrodataController {
    @Autowired
    private SynchrodataApi synchrodataApi;

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Synchrodata,businessName="查询数据同步列表",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPage",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPage(@RequestBody TSynchrodataSearch tSynchrodataSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<BaseDto> result = synchrodataApi.listPage(ctx,tSynchrodataSearch);
        if(result.isSuccess()){
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Synchrodata,businessName="同步数据",operationType= OperationEnum.Modify)
    @RequestMapping(value = "/syncData/{id}",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap syncData(@PathVariable String id){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        RemoteModelResult<MessageMap> result = synchrodataApi.syncData(ctx,ids);
        if(!result.isSuccess()){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }
}
