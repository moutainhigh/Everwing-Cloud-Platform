package com.everwing.coreservice.wy.dao.mapper.common;


import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * 导入导出mapper
 */
public interface ImportExportMapper {

    /**
     * 分页查询
     * @param condition
     * @return
     */
    List<TSysImportExportList> listPage(TSysImportExportSearch condition) throws DataAccessException;

    /**
     * 获取指定条件的信息
     * @param condition
     * @return
     */
    List<TSysImportExportList> findByCondtion(TSysImportExportSearch condition) throws DataAccessException;

    /**
     * 新增导入导出记录
     * @param list
     * @return
     */
    int batchInsert(List<TSysImportExport> list) throws DataAccessException;

    /**
     * 更新
     * @param entity
     * @return
     */
    int modify(TSysImportExport entity) throws DataAccessException;

    /**
     * 执行动态SQL
     * @param parameters
     * @return
     */
    List<Map<String, Object>> findBySql(Map<String,Object> parameters) throws DataAccessException;
}
