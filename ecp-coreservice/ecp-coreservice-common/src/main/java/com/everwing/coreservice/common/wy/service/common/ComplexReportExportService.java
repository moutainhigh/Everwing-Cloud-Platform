package com.everwing.coreservice.common.wy.service.common;/**
 * Created by wust on 2018/1/15.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.importExport.Excel;

/**
 *
 * Function:复杂报表组件
 * Reason:你只要写自己的实现类并且继承对应的组件类
 * Date:2018/1/15
 * @author wusongti@lii.com.cn
 */
public interface ComplexReportExportService {
    /**
     * 导出格式固定的复杂excel报表
     * @param ctx
     * @param excel
     * @return
     */
    MessageMap exportFixedExcel(WyBusinessContext ctx, Excel excel);
}
