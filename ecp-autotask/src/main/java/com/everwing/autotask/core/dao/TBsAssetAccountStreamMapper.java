package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by DELL on 2018/6/4.
 */
@Repository
public interface TBsAssetAccountStreamMapper {

    int singleInsert(TBsAssetAccountStream tBsAssetAccountStream);

    int batchInsert(List<TBsAssetAccountStream> insertStreamList);
}
