package com.everwing.coreservice.platform.core.service.impl;

import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.service.BuildingService;
import com.everwing.coreservice.platform.dao.mapper.extra.BuildingExtraMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingExtraMapper buildingExtraMapper;

    @Override
    public LinphoneResult queryByMobile(String accountId, String mobile) {
        return new LinphoneResult(buildingExtraMapper.selectByMobile(mobile));
    }

    @Override
    public RemoteModelResult queryByBuildingId(String buildingId) {
        Map<String,Object> result= buildingExtraMapper.selectById(buildingId);
        List<Map<String,String>> owners=buildingExtraMapper.queryOwnersByBId(buildingId);
        result.put("owners",owners);
        return new RemoteModelResult(result);
    }
}