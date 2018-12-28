package com.everwing.coreservice.wy.api.common;/**
 * Created by wust on 2018/1/17.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.system.importExport.Excel;
import com.everwing.coreservice.common.wy.service.common.ComplexReportExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/17
 * @author wusongti@lii.com.cn
 */
@Component
public class ComplexReportExportApi {
    @Autowired
    private ComplexReportExportService complexReportExportDemoService;

    /**
     * 导出格式固定的复杂excel报表
     * @param ctx
     * @param excel
     * @return
     */
    public RemoteModelResult<MessageMap> exportDemo(WyBusinessContext ctx, Excel excel) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(complexReportExportDemoService.exportFixedExcel(ctx,excel));
        return result;
    }
}
