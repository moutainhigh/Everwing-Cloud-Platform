package com.everwing.coreservice.wy.core.service.impl.sys;/**
 * Created by wust on 2018/8/6.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.system.code.TSysCode;
import com.everwing.coreservice.common.wy.entity.system.code.TSysCodeSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysCodeService;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/8/6
 * @author wusongti@lii.com.cn
 */
@Service("tSysCodeServiceImpl")
public class TSysCodeServiceImpl implements TSysCodeService {
    @Autowired
    private TSysCodeMapper tSysCodeMapper;

    @Override
    public List<TSysCode> findByCondition(WyBusinessContext ctx, TSysCodeSearch tSysCodeSearch) {
        return tSysCodeMapper.findByCondition(tSysCodeSearch);
    }

    @Override
    public int insert(WyBusinessContext ctx, TSysCode tSysCode) {
        return tSysCodeMapper.insert(tSysCode);
    }

    @Override
    public int update(WyBusinessContext ctx, TSysCode tSysCode) {
        return tSysCodeMapper.update(tSysCode);
    }
}
