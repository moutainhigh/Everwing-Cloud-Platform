package com.everwing.coreservice.wy.api.building;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAsset;
import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAssetSearch;
import com.everwing.coreservice.common.wy.service.TcPublicAsset.TcPublicAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @ClassName: TcPublicAssetApi
 * @Author:Ck
 * @Date: 2018/10/30
 **/
@Component
public class TcPublicAssetApi {

    @Autowired
    private TcPublicAssetService tcPublicAssetService;
    public RemoteModelResult<BaseDto> loadPublicAssetlistPage(WyBusinessContext ctx, TcPublicAssetSearch tcPublicAssetSearch) {
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(tcPublicAssetService.loadPublicAssetlistPage(ctx, tcPublicAssetSearch));
        return result;
    }

    public RemoteModelResult<BaseDto> loadPublicWaterlistPage(WyBusinessContext ctx, TcPublicAssetSearch tcPublicAssetSearch) {
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(tcPublicAssetService.loadPublicWaterlistPage(ctx, tcPublicAssetSearch));
        return result;
    }

    public RemoteModelResult<MessageMap> InsertAsset(WyBusinessContext ctx, TcPublicAsset tcPublicAsset) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tcPublicAssetService.InsertAsset(ctx, tcPublicAsset));
        return result;
    }

    public RemoteModelResult<MessageMap> deletePublicAsset(WyBusinessContext ctx, String id) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tcPublicAssetService.deletePublicAsset(ctx,id));
        return result;
    }

    public RemoteModelResult<MessageMap> EditAsset(WyBusinessContext ctx, TcPublicAsset tcPublicAsset) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tcPublicAssetService.EditAsset(ctx, tcPublicAsset));
        return result;
    }

    public RemoteModelResult<BaseDto> SearchAsset(WyBusinessContext ctx, String project) {
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(tcPublicAssetService.SearchAsset(ctx, project));
        return result;
    }
}
