package com.everwing.server.wy.web.controller.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAsset;
import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAssetSearch;
import com.everwing.coreservice.wy.api.building.TcPublicAssetApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zyf on 2018/10/30.
 */

@Controller
@RequestMapping("/TcPublicAssetController")
public class TcPubuilcAssetController {
    @Autowired
    private TcPublicAssetApi tcPublicAssetApi  ;

    @RequestMapping(value = "loadPublicAssetListPage", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto loadPublicAssetListPage(@RequestBody TcPublicAssetSearch tcPublicAssetSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        RemoteModelResult<BaseDto> result = tcPublicAssetApi.loadPublicAssetlistPage(WyBusinessContext.getContext(), tcPublicAssetSearch);
        if (!result.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        } else {
            baseDto = result.getModel();
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    @RequestMapping(value = "loadPublicWaterlistPage", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto loadPublicWaterlistPage(@RequestBody TcPublicAssetSearch tcPublicAssetSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        RemoteModelResult<BaseDto> result = tcPublicAssetApi.loadPublicWaterlistPage(WyBusinessContext.getContext(), tcPublicAssetSearch);
        if (!result.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        } else {
            baseDto = result.getModel();
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }



    @RequestMapping(value = "InsertAsset", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap InsertAsset(@RequestBody TcPublicAsset tcPublicAsset) {
        MessageMap mm = new MessageMap();
        RemoteModelResult<MessageMap> result = tcPublicAssetApi.InsertAsset(WyBusinessContext.getContext(), tcPublicAsset);
        if (result.isSuccess()) {
            mm = result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    @RequestMapping(value = "EditAsset", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap EditAsset(@RequestBody TcPublicAsset tcPublicAsset) {
        MessageMap mm = new MessageMap();
        RemoteModelResult<MessageMap> result = tcPublicAssetApi.EditAsset(WyBusinessContext.getContext(), tcPublicAsset);
        if (result.isSuccess()) {
            mm = result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }



    @RequestMapping(value = "deletePublicAsset/{Id}", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap InsertAsset(@PathVariable String Id) {
        MessageMap mm = new MessageMap();
        RemoteModelResult<MessageMap> result = tcPublicAssetApi.deletePublicAsset(WyBusinessContext.getContext(),Id);
        if (result.isSuccess()) {
            mm = result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }



    @RequestMapping(value = "SearchAsset", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto SearchAsset(@RequestBody String project) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        RemoteModelResult<BaseDto> result = tcPublicAssetApi.SearchAsset(WyBusinessContext.getContext(), project);
        if (!result.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        } else {
            baseDto = result.getModel();
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }




}
