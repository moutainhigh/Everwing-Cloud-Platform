package com.everwing.coreservice.common.wy.service.sys;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.lookup.*;

import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */
public interface TSysLookupService {
    /**
     * 属性主表分页查询
     * @param condition 条件
     * @return
     */
    BaseDto listPageLookup(WyBusinessContext ctx, TSysLookupSearch condition);

    /**
     * 属性主表保存操作
     * @param entity
     */
    MessageMap saveLookup(WyBusinessContext ctx, TSysLookup entity);

    /**
     * 属性子表分页查询
     * @param condition
     * @return
     */
    BaseDto listPageLookupItem(WyBusinessContext ctx, TSysLookupItemSearch condition);

    /**
     * 属性子表保存操作
     * @param entity
     */
    MessageMap saveLookupItem(WyBusinessContext ctx, TSysLookupItem entity);


    /**
     * 根据父类编码获取主表属性集合
     * @param condition
     * @return
     */
    List<TSysLookupSelectView> findLookup(WyBusinessContext ctx, TSysLookupSelectSearch condition);

    /**
     * 根据父类编码获取子表属性集合
     * @param condition
     * @return
     */
    List<TSysLookupSelectView> findLookupItem(WyBusinessContext ctx, TSysLookupSelectSearch condition);

    /**
     * lookup:根据名字获取code
     * @param ctx 上下文
     * @param parentCode 父级编码，可为空
     * @param name 名字，不能为空
     * @return
     */
    String getLookupCodeByName(WyBusinessContext ctx, String parentCode, String name);


    /**
     * lookupItem:根据名字获取code
     * @param ctx 上下文
     * @param parentCode 父级编码，可为空
     * @param name 名字，不能为空
     * @return
     */
    String getLookupItemCodeByName(WyBusinessContext ctx, String parentCode, String name);


    /**
     * lookup:根据code获取name
     * @param ctx
     * @param parentCode 父编码
     * @param code      子编码
     * @return
     */
    String getLookupNameByCode(WyBusinessContext ctx, String parentCode, String code);

    /**
     * lookupItem:根据code获取name
     * @param ctx
     * @param parentCode 父编码
     * @param code      子编码
     * @return
     */
    String getLookupItemNameByCode(WyBusinessContext ctx, String parentCode, String code);
}
