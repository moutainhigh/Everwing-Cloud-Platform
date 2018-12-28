package com.everwing.coreservice.wy.core.service.impl.sys;/**
 * Created by wust on 2017/6/20.
 */

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.enums.ApplicationConstant;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.UserContextModel;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.system.organization.TSysOrganization;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.common.wy.entity.system.user.UserResourceList;
import com.everwing.coreservice.common.wy.service.sys.TSysOrganizationService;
import com.everwing.coreservice.common.wy.service.sys.TSysProjectService;
import com.everwing.coreservice.wy.dao.mapper.configuration.TBsProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysOrganizationMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysProjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/20
 * @author wusongti@lii.com.cn
 */
@Service("tSysProjectServiceImpl")
public class TSysProjectServiceImpl implements TSysProjectService{
    @Autowired
    private TSysProjectMapper tSysProjectMapper;

    @Autowired
    private TSysOrganizationMapper tSysOrganizationMapper;

    @Autowired
    private SpringRedisTools springRedisTools;
    
    @Autowired
    private TBsProjectMapper tBsProjectMapper;

    @Autowired
    private TSysOrganizationService tSysOrganizationService;

    @Override
    public BaseDto listPage(WyBusinessContext ctx, TSysProjectSearch condition) {
        MessageMap mm = new MessageMap();
        try {
            condition.setProjectIdList(getProjectIdsByLoginName(ctx.getLoginName()));
        } catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(e.getMessage());
        }
        List<TSysProjectList> list = tSysProjectMapper.listPage(condition);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setMessageMap(mm);
        baseDto.setLstDto(list);
        baseDto.setPage(condition.getPage());
        return baseDto;
    }

    @Override
    public BaseDto findByCondition(WyBusinessContext ctx, TSysProjectSearch condition) {
        MessageMap mm = new MessageMap();
        try {
            condition.setProjectIdList(getProjectIdsByLoginName(ctx.getLoginName()));
        } catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(e.getMessage());
        }
        BaseDto baseDto = new BaseDto<>();
        baseDto.setMessageMap(mm);
        baseDto.setLstDto(tSysProjectMapper.findByCondition(condition));
        return baseDto;
    }

    @Override
    public Map getSummaryInformationByProjectId(WyBusinessContext ctx, String projectId) {
        return tSysProjectMapper.getSummaryInformationByProjectId(projectId);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap save(String companyId,TSysProject entity) {
        MessageMap mm = new MessageMap();

        if(StringUtils.isNotEmpty(entity.getProjectId())){
            tSysProjectMapper.modify(entity);
            
            tBsProjectMapper.updateWhenTSysProjectUpdate(assembleTBsProject(entity));  //根据project_id来更改所有的项目(因为该表有历史数据)

        }else{
            entity.setStatus(LookupItemEnum.enableDisable_enable.getStringValue());
            tSysProjectMapper.insert(entity);
        }
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap delete(String companyId,TSysProject entity) {
        MessageMap mm = new MessageMap();
        int delProjectCount = tSysProjectMapper.delete(entity);

        TSysOrganization tSysOrganization = new TSysOrganization();
        tSysOrganization.setCode(entity.getProjectId());
        tSysOrganization.setType(WyEnum.organizationType_project.getStringValue());
        int delOrganizationCount = tSysOrganizationMapper.delete(tSysOrganization);
        mm.setMessage("["+delProjectCount+"]条项目记录被删除，["+delOrganizationCount+"]条组织记录被删除。");
        return mm;
    }


    @Override
    @Transactional(rollbackFor=Exception.class)
    public MessageMap changeState(String companyId,TSysProject entity) {
        MessageMap mm = new MessageMap();

        if(LookupItemEnum.enableDisable_enable.getStringValue().equals(entity.getStatus())){
            entity.setStatus(LookupItemEnum.enableDisable_disable.getStringValue());
            tSysProjectMapper.modify(entity);
        }else if(LookupItemEnum.enableDisable_disable.getStringValue().equals(entity.getStatus())){
            entity.setStatus(LookupItemEnum.enableDisable_enable.getStringValue());
            tSysProjectMapper.modify(entity);
        }else{
            throw new ECPBusinessException("没有这种状态["+entity.getStatus()+"]");
        }
        return mm;
    }



    public List<String> getProjectIdsByLoginName(String loginName){
        List<String> projectIds = new ArrayList<>(10);

        // 登录账号参数为空，则不允许查询
        if(StringUtils.isBlank(CommonUtils.null2String(loginName))){
           throw new ECPBusinessException("登录信息过期，请重新登录。");
        }

        String key = String.format(ApplicationConstant.WY_WEB_LOGIN_KEY.getStringValue(),loginName);
        UserContextModel userContext = springRedisTools.getByKey(key) == null ? null : (UserContextModel)springRedisTools.getByKey(key);
        if(userContext != null){
            // 系统管理员，不需要过滤
            if(LookupItemEnum.staffType_systemAdmin.getStringValue().equalsIgnoreCase(userContext.getLoginUser().getType())){
                return null;
            }

            String companyId = userContext.getLoginUser().getCompanyId();
            String staffNumber = userContext.getLoginUser().getStaffNumber();
            List<UserResourceList> userResourceLists = tSysOrganizationService.getUserResourceListByKey(companyId,staffNumber);
            if(CollectionUtils.isNotEmpty(userResourceLists)){
                for (UserResourceList userResourceList : userResourceLists) {
                    projectIds.add(userResourceList.getProjectId());
                }
            }
        }else{
            throw new ECPBusinessException("登录信息过期，请重新登录。");
        }
        return projectIds;
    }
    
    private TBsProject assembleTBsProject(TSysProject pro){
        /** 项目新增时, 同时创建项目计费状态数据 */
        TBsProject bp = new TBsProject();
        bp.setId(CommonUtils.getUUID());
        bp.setProjectId(pro.getCode());
        bp.setProjectName(pro.getName());
        bp.setModifyId(pro.getModifyId());
        bp.setModifyTime(new Date());
        return bp;
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto findAllProject(String companyId,TSysProjectSearch search) {
		BaseDto returnDto = new BaseDto();
		returnDto.setLstDto(this.tSysProjectMapper.findByCondition(search));
		return returnDto;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageAndCountMeters(String companyId,TSysProjectSearch condition) {
		BaseDto returnDto = new BaseDto();
		condition.setProjectIdList(getProjectIdsByLoginName(condition.getLoginName()));
		returnDto.setLstDto(this.tSysProjectMapper.listPageAndCountMeters(condition));
		returnDto.setPage(condition.getPage());
		returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		return returnDto; 
	}
}