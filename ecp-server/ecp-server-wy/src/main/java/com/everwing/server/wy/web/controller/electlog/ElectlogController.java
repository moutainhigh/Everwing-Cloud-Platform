package com.everwing.server.wy.web.controller.electlog;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectlog;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterlog;
import com.everwing.coreservice.wy.api.Electlog.ElectlogApi;
import com.everwing.coreservice.wy.api.WaterlogApi.WaterlogApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("ElectlogController")
public class ElectlogController {
    @Autowired
    private ElectlogApi  electlogApi;


    @RequestMapping(value = "/loadElectlogListPage", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto loadElectlogListPage(@RequestBody TcElectlog  tcElectlog) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result =electlogApi.loadElectlogListPage(ctx, tcElectlog);
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
    MessageMap insert (@RequestBody TcElectlog  tcElectlog){
        WyBusinessContext ctx = WyBusinessContext.getContext();
        MessageMap mm = new MessageMap();
        RemoteModelResult remoteModelResult =electlogApi.insert(ctx,tcElectlog);
        if (!remoteModelResult.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }
        return mm;
    }
}
