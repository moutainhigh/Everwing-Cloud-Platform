package com.everwing.coreservice.wy.core.service.impl.sys;/**
 * Created by wust on 2018/3/20.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.system.areas.TSysAreasList;
import com.everwing.coreservice.common.wy.entity.system.areas.TSysAreasSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysAreasService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/3/20
 * @author wusongti@lii.com.cn
 */
@Service("tSysAreasServiceImpl")
public class TSysAreasServiceImpl implements TSysAreasService {


    @Override
    public List<TSysAreasList> findByCondition(WyBusinessContext ctx, TSysAreasSearch tSysAreasSearch) {
        return null;
    }
}
