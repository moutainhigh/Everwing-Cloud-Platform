package com.everwing.coreservice.common.wy.service.TcPublicAsset;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAsset;
import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAssetSearch;

public interface TcPublicAssetService {
    BaseDto loadPublicAssetlistPage(WyBusinessContext ctx, TcPublicAssetSearch tcPublicAssetSearch);

    BaseDto loadPublicWaterlistPage(WyBusinessContext ctx, TcPublicAssetSearch tcPublicAssetSearch);

    MessageMap InsertAsset(WyBusinessContext ctx, TcPublicAsset tcPublicAsset);

    MessageMap deletePublicAsset(WyBusinessContext ctx, String id);

    MessageMap EditAsset(WyBusinessContext ctx, TcPublicAsset tcPublicAsset);

    BaseDto SearchAsset(WyBusinessContext ctx, String project);
}
