package com.everwing.coreservice.common.wy.service.sys;/**
 * Created by wust on 2017/4/26.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.importExport.Excel;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExport;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/4/26
 * @author wusongti@lii.com.cn
 */
public interface TSysImportExportService {
    /**
     * 分页查询
     * @param condition
     * @return
     */
    BaseDto listPage(WyBusinessContext ctx, TSysImportExportSearch condition);

    /**
     * 获取指定条件的信息
     * @param condition
     * @return
     */
    List<TSysImportExportList> findByCondtion(String companyId, TSysImportExportSearch condition);

    /**
     * 新增导入导出记录
     * @param list
     * @return
     */
    MessageMap batchInsert(String companyId, List<TSysImportExport> list);

    /**
     * 更新
     * @param entity
     * @return
     */
    MessageMap modify(TSysImportExport entity);
    
    /**
     * 更新
     * @param entity
     * @return
     */
    MessageMap modify(String companyId, TSysImportExport entity);


    /**
     * 导出excel
     * @param ctx
     * @param excel
     * @return
     */
    MessageMap exportExcel(WyBusinessContext ctx, Excel excel);

    /**
     * 导出回调方法，多线程会涉及到切换数据源问题，采用回调实现
     * @param ctx
     */
    void exportExcelCallback(WyBusinessContext ctx);
}
