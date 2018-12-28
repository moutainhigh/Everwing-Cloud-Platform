package com.everwing.coreservice.wy.core.service.impl.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartment;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartmentList;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartmentSearch;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganization;
import com.everwing.coreservice.common.wy.service.sys.TSysDepartmentService;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysDepartmentMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysOrganizationMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
@Service("tSysDepartmentServiceImpl")
public class TSysDepartmentServiceImpl implements TSysDepartmentService {
    @Autowired
    private TSysDepartmentMapper tSysDepartmentMapper;

    @Autowired
    private TSysOrganizationMapper tSysOrganizationMapper;

    @Override
    public BaseDto listPage(WyBusinessContext ctx, TSysDepartmentSearch condition) {
        List<TSysDepartmentList> list = tSysDepartmentMapper.listPage(condition);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(condition.getPage());
        return baseDto;
    }

    @Override
    public List<TSysDepartment> findByCondtion(WyBusinessContext ctx, TSysDepartmentSearch condition) {
        return tSysDepartmentMapper.findByCondition(condition);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap save(WyBusinessContext ctx, TSysDepartment entity) {
        MessageMap mm = new MessageMap();
        if(StringUtils.isNotEmpty(entity.getDepartmentId())){
            tSysDepartmentMapper.modify(entity);
        }else {
            tSysDepartmentMapper.insert(entity);
        }
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap delete(WyBusinessContext ctx, TSysDepartment entity) {
        MessageMap mm = new MessageMap();
        int delDepartmentCount = tSysDepartmentMapper.delete(entity);

        TSysOrganization tSysOrganization = new TSysOrganization();
        tSysOrganization.setCode(entity.getDepartmentId());
        tSysOrganization.setType(WyEnum.organizationType_department.getStringValue());
        int delOrganizationCount = tSysOrganizationMapper.delete(tSysOrganization);
        mm.setMessage("["+delDepartmentCount+"]条部门记录被删除，["+delOrganizationCount+"]条组织记录被删除。");
        return mm;
    }

    @Override
    public BaseDto findByCompanyIdAndProjectId(String companyId,String project){
        List<Map<String,Object>> result= tSysOrganizationMapper.selectByCAndP(companyId,project);
        BaseDto baseDto=new BaseDto();
        baseDto.setLstDto(result);
        return baseDto;
    }
}
