package com.everwing.coreservice.platform.dao.mapper.gating;

import com.everwing.coreservice.common.wy.entity.gating.Gating;

import java.util.List;

public interface GatingExtraMapper {


	int addGating(Gating entity);
	
	int updateGating(Gating entity);
	
	int delGating(Gating entity);
	
	int batchAdd(List<Gating> entities);
	
	int batchDel(List<String> entites);
	

}
