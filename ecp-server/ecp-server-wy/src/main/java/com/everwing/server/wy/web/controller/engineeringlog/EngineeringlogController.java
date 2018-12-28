package com.everwing.server.wy.web.controller.engineeringlog;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.operation.Operationlog;
import com.everwing.coreservice.common.wy.entity.property.Engineering.Engineeringlog;
import com.everwing.coreservice.wy.api.engineeringlog.EngineeringlogApi;
import com.everwing.coreservice.wy.api.operation.OperationApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("EngineeringlogController")
public class EngineeringlogController {
    @Autowired
    private EngineeringlogApi engineeringlogApi;


    @RequestMapping(value = "/loadEngineeringlogListPage", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto loadEngineeringlogListPage(@RequestBody Engineeringlog engineeringlog) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result =engineeringlogApi.loadEngineeringlogListPage(ctx, engineeringlog);
        if (result.isSuccess()) {
            baseDto = result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    @RequestMapping(value ="/add",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap insert (@RequestBody Engineeringlog engineeringlog){
        WyBusinessContext ctx = WyBusinessContext.getContext();
        MessageMap mm = new MessageMap();
        RemoteModelResult remoteModelResult =engineeringlogApi.insert(ctx, engineeringlog);
        if (!remoteModelResult.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }
        return mm;
    }
}
