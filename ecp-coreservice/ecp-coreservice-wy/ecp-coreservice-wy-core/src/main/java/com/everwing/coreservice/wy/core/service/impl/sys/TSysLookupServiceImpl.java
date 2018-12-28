package com.everwing.coreservice.wy.core.service.impl.sys;

import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.entity.system.lookup.*;
import com.everwing.coreservice.common.wy.service.sys.TSysLookupService;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysLookupMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */
@Service("tSysLookupServiceImpl")
public class TSysLookupServiceImpl implements TSysLookupService {
    @Autowired
    private TSysLookupMapper tSysLookupMapper;


   public BaseDto listPageLookup(WyBusinessContext ctx, TSysLookupSearch condition) {
       condition.setLan(ctx.getLan());
       List<TSysLookupList> list = tSysLookupMapper.listPageLookup(condition);

       BaseDto baseDto = new BaseDto<>();
       baseDto.setLstDto(list);
       baseDto.setPage(condition.getPage());
       return baseDto ;
    }


    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap saveLookup(WyBusinessContext ctx, TSysLookup entity) {
        MessageMap mm = new MessageMap();

        if(StringUtils.isEmpty(entity.getLookupId())){
            TSysLookupSearch condition = new TSysLookupSearch();
            condition.setProjectCode(entity.getProjectCode());
            condition.setParentCode(entity.getParentCode());
            condition.setCode(entity.getCode());
            condition.setLan(entity.getLan());
            condition.setItemOrder(entity.getItemOrder());
            List<TSysLookup> list = tSysLookupMapper.findLookupByCondtion(condition);
            if(CollectionUtils.isNotEmpty(list)){
                throw new ECPBusinessException("系统已经存在同样的编码，[父级编码，编码]["+entity.getParentCode()+"，"+entity.getCode()+"]");
            }else{
                entity.setStatus(LookupItemEnum.enableDisable_enable.getStringValue());
                int count = tSysLookupMapper.insertLookup(entity);
                mm.setMessage("新增了["+count+"]条记录");
            }

        }else{
            int count = tSysLookupMapper.modifyLookup(entity);
            mm.setMessage("["+count+"]条记录被修改了");
        }
        return mm;
    }


    public BaseDto listPageLookupItem(WyBusinessContext ctx, TSysLookupItemSearch condition) {
        condition.setLan(ctx.getLan());

        List<TSysLookupItemList> list = tSysLookupMapper.listPageLookupItem(condition);

        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(condition.getPage());
        return baseDto ;
    }


    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap saveLookupItem(WyBusinessContext ctx, TSysLookupItem entity) {
        MessageMap mm = new MessageMap();

        if(StringUtils.isEmpty(entity.getLookupItemId())){
            TSysLookupItemSearch condition = new TSysLookupItemSearch();
            condition.setParentCode(entity.getParentCode());
            condition.setCode(entity.getCode());
            condition.setLan(entity.getLan());
            condition.setItemOrder(entity.getItemOrder());
            List<TSysLookupItem> list = tSysLookupMapper.findLookupItemByCondtion(condition);
            if(CollectionUtils.isNotEmpty(list)){
                throw new ECPBusinessException("系统已经存在同样的编码，[父级编码，编码][" + entity.getParentCode()+"，"+entity.getCode()+"]");
            }else{
                entity.setStatus(LookupItemEnum.enableDisable_enable.getStringValue());
                int count = tSysLookupMapper.insertLookupItem(entity);
                mm.setMessage("新增了["+count+"]条记录");
            }
        }else{
            int count = tSysLookupMapper.modifyLookupItem(entity);
            mm.setMessage("["+count+"]条记录被修改了");
        }
        return mm;
    }

    /**
     * 获取lookup列表集合，如果根据项目编码找不到列表，则查询项目编码为空的列表
     * @param ctx
     * @param condition
     * @return
     */
    @Override
    public  List<TSysLookupSelectView> findLookup(WyBusinessContext ctx, TSysLookupSelectSearch condition) {
        condition.setLan(ctx.getLan());

        if(StringUtils.isBlank(CommonUtils.null2String(condition.getProjectCode()))){
            return tSysLookupMapper.findLookup(condition);
        }

        List<TSysLookupSelectView> lookupSelectViewList = tSysLookupMapper.findLookupByProjectCode(condition);

        if(CollectionUtils.isEmpty(lookupSelectViewList)){
            lookupSelectViewList =  tSysLookupMapper.findLookup(condition);;
        }
        return lookupSelectViewList;
    }

    /**
     * 获取lookupItem列表集合，如果根据项目编码找不到列表，则查询项目编码为空的列表
     * @param condition
     * @return
     */
    @Override
    public  List<TSysLookupSelectView> findLookupItem(WyBusinessContext ctx, TSysLookupSelectSearch condition) {
        condition.setLan(ctx.getLan());
        if(StringUtils.isBlank(CommonUtils.null2String(condition.getProjectCode()))){
            return tSysLookupMapper.findLookupItem(condition);
        }

        List<TSysLookupSelectView> lookupSelectViewList = tSysLookupMapper.findLookupItemByProjectCode(condition);

        if(CollectionUtils.isEmpty(lookupSelectViewList)){
            lookupSelectViewList =  tSysLookupMapper.findLookupItem(condition);;
        }
        return lookupSelectViewList;
    }

    /**
     *
     * @param ctx 上下文
     * @param parentCode 父级编码，可为空
     * @param name 名字，不能为空
     * @return
     */
    @Override
    public String getLookupCodeByName(WyBusinessContext ctx, String parentCode, String name) {
        TSysLookupSearch tSysLookupSearch = new TSysLookupSearch();
        tSysLookupSearch.setProjectCode(ctx.getProjectCode());
        tSysLookupSearch.setParentCode(parentCode);
        tSysLookupSearch.setName(name);
        tSysLookupSearch.setLan(ctx.getLan());

        List<TSysLookup> lookupList = tSysLookupMapper.findLookupByCondtion(tSysLookupSearch);

        if(CollectionUtils.isEmpty(lookupList)){
            tSysLookupSearch.setProjectCode(null);
            lookupList = tSysLookupMapper.findLookupByCondtion(tSysLookupSearch);
        }

        if(CollectionUtils.isNotEmpty(lookupList)){
            return lookupList.get(0).getCode();
        }
        return null;
    }

    /**
     *
     * @param ctx 上下文
     * @param parentCode 父级编码，可为空
     * @param name 名字，不能为空
     * @return
     */
    @Override
    public String getLookupItemCodeByName(WyBusinessContext ctx, String parentCode, String name) {
        TSysLookupItemSearch tSysLookupSelectSearch = new TSysLookupItemSearch();
        tSysLookupSelectSearch.setProjectCode(ctx.getProjectCode());
        tSysLookupSelectSearch.setParentCode(parentCode);
        tSysLookupSelectSearch.setName(name);
        tSysLookupSelectSearch.setLan(ctx.getLan());

        List<TSysLookupItem> lookupItemList = tSysLookupMapper.findLookupItemByCondtion(tSysLookupSelectSearch);

        if(CollectionUtils.isEmpty(lookupItemList)){
            tSysLookupSelectSearch.setProjectCode(null);
            lookupItemList = tSysLookupMapper.findLookupItemByCondtion(tSysLookupSelectSearch);
        }

        if(CollectionUtils.isNotEmpty(lookupItemList)){
            return lookupItemList.get(0).getCode();
        }
        return null;
    }

    @Override
    public String getLookupNameByCode(WyBusinessContext ctx, String parentCode, String code) {
        TSysLookupSearch tSysLookupSearch = new TSysLookupSearch();
        tSysLookupSearch.setProjectCode(ctx.getProjectCode());
        tSysLookupSearch.setParentCode(parentCode);
        tSysLookupSearch.setCode(code);
        tSysLookupSearch.setLan(ctx.getLan());

        List<TSysLookup> lookupList = tSysLookupMapper.findLookupByCondtion(tSysLookupSearch);

        if(CollectionUtils.isEmpty(lookupList)){
            tSysLookupSearch.setProjectCode(null);
            lookupList = tSysLookupMapper.findLookupByCondtion(tSysLookupSearch);
        }

        if(CollectionUtils.isNotEmpty(lookupList)){
            return lookupList.get(0).getName();
        }
        return null;
    }

    @Override
    public String getLookupItemNameByCode(WyBusinessContext ctx, String parentCode, String code) {
        TSysLookupItemSearch tSysLookupSelectSearch = new TSysLookupItemSearch();
        tSysLookupSelectSearch.setProjectCode(ctx.getProjectCode());
        tSysLookupSelectSearch.setParentCode(parentCode);
        tSysLookupSelectSearch.setCode(code);
        tSysLookupSelectSearch.setLan(ctx.getLan());

        List<TSysLookupItem> lookupItemList = tSysLookupMapper.findLookupItemByCondtion(tSysLookupSelectSearch);

        if(CollectionUtils.isEmpty(lookupItemList)){
            tSysLookupSelectSearch.setProjectCode(null);
            lookupItemList = tSysLookupMapper.findLookupItemByCondtion(tSysLookupSelectSearch);
        }

        if(CollectionUtils.isNotEmpty(lookupItemList)){
            return lookupItemList.get(0).getName();
        }
        return null;
    }
}