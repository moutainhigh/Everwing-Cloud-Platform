package com.everwing.coreservice.wy.core.service.impl.configuration.tbcassetacount;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.service.configuration.tbcassetacount.TBsAssetAccountStreamService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.springframework.stereotype.Service;

@Service("tBsAssetAccountStreamService")
public class TBsAssetAccountStreamServiceImpl extends Resources implements TBsAssetAccountStreamService{

	@Override
	public BaseDto listPage(String companyId, TBsAssetAccountStream stream) {
		return new BaseDto(this.tBsAssetAccountStreamMapper.listPage(stream),stream.getPage());
	}

}
