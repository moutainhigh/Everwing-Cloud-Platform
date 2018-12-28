package com.everwing.coreservice.platform.dao.mapper.gating.buildinggating;

import com.everwing.coreservice.common.wy.entity.gating.BuildingGate;

import java.util.List;

public interface BuildingGateExtraMapper {

	int batchAdd(List<BuildingGate> entities);

	int delete(String gatingCode);
}

