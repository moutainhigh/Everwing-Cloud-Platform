package com.everwing.coreservice.common.wy.service.business.pay;

import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;

import java.util.List;

public interface TBspayInfoImportService {
    /**
     * 财务管理导入新账户

     * @param tSysImportExportRequest
     * @param singleStr
     * @param isNotSkipLateFee
     */
    void importTcbuilding( TSysImportExportSearch tSysImportExportRequest, String singleStr, String isNotSkipLateFee);

    /**
     * 导入回调方法

     * @param tSysImportExportRequest

     */
    List<TBsPayInfo> importTcbuildingCallback(TSysImportExportSearch tSysImportExportRequest);
}
