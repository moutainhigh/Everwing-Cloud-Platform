package com.everwing.coreservice.common.wy.service.annex;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.annex.Annex;

public interface AnnexService {

	/**
	 * 根据关联ID和附件名称查询
	 * @param relationId
	 * @param annexName
	 * @return
	 */
	BaseDto getListAnnexByReIdAndName(String companyId,String relationId,String annexName,String annexType);
	
	/**
	 * 新增附件
	 */
	BaseDto addAnnex(String CompanyId,Annex annex);
	
	/**
	 * 根据关联Id查询
	 * @param companyId
	 * @param relationId
	 * @return
	 */
	BaseDto queryByRelationId(String companyId,Annex annex);
	
	/**
	 * 删除附件
	 */
	BaseDto deleteEnclosureFile(String compamyId,Annex annex);
	
	/**
	 * 查询账单附件列表
	 * @param ctx
	 * @param annex
	 */
	BaseDto queryBillEnclosure(WyBusinessContext ctx, Annex annex);

	BaseDto queryById(String companyId, String annexId);
}
