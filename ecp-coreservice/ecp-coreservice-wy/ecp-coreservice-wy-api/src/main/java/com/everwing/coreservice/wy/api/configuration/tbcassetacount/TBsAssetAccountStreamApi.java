package com.everwing.coreservice.wy.api.configuration.tbcassetacount;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.service.configuration.tbcassetacount.TBsAssetAccountStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tBsAssetAccountStreamApi")
public class TBsAssetAccountStreamApi {

	@Autowired
	private TBsAssetAccountStreamService tBsAssetAccountStreamService;

	public RemoteModelResult<BaseDto> listPage(String companyId,TBsAssetAccountStream stream) {
		return new RemoteModelResult<BaseDto>(this.tBsAssetAccountStreamService.listPage(companyId,stream));
	}
	
	
	
}
