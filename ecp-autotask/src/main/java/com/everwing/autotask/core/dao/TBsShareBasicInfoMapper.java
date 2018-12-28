package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsShareBasicsInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by DELL on 2018/6/4.
 */
@Repository
public interface TBsShareBasicInfoMapper {

    List<TBsShareBasicsInfo> getUsedShareInfo (String projectId, String shareType);

    int updateShareBasicInfo(List<String> ids);;
}
