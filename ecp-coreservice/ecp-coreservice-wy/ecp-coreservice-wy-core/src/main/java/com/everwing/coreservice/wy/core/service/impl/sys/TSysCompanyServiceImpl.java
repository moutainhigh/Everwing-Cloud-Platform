package com.everwing.coreservice.wy.core.service.impl.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompany;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanyList;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanySearch;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganization;
import com.everwing.coreservice.common.wy.service.sys.TSysCompanyService;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysCompanyMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysOrganizationMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
@Service("tSysCompanyServiceImpl")
public class TSysCompanyServiceImpl implements TSysCompanyService {
    @Autowired
    private TSysCompanyMapper tSysCompanyMapper;

    @Autowired
    private TSysOrganizationMapper tSysOrganizationMapper;

    @Override
    public BaseDto listPage(String companyId,TSysCompanySearch condition) {
        List<TSysCompanyList> list = tSysCompanyMapper.listPage(condition);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(condition.getPage());
        return baseDto;
    }

    @Override
    public List<TSysCompanyList> findByCondtion(String companyId,TSysCompanySearch condition) {
        return tSysCompanyMapper.findByCondition(condition);
    }


    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap save(String companyId,TSysCompany entity) {
        MessageMap mm = new MessageMap();

        if(StringUtils.isNotEmpty(entity.getCompanyId())){
            tSysCompanyMapper.modify(entity);
        }else{
            tSysCompanyMapper.insert(entity);
        }
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap delete(String companyId,TSysCompany entity) {
        MessageMap mm = new MessageMap();
        int delCompanyCount = tSysCompanyMapper.delete(entity);

        TSysOrganization tSysOrganization = new TSysOrganization();
        tSysOrganization.setCode(entity.getCompanyId());
        tSysOrganization.setType(WyEnum.organizationType_company.getStringValue());
        int delOrganizationCount = tSysOrganizationMapper.delete(tSysOrganization);
        mm.setMessage("["+delCompanyCount+"]条公司记录被删除，["+delOrganizationCount+"]条组织记录被删除。");
        return mm;
    }
}
