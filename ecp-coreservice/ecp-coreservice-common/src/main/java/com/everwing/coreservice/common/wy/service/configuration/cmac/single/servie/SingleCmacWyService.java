package com.everwing.coreservice.common.wy.service.configuration.cmac.single.servie;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;

public interface SingleCmacWyService extends ISingleCmacService{

	public void invoke(String companyId, TBsProject project,String buildCode);
}
