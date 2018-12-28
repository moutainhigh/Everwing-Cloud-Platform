package com.everwing.coreservice.wy.dao.mapper.sys;

import com.everwing.coreservice.common.wy.entity.system.lookup.*;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */
public interface TSysLookupMapper {
    /**
     * 属性主表分页查询
     * @param condition
     * @return
     */
    List<TSysLookupList> listPageLookup(TSysLookupSearch condition) throws DataAccessException;

    /**
     * 属性主表新增操作
     * @param entity
     * @return
     */
    int insertLookup(TSysLookup entity) throws DataAccessException;

    /**
     * 属性主表修改操作
     * @param entity
     * @return
     */
    int modifyLookup(TSysLookup entity) throws DataAccessException;


    /**
     * 属性子表分页查询
     * @param condition
     * @return
     */
    List<TSysLookupItemList> listPageLookupItem(TSysLookupItemSearch condition) throws DataAccessException;


    /**
     * 属性子表新增操作
     * @param entity
     * @return
     */
    int insertLookupItem(TSysLookupItem entity) throws DataAccessException;

    /**
     * 属性子表修改操作
     * @param entity
     * @return
     */
    int modifyLookupItem(TSysLookupItem entity) throws DataAccessException;

    /**
     * 获取指定条件的主表信息
     * @param condition
     * @return
     */
    List<TSysLookup> findLookupByCondtion(TSysLookupSearch condition) throws DataAccessException;


    /**
     * 获取指定条件的子表信息
     * @param condition
     * @return
     */
    List<TSysLookupItem> findLookupItemByCondtion(TSysLookupItemSearch condition) throws DataAccessException;

    /**
     * 根据父类编码获取主表属性集合
     * @param condition
     * @return
     */
    List<TSysLookupSelectView> findLookup(TSysLookupSelectSearch condition) throws DataAccessException;

    /**
     * 根据父类编码和项目编码获取主表属性集合
     * @param condition
     * @return
     */
    List<TSysLookupSelectView> findLookupByProjectCode(TSysLookupSelectSearch condition) throws DataAccessException;

    /**
     * 根据父类编码获取子表属性集合
     * @param condition
     * @return
     */
    List<TSysLookupSelectView> findLookupItem(TSysLookupSelectSearch condition) throws DataAccessException;

    /**
     * 根据父类编码和项目编码获取子表属性集合
     * @param condition
     * @return
     */
    List<TSysLookupSelectView> findLookupItemByProjectCode(TSysLookupSelectSearch condition) throws DataAccessException;
}
