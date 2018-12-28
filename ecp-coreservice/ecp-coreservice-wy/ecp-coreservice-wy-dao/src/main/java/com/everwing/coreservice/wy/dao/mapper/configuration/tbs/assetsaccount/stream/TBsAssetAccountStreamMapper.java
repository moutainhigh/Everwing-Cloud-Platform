package com.everwing.coreservice.wy.dao.mapper.configuration.tbs.assetsaccount.stream;

import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;

import java.util.List;

public interface TBsAssetAccountStreamMapper {

	
	/**
	 * 单个新增
	 */
	int singleInsert(TBsAssetAccountStream tBsAssetAccountStream);

	int batchInsert(List<TBsAssetAccountStream> insertStreamList);

	int update(TBsAssetAccountStream stream);

	List<TBsAssetAccountStream> listPage(TBsAssetAccountStream stream);
}
