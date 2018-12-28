package com.everwing.coreservice.common.wy.service.configuration.tbcassetacount;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;

public interface TBsAssetAccountStreamService {

	BaseDto listPage(String companyId, TBsAssetAccountStream stream);

}
