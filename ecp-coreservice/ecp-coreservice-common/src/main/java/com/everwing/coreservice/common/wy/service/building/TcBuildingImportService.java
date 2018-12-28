package com.everwing.coreservice.common.wy.service.building;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;

/**
 * 导入建筑接口
 */
public interface TcBuildingImportService {

    /**
     * 导入入口
     * @param ctx
     * @param tSysImportExportRequest
     */
    void doImport(WyBusinessContext ctx, TSysImportExportSearch tSysImportExportRequest);

    /**
     * 导入回调方法
     * @param ctx
     * @param batchNo
     */
    MessageMap importCallback(WyBusinessContext ctx, String batchNo);

    /**
     * 导入完成后的操作，如记录日志等等
     * @param ctx
     * @param mm
     * @param batchNo
     */
    void after(WyBusinessContext ctx, MessageMap mm, String batchNo);
}
