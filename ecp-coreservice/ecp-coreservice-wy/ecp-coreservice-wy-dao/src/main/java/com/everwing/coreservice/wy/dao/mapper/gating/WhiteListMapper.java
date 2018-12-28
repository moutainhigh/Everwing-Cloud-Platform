package com.everwing.coreservice.wy.dao.mapper.gating;

import com.everwing.coreservice.common.wy.entity.gating.WhiteList;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2017/12/28.
 */
public interface WhiteListMapper {

    void batchInsert(@Param("whiteLists") List<WhiteList> whiteLists);

    List<Map<String,String>> selectByCPG(@Param("companyId") String companyId, @Param("projectId")String projectId,@Param("gatingId") String gatingId);
}
