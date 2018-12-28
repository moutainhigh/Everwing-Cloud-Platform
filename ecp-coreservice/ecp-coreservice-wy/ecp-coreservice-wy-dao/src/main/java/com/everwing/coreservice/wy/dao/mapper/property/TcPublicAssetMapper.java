package com.everwing.coreservice.wy.dao.mapper.property;

import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAsset;
import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAssetList;
import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAssetSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TcPublicAssetMapper {
    List<TcPublicAsset> loadPublicAssetlistPage(TcPublicAssetSearch tcPublicAssetSearch);

    List<TcPublicAsset> loadPublicWaterlistPage(TcPublicAssetSearch tcPublicAssetSearch);

    String SearchMaxCode(@Param("isHold")String isHold, @Param("projectId")String projectId);

    int insert(TcPublicAsset tcPublicAsset);

    int deletePublicAsset(String id);

    int EditAsset(TcPublicAsset tcPublicAsset);

    List<TcPublicAssetList> SearchAsset(String project);

    TcPublicAsset selectById(String id);
}
