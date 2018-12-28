package com.everwing.server.wy.web.controller.operation;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.operation.Operationlog;
import com.everwing.coreservice.wy.api.operation.OperationApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("OperationController")
public class OperationController {
    @Autowired
    private OperationApi operationApi;


    @RequestMapping(value = "/loadOperationListPage", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto loadOperationListPage(@RequestBody Operationlog operationlog) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = operationApi.loadOperationListPage(ctx, operationlog);
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
    MessageMap insert (@RequestBody Operationlog operationlog){
        WyBusinessContext ctx = WyBusinessContext.getContext();
        MessageMap mm = new MessageMap();
        RemoteModelResult remoteModelResult = operationApi.insert(ctx, operationlog);
        if (!remoteModelResult.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }
        return mm;
    }
}
