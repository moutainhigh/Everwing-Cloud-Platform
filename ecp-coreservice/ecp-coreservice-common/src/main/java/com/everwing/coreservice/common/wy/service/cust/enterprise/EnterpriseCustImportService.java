package com.everwing.coreservice.common.wy.service.cust.enterprise;

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;

public interface EnterpriseCustImportService {

    void doImport(WyBusinessContext ctx, TSysImportExportSearch tSysImportExportRequest);

    MessageMap importCallback(WyBusinessContext ctx, String batchNo);

    void after(WyBusinessContext ctx, MessageMap mm, String batchNo);
}
