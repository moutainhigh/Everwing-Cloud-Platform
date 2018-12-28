package com.everwing.coreservice.wy.api.annex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.service.annex.AnnexService;

@Service("annexApi")
public class AnnexApi {

	@Autowired
	private AnnexService annexService;
	
	public RemoteModelResult<BaseDto> getListAnnexByReIdAndName(String companyId,String relationId, String annexName,String annexType){
		return new RemoteModelResult<BaseDto>(this.annexService.getListAnnexByReIdAndName(companyId, relationId, annexName,annexType));
	}
	
	public RemoteModelResult<BaseDto> addAnner(String companyId,Annex annex){
		return new RemoteModelResult<BaseDto>(this.annexService.addAnnex(companyId, annex));
	}
	
	public RemoteModelResult<BaseDto> queryByRelationId(String companyId,Annex annex){
		return new RemoteModelResult<BaseDto>(this.annexService.queryByRelationId(companyId, annex));
	}
	
	public RemoteModelResult<BaseDto> deleteEnclosureFile(String compamyId,Annex annex){
		return new RemoteModelResult<BaseDto>(this.annexService.deleteEnclosureFile(compamyId, annex));
	}
	
	/**
	 * 根据关联ID和时间查询
	 */
	public RemoteModelResult<BaseDto> queryBillEnclosure(WyBusinessContext ctx, Annex annex){
		return new RemoteModelResult<BaseDto>(this.annexService.queryBillEnclosure(ctx, annex));
	}

	public RemoteModelResult<BaseDto> queryById(String companyId, String annexId) {
		return new RemoteModelResult<BaseDto>(this.annexService.queryById(companyId, annexId));
	}
}
