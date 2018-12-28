package com.everwing.server.wy.web.controller.waterlog;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterlog;
import com.everwing.coreservice.wy.api.WaterlogApi.WaterlogApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("WaterlogController")
public class WaterlogController {
    @Autowired
    private WaterlogApi  waterlogApi;


    @RequestMapping(value = "/loadwaterlogListPage", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto loadWaterlogListPage(@RequestBody TcWaterlog  tcWaterlog) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result =waterlogApi.loadWaterlogListPage(ctx, tcWaterlog);
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
    MessageMap insert (@RequestBody TcWaterlog  tcWaterlog){
        WyBusinessContext ctx = WyBusinessContext.getContext();
        MessageMap mm = new MessageMap();
        RemoteModelResult remoteModelResult =waterlogApi.insert(ctx, tcWaterlog);
        if (!remoteModelResult.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }
        return mm;
    }
}
